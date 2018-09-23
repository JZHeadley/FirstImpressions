package com.jzheadley.firstimpressions.web.rest;

import com.jzheadley.firstimpressions.FirstImpressionsApp;

import com.jzheadley.firstimpressions.domain.ClothingCompany;
import com.jzheadley.firstimpressions.repository.ClothingCompanyRepository;
import com.jzheadley.firstimpressions.repository.search.ClothingCompanySearchRepository;
import com.jzheadley.firstimpressions.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.jzheadley.firstimpressions.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClothingCompanyResource REST controller.
 *
 * @see ClothingCompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FirstImpressionsApp.class)
public class ClothingCompanyResourceIntTest {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_DESC = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_DESC = "BBBBBBBBBB";

    @Autowired
    private ClothingCompanyRepository clothingCompanyRepository;

    /**
     * This repository is mocked in the com.jzheadley.firstimpressions.repository.search test package.
     *
     * @see com.jzheadley.firstimpressions.repository.search.ClothingCompanySearchRepositoryMockConfiguration
     */
    @Autowired
    private ClothingCompanySearchRepository mockClothingCompanySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClothingCompanyMockMvc;

    private ClothingCompany clothingCompany;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClothingCompanyResource clothingCompanyResource = new ClothingCompanyResource(clothingCompanyRepository, mockClothingCompanySearchRepository);
        this.restClothingCompanyMockMvc = MockMvcBuilders.standaloneSetup(clothingCompanyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClothingCompany createEntity(EntityManager em) {
        ClothingCompany clothingCompany = new ClothingCompany()
            .companyName(DEFAULT_COMPANY_NAME)
            .companyDesc(DEFAULT_COMPANY_DESC);
        return clothingCompany;
    }

    @Before
    public void initTest() {
        clothingCompany = createEntity(em);
    }

    @Test
    @Transactional
    public void createClothingCompany() throws Exception {
        int databaseSizeBeforeCreate = clothingCompanyRepository.findAll().size();

        // Create the ClothingCompany
        restClothingCompanyMockMvc.perform(post("/api/clothing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clothingCompany)))
            .andExpect(status().isCreated());

        // Validate the ClothingCompany in the database
        List<ClothingCompany> clothingCompanyList = clothingCompanyRepository.findAll();
        assertThat(clothingCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        ClothingCompany testClothingCompany = clothingCompanyList.get(clothingCompanyList.size() - 1);
        assertThat(testClothingCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testClothingCompany.getCompanyDesc()).isEqualTo(DEFAULT_COMPANY_DESC);

        // Validate the ClothingCompany in Elasticsearch
        verify(mockClothingCompanySearchRepository, times(1)).save(testClothingCompany);
    }

    @Test
    @Transactional
    public void createClothingCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clothingCompanyRepository.findAll().size();

        // Create the ClothingCompany with an existing ID
        clothingCompany.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClothingCompanyMockMvc.perform(post("/api/clothing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clothingCompany)))
            .andExpect(status().isBadRequest());

        // Validate the ClothingCompany in the database
        List<ClothingCompany> clothingCompanyList = clothingCompanyRepository.findAll();
        assertThat(clothingCompanyList).hasSize(databaseSizeBeforeCreate);

        // Validate the ClothingCompany in Elasticsearch
        verify(mockClothingCompanySearchRepository, times(0)).save(clothingCompany);
    }

    @Test
    @Transactional
    public void getAllClothingCompanies() throws Exception {
        // Initialize the database
        clothingCompanyRepository.saveAndFlush(clothingCompany);

        // Get all the clothingCompanyList
        restClothingCompanyMockMvc.perform(get("/api/clothing-companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clothingCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyDesc").value(hasItem(DEFAULT_COMPANY_DESC.toString())));
    }
    
    @Test
    @Transactional
    public void getClothingCompany() throws Exception {
        // Initialize the database
        clothingCompanyRepository.saveAndFlush(clothingCompany);

        // Get the clothingCompany
        restClothingCompanyMockMvc.perform(get("/api/clothing-companies/{id}", clothingCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clothingCompany.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.companyDesc").value(DEFAULT_COMPANY_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClothingCompany() throws Exception {
        // Get the clothingCompany
        restClothingCompanyMockMvc.perform(get("/api/clothing-companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClothingCompany() throws Exception {
        // Initialize the database
        clothingCompanyRepository.saveAndFlush(clothingCompany);

        int databaseSizeBeforeUpdate = clothingCompanyRepository.findAll().size();

        // Update the clothingCompany
        ClothingCompany updatedClothingCompany = clothingCompanyRepository.findById(clothingCompany.getId()).get();
        // Disconnect from session so that the updates on updatedClothingCompany are not directly saved in db
        em.detach(updatedClothingCompany);
        updatedClothingCompany
            .companyName(UPDATED_COMPANY_NAME)
            .companyDesc(UPDATED_COMPANY_DESC);

        restClothingCompanyMockMvc.perform(put("/api/clothing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClothingCompany)))
            .andExpect(status().isOk());

        // Validate the ClothingCompany in the database
        List<ClothingCompany> clothingCompanyList = clothingCompanyRepository.findAll();
        assertThat(clothingCompanyList).hasSize(databaseSizeBeforeUpdate);
        ClothingCompany testClothingCompany = clothingCompanyList.get(clothingCompanyList.size() - 1);
        assertThat(testClothingCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testClothingCompany.getCompanyDesc()).isEqualTo(UPDATED_COMPANY_DESC);

        // Validate the ClothingCompany in Elasticsearch
        verify(mockClothingCompanySearchRepository, times(1)).save(testClothingCompany);
    }

    @Test
    @Transactional
    public void updateNonExistingClothingCompany() throws Exception {
        int databaseSizeBeforeUpdate = clothingCompanyRepository.findAll().size();

        // Create the ClothingCompany

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClothingCompanyMockMvc.perform(put("/api/clothing-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clothingCompany)))
            .andExpect(status().isBadRequest());

        // Validate the ClothingCompany in the database
        List<ClothingCompany> clothingCompanyList = clothingCompanyRepository.findAll();
        assertThat(clothingCompanyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ClothingCompany in Elasticsearch
        verify(mockClothingCompanySearchRepository, times(0)).save(clothingCompany);
    }

    @Test
    @Transactional
    public void deleteClothingCompany() throws Exception {
        // Initialize the database
        clothingCompanyRepository.saveAndFlush(clothingCompany);

        int databaseSizeBeforeDelete = clothingCompanyRepository.findAll().size();

        // Get the clothingCompany
        restClothingCompanyMockMvc.perform(delete("/api/clothing-companies/{id}", clothingCompany.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClothingCompany> clothingCompanyList = clothingCompanyRepository.findAll();
        assertThat(clothingCompanyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ClothingCompany in Elasticsearch
        verify(mockClothingCompanySearchRepository, times(1)).deleteById(clothingCompany.getId());
    }

    @Test
    @Transactional
    public void searchClothingCompany() throws Exception {
        // Initialize the database
        clothingCompanyRepository.saveAndFlush(clothingCompany);
        when(mockClothingCompanySearchRepository.search(queryStringQuery("id:" + clothingCompany.getId())))
            .thenReturn(Collections.singletonList(clothingCompany));
        // Search the clothingCompany
        restClothingCompanyMockMvc.perform(get("/api/_search/clothing-companies?query=id:" + clothingCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clothingCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyDesc").value(hasItem(DEFAULT_COMPANY_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClothingCompany.class);
        ClothingCompany clothingCompany1 = new ClothingCompany();
        clothingCompany1.setId(1L);
        ClothingCompany clothingCompany2 = new ClothingCompany();
        clothingCompany2.setId(clothingCompany1.getId());
        assertThat(clothingCompany1).isEqualTo(clothingCompany2);
        clothingCompany2.setId(2L);
        assertThat(clothingCompany1).isNotEqualTo(clothingCompany2);
        clothingCompany1.setId(null);
        assertThat(clothingCompany1).isNotEqualTo(clothingCompany2);
    }
}

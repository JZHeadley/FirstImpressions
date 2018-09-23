package com.jzheadley.firstimpressions.web.rest;

import com.jzheadley.firstimpressions.FirstImpressionsApp;

import com.jzheadley.firstimpressions.domain.InterviewResources;
import com.jzheadley.firstimpressions.repository.InterviewResourcesRepository;
import com.jzheadley.firstimpressions.repository.search.InterviewResourcesSearchRepository;
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
 * Test class for the InterviewResourcesResource REST controller.
 *
 * @see InterviewResourcesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FirstImpressionsApp.class)
public class InterviewResourcesResourceIntTest {

    private static final String DEFAULT_RESOURCE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private InterviewResourcesRepository interviewResourcesRepository;

    /**
     * This repository is mocked in the com.jzheadley.firstimpressions.repository.search test package.
     *
     * @see com.jzheadley.firstimpressions.repository.search.InterviewResourcesSearchRepositoryMockConfiguration
     */
    @Autowired
    private InterviewResourcesSearchRepository mockInterviewResourcesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInterviewResourcesMockMvc;

    private InterviewResources interviewResources;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InterviewResourcesResource interviewResourcesResource = new InterviewResourcesResource(interviewResourcesRepository, mockInterviewResourcesSearchRepository);
        this.restInterviewResourcesMockMvc = MockMvcBuilders.standaloneSetup(interviewResourcesResource)
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
    public static InterviewResources createEntity(EntityManager em) {
        InterviewResources interviewResources = new InterviewResources()
            .resourceLink(DEFAULT_RESOURCE_LINK)
            .desc(DEFAULT_DESC);
        return interviewResources;
    }

    @Before
    public void initTest() {
        interviewResources = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterviewResources() throws Exception {
        int databaseSizeBeforeCreate = interviewResourcesRepository.findAll().size();

        // Create the InterviewResources
        restInterviewResourcesMockMvc.perform(post("/api/interview-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interviewResources)))
            .andExpect(status().isCreated());

        // Validate the InterviewResources in the database
        List<InterviewResources> interviewResourcesList = interviewResourcesRepository.findAll();
        assertThat(interviewResourcesList).hasSize(databaseSizeBeforeCreate + 1);
        InterviewResources testInterviewResources = interviewResourcesList.get(interviewResourcesList.size() - 1);
        assertThat(testInterviewResources.getResourceLink()).isEqualTo(DEFAULT_RESOURCE_LINK);
        assertThat(testInterviewResources.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the InterviewResources in Elasticsearch
        verify(mockInterviewResourcesSearchRepository, times(1)).save(testInterviewResources);
    }

    @Test
    @Transactional
    public void createInterviewResourcesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interviewResourcesRepository.findAll().size();

        // Create the InterviewResources with an existing ID
        interviewResources.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewResourcesMockMvc.perform(post("/api/interview-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interviewResources)))
            .andExpect(status().isBadRequest());

        // Validate the InterviewResources in the database
        List<InterviewResources> interviewResourcesList = interviewResourcesRepository.findAll();
        assertThat(interviewResourcesList).hasSize(databaseSizeBeforeCreate);

        // Validate the InterviewResources in Elasticsearch
        verify(mockInterviewResourcesSearchRepository, times(0)).save(interviewResources);
    }

    @Test
    @Transactional
    public void getAllInterviewResources() throws Exception {
        // Initialize the database
        interviewResourcesRepository.saveAndFlush(interviewResources);

        // Get all the interviewResourcesList
        restInterviewResourcesMockMvc.perform(get("/api/interview-resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewResources.getId().intValue())))
            .andExpect(jsonPath("$.[*].resourceLink").value(hasItem(DEFAULT_RESOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    
    @Test
    @Transactional
    public void getInterviewResources() throws Exception {
        // Initialize the database
        interviewResourcesRepository.saveAndFlush(interviewResources);

        // Get the interviewResources
        restInterviewResourcesMockMvc.perform(get("/api/interview-resources/{id}", interviewResources.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interviewResources.getId().intValue()))
            .andExpect(jsonPath("$.resourceLink").value(DEFAULT_RESOURCE_LINK.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInterviewResources() throws Exception {
        // Get the interviewResources
        restInterviewResourcesMockMvc.perform(get("/api/interview-resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterviewResources() throws Exception {
        // Initialize the database
        interviewResourcesRepository.saveAndFlush(interviewResources);

        int databaseSizeBeforeUpdate = interviewResourcesRepository.findAll().size();

        // Update the interviewResources
        InterviewResources updatedInterviewResources = interviewResourcesRepository.findById(interviewResources.getId()).get();
        // Disconnect from session so that the updates on updatedInterviewResources are not directly saved in db
        em.detach(updatedInterviewResources);
        updatedInterviewResources
            .resourceLink(UPDATED_RESOURCE_LINK)
            .desc(UPDATED_DESC);

        restInterviewResourcesMockMvc.perform(put("/api/interview-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInterviewResources)))
            .andExpect(status().isOk());

        // Validate the InterviewResources in the database
        List<InterviewResources> interviewResourcesList = interviewResourcesRepository.findAll();
        assertThat(interviewResourcesList).hasSize(databaseSizeBeforeUpdate);
        InterviewResources testInterviewResources = interviewResourcesList.get(interviewResourcesList.size() - 1);
        assertThat(testInterviewResources.getResourceLink()).isEqualTo(UPDATED_RESOURCE_LINK);
        assertThat(testInterviewResources.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the InterviewResources in Elasticsearch
        verify(mockInterviewResourcesSearchRepository, times(1)).save(testInterviewResources);
    }

    @Test
    @Transactional
    public void updateNonExistingInterviewResources() throws Exception {
        int databaseSizeBeforeUpdate = interviewResourcesRepository.findAll().size();

        // Create the InterviewResources

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewResourcesMockMvc.perform(put("/api/interview-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interviewResources)))
            .andExpect(status().isBadRequest());

        // Validate the InterviewResources in the database
        List<InterviewResources> interviewResourcesList = interviewResourcesRepository.findAll();
        assertThat(interviewResourcesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterviewResources in Elasticsearch
        verify(mockInterviewResourcesSearchRepository, times(0)).save(interviewResources);
    }

    @Test
    @Transactional
    public void deleteInterviewResources() throws Exception {
        // Initialize the database
        interviewResourcesRepository.saveAndFlush(interviewResources);

        int databaseSizeBeforeDelete = interviewResourcesRepository.findAll().size();

        // Get the interviewResources
        restInterviewResourcesMockMvc.perform(delete("/api/interview-resources/{id}", interviewResources.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InterviewResources> interviewResourcesList = interviewResourcesRepository.findAll();
        assertThat(interviewResourcesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InterviewResources in Elasticsearch
        verify(mockInterviewResourcesSearchRepository, times(1)).deleteById(interviewResources.getId());
    }

    @Test
    @Transactional
    public void searchInterviewResources() throws Exception {
        // Initialize the database
        interviewResourcesRepository.saveAndFlush(interviewResources);
        when(mockInterviewResourcesSearchRepository.search(queryStringQuery("id:" + interviewResources.getId())))
            .thenReturn(Collections.singletonList(interviewResources));
        // Search the interviewResources
        restInterviewResourcesMockMvc.perform(get("/api/_search/interview-resources?query=id:" + interviewResources.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewResources.getId().intValue())))
            .andExpect(jsonPath("$.[*].resourceLink").value(hasItem(DEFAULT_RESOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterviewResources.class);
        InterviewResources interviewResources1 = new InterviewResources();
        interviewResources1.setId(1L);
        InterviewResources interviewResources2 = new InterviewResources();
        interviewResources2.setId(interviewResources1.getId());
        assertThat(interviewResources1).isEqualTo(interviewResources2);
        interviewResources2.setId(2L);
        assertThat(interviewResources1).isNotEqualTo(interviewResources2);
        interviewResources1.setId(null);
        assertThat(interviewResources1).isNotEqualTo(interviewResources2);
    }
}

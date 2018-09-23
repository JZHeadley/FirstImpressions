package com.jzheadley.firstimpressions.web.rest;

import com.jzheadley.firstimpressions.FirstImpressionsApp;

import com.jzheadley.firstimpressions.domain.OnlineTraining;
import com.jzheadley.firstimpressions.repository.OnlineTrainingRepository;
import com.jzheadley.firstimpressions.repository.search.OnlineTrainingSearchRepository;
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
 * Test class for the OnlineTrainingResource REST controller.
 *
 * @see OnlineTrainingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FirstImpressionsApp.class)
public class OnlineTrainingResourceIntTest {

    private static final String DEFAULT_TRAINING_LINK = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private OnlineTrainingRepository onlineTrainingRepository;

    /**
     * This repository is mocked in the com.jzheadley.firstimpressions.repository.search test package.
     *
     * @see com.jzheadley.firstimpressions.repository.search.OnlineTrainingSearchRepositoryMockConfiguration
     */
    @Autowired
    private OnlineTrainingSearchRepository mockOnlineTrainingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOnlineTrainingMockMvc;

    private OnlineTraining onlineTraining;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OnlineTrainingResource onlineTrainingResource = new OnlineTrainingResource(onlineTrainingRepository, mockOnlineTrainingSearchRepository);
        this.restOnlineTrainingMockMvc = MockMvcBuilders.standaloneSetup(onlineTrainingResource)
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
    public static OnlineTraining createEntity(EntityManager em) {
        OnlineTraining onlineTraining = new OnlineTraining()
            .trainingLink(DEFAULT_TRAINING_LINK)
            .desc(DEFAULT_DESC);
        return onlineTraining;
    }

    @Before
    public void initTest() {
        onlineTraining = createEntity(em);
    }

    @Test
    @Transactional
    public void createOnlineTraining() throws Exception {
        int databaseSizeBeforeCreate = onlineTrainingRepository.findAll().size();

        // Create the OnlineTraining
        restOnlineTrainingMockMvc.perform(post("/api/online-trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onlineTraining)))
            .andExpect(status().isCreated());

        // Validate the OnlineTraining in the database
        List<OnlineTraining> onlineTrainingList = onlineTrainingRepository.findAll();
        assertThat(onlineTrainingList).hasSize(databaseSizeBeforeCreate + 1);
        OnlineTraining testOnlineTraining = onlineTrainingList.get(onlineTrainingList.size() - 1);
        assertThat(testOnlineTraining.getTrainingLink()).isEqualTo(DEFAULT_TRAINING_LINK);
        assertThat(testOnlineTraining.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the OnlineTraining in Elasticsearch
        verify(mockOnlineTrainingSearchRepository, times(1)).save(testOnlineTraining);
    }

    @Test
    @Transactional
    public void createOnlineTrainingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = onlineTrainingRepository.findAll().size();

        // Create the OnlineTraining with an existing ID
        onlineTraining.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOnlineTrainingMockMvc.perform(post("/api/online-trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onlineTraining)))
            .andExpect(status().isBadRequest());

        // Validate the OnlineTraining in the database
        List<OnlineTraining> onlineTrainingList = onlineTrainingRepository.findAll();
        assertThat(onlineTrainingList).hasSize(databaseSizeBeforeCreate);

        // Validate the OnlineTraining in Elasticsearch
        verify(mockOnlineTrainingSearchRepository, times(0)).save(onlineTraining);
    }

    @Test
    @Transactional
    public void getAllOnlineTrainings() throws Exception {
        // Initialize the database
        onlineTrainingRepository.saveAndFlush(onlineTraining);

        // Get all the onlineTrainingList
        restOnlineTrainingMockMvc.perform(get("/api/online-trainings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(onlineTraining.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingLink").value(hasItem(DEFAULT_TRAINING_LINK.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    
    @Test
    @Transactional
    public void getOnlineTraining() throws Exception {
        // Initialize the database
        onlineTrainingRepository.saveAndFlush(onlineTraining);

        // Get the onlineTraining
        restOnlineTrainingMockMvc.perform(get("/api/online-trainings/{id}", onlineTraining.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(onlineTraining.getId().intValue()))
            .andExpect(jsonPath("$.trainingLink").value(DEFAULT_TRAINING_LINK.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOnlineTraining() throws Exception {
        // Get the onlineTraining
        restOnlineTrainingMockMvc.perform(get("/api/online-trainings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOnlineTraining() throws Exception {
        // Initialize the database
        onlineTrainingRepository.saveAndFlush(onlineTraining);

        int databaseSizeBeforeUpdate = onlineTrainingRepository.findAll().size();

        // Update the onlineTraining
        OnlineTraining updatedOnlineTraining = onlineTrainingRepository.findById(onlineTraining.getId()).get();
        // Disconnect from session so that the updates on updatedOnlineTraining are not directly saved in db
        em.detach(updatedOnlineTraining);
        updatedOnlineTraining
            .trainingLink(UPDATED_TRAINING_LINK)
            .desc(UPDATED_DESC);

        restOnlineTrainingMockMvc.perform(put("/api/online-trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOnlineTraining)))
            .andExpect(status().isOk());

        // Validate the OnlineTraining in the database
        List<OnlineTraining> onlineTrainingList = onlineTrainingRepository.findAll();
        assertThat(onlineTrainingList).hasSize(databaseSizeBeforeUpdate);
        OnlineTraining testOnlineTraining = onlineTrainingList.get(onlineTrainingList.size() - 1);
        assertThat(testOnlineTraining.getTrainingLink()).isEqualTo(UPDATED_TRAINING_LINK);
        assertThat(testOnlineTraining.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the OnlineTraining in Elasticsearch
        verify(mockOnlineTrainingSearchRepository, times(1)).save(testOnlineTraining);
    }

    @Test
    @Transactional
    public void updateNonExistingOnlineTraining() throws Exception {
        int databaseSizeBeforeUpdate = onlineTrainingRepository.findAll().size();

        // Create the OnlineTraining

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOnlineTrainingMockMvc.perform(put("/api/online-trainings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onlineTraining)))
            .andExpect(status().isBadRequest());

        // Validate the OnlineTraining in the database
        List<OnlineTraining> onlineTrainingList = onlineTrainingRepository.findAll();
        assertThat(onlineTrainingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OnlineTraining in Elasticsearch
        verify(mockOnlineTrainingSearchRepository, times(0)).save(onlineTraining);
    }

    @Test
    @Transactional
    public void deleteOnlineTraining() throws Exception {
        // Initialize the database
        onlineTrainingRepository.saveAndFlush(onlineTraining);

        int databaseSizeBeforeDelete = onlineTrainingRepository.findAll().size();

        // Get the onlineTraining
        restOnlineTrainingMockMvc.perform(delete("/api/online-trainings/{id}", onlineTraining.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OnlineTraining> onlineTrainingList = onlineTrainingRepository.findAll();
        assertThat(onlineTrainingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OnlineTraining in Elasticsearch
        verify(mockOnlineTrainingSearchRepository, times(1)).deleteById(onlineTraining.getId());
    }

    @Test
    @Transactional
    public void searchOnlineTraining() throws Exception {
        // Initialize the database
        onlineTrainingRepository.saveAndFlush(onlineTraining);
        when(mockOnlineTrainingSearchRepository.search(queryStringQuery("id:" + onlineTraining.getId())))
            .thenReturn(Collections.singletonList(onlineTraining));
        // Search the onlineTraining
        restOnlineTrainingMockMvc.perform(get("/api/_search/online-trainings?query=id:" + onlineTraining.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(onlineTraining.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingLink").value(hasItem(DEFAULT_TRAINING_LINK.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OnlineTraining.class);
        OnlineTraining onlineTraining1 = new OnlineTraining();
        onlineTraining1.setId(1L);
        OnlineTraining onlineTraining2 = new OnlineTraining();
        onlineTraining2.setId(onlineTraining1.getId());
        assertThat(onlineTraining1).isEqualTo(onlineTraining2);
        onlineTraining2.setId(2L);
        assertThat(onlineTraining1).isNotEqualTo(onlineTraining2);
        onlineTraining1.setId(null);
        assertThat(onlineTraining1).isNotEqualTo(onlineTraining2);
    }
}

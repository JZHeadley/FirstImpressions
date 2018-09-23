package com.jzheadley.firstimpressions.web.rest;

import com.jzheadley.firstimpressions.FirstImpressionsApp;

import com.jzheadley.firstimpressions.domain.Program;
import com.jzheadley.firstimpressions.repository.ProgramRepository;
import com.jzheadley.firstimpressions.repository.search.ProgramSearchRepository;
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
 * Test class for the ProgramResource REST controller.
 *
 * @see ProgramResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FirstImpressionsApp.class)
public class ProgramResourceIntTest {

    private static final String DEFAULT_PROGRAM_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAM_LINK = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAM_DESC = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAM_STATE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_STATE = "BBBBBBBBBB";

    @Autowired
    private ProgramRepository programRepository;

    /**
     * This repository is mocked in the com.jzheadley.firstimpressions.repository.search test package.
     *
     * @see com.jzheadley.firstimpressions.repository.search.ProgramSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProgramSearchRepository mockProgramSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgramMockMvc;

    private Program program;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramResource programResource = new ProgramResource(programRepository, mockProgramSearchRepository);
        this.restProgramMockMvc = MockMvcBuilders.standaloneSetup(programResource)
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
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .programTitle(DEFAULT_PROGRAM_TITLE)
            .programLink(DEFAULT_PROGRAM_LINK)
            .programDesc(DEFAULT_PROGRAM_DESC)
            .programState(DEFAULT_PROGRAM_STATE);
        return program;
    }

    @Before
    public void initTest() {
        program = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramTitle()).isEqualTo(DEFAULT_PROGRAM_TITLE);
        assertThat(testProgram.getProgramLink()).isEqualTo(DEFAULT_PROGRAM_LINK);
        assertThat(testProgram.getProgramDesc()).isEqualTo(DEFAULT_PROGRAM_DESC);
        assertThat(testProgram.getProgramState()).isEqualTo(DEFAULT_PROGRAM_STATE);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(1)).save(testProgram);
    }

    @Test
    @Transactional
    public void createProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program with an existing ID
        program.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(0)).save(program);
    }

    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].programTitle").value(hasItem(DEFAULT_PROGRAM_TITLE.toString())))
            .andExpect(jsonPath("$.[*].programLink").value(hasItem(DEFAULT_PROGRAM_LINK.toString())))
            .andExpect(jsonPath("$.[*].programDesc").value(hasItem(DEFAULT_PROGRAM_DESC.toString())))
            .andExpect(jsonPath("$.[*].programState").value(hasItem(DEFAULT_PROGRAM_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.programTitle").value(DEFAULT_PROGRAM_TITLE.toString()))
            .andExpect(jsonPath("$.programLink").value(DEFAULT_PROGRAM_LINK.toString()))
            .andExpect(jsonPath("$.programDesc").value(DEFAULT_PROGRAM_DESC.toString()))
            .andExpect(jsonPath("$.programState").value(DEFAULT_PROGRAM_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).get();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .programTitle(UPDATED_PROGRAM_TITLE)
            .programLink(UPDATED_PROGRAM_LINK)
            .programDesc(UPDATED_PROGRAM_DESC)
            .programState(UPDATED_PROGRAM_STATE);

        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgram)))
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramTitle()).isEqualTo(UPDATED_PROGRAM_TITLE);
        assertThat(testProgram.getProgramLink()).isEqualTo(UPDATED_PROGRAM_LINK);
        assertThat(testProgram.getProgramDesc()).isEqualTo(UPDATED_PROGRAM_DESC);
        assertThat(testProgram.getProgramState()).isEqualTo(UPDATED_PROGRAM_STATE);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(1)).save(testProgram);
    }

    @Test
    @Transactional
    public void updateNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Create the Program

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(0)).save(program);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Get the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(1)).deleteById(program.getId());
    }

    @Test
    @Transactional
    public void searchProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);
        when(mockProgramSearchRepository.search(queryStringQuery("id:" + program.getId())))
            .thenReturn(Collections.singletonList(program));
        // Search the program
        restProgramMockMvc.perform(get("/api/_search/programs?query=id:" + program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].programTitle").value(hasItem(DEFAULT_PROGRAM_TITLE.toString())))
            .andExpect(jsonPath("$.[*].programLink").value(hasItem(DEFAULT_PROGRAM_LINK.toString())))
            .andExpect(jsonPath("$.[*].programDesc").value(hasItem(DEFAULT_PROGRAM_DESC.toString())))
            .andExpect(jsonPath("$.[*].programState").value(hasItem(DEFAULT_PROGRAM_STATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Program.class);
        Program program1 = new Program();
        program1.setId(1L);
        Program program2 = new Program();
        program2.setId(program1.getId());
        assertThat(program1).isEqualTo(program2);
        program2.setId(2L);
        assertThat(program1).isNotEqualTo(program2);
        program1.setId(null);
        assertThat(program1).isNotEqualTo(program2);
    }
}

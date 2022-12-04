package com.billing.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.billing.IntegrationTest;
import com.billing.domain.WorkoutStep;
import com.billing.repository.WorkoutStepRepository;
import com.billing.service.dto.WorkoutStepDTO;
import com.billing.service.mapper.WorkoutStepMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkoutStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkoutStepResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STEP_NUMBER = 1;
    private static final Integer UPDATED_STEP_NUMBER = 2;

    private static final String ENTITY_API_URL = "/api/workout-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutStepRepository workoutStepRepository;

    @Autowired
    private WorkoutStepMapper workoutStepMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutStepMockMvc;

    private WorkoutStep workoutStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutStep createEntity(EntityManager em) {
        WorkoutStep workoutStep = new WorkoutStep().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).stepNumber(DEFAULT_STEP_NUMBER);
        return workoutStep;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutStep createUpdatedEntity(EntityManager em) {
        WorkoutStep workoutStep = new WorkoutStep().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).stepNumber(UPDATED_STEP_NUMBER);
        return workoutStep;
    }

    @BeforeEach
    public void initTest() {
        workoutStep = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutStep() throws Exception {
        int databaseSizeBeforeCreate = workoutStepRepository.findAll().size();
        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);
        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(DEFAULT_STEP_NUMBER);
    }

    @Test
    @Transactional
    void createWorkoutStepWithExistingId() throws Exception {
        // Create the WorkoutStep with an existing ID
        workoutStep.setId(1L);
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        int databaseSizeBeforeCreate = workoutStepRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setTitle(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setDescription(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStepNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setStepNumber(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkoutSteps() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)));
    }

    @Test
    @Transactional
    void getWorkoutStep() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get the workoutStep
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutStep.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.stepNumber").value(DEFAULT_STEP_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutStep() throws Exception {
        // Get the workoutStep
        restWorkoutStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkoutStep() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();

        // Update the workoutStep
        WorkoutStep updatedWorkoutStep = workoutStepRepository.findById(workoutStep.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutStep are not directly saved in db
        em.detach(updatedWorkoutStep);
        updatedWorkoutStep.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).stepNumber(UPDATED_STEP_NUMBER);
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(updatedWorkoutStep);

        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(UPDATED_STEP_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutStepWithPatch() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();

        // Update the workoutStep using partial update
        WorkoutStep partialUpdatedWorkoutStep = new WorkoutStep();
        partialUpdatedWorkoutStep.setId(workoutStep.getId());

        partialUpdatedWorkoutStep.description(UPDATED_DESCRIPTION);

        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(DEFAULT_STEP_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutStepWithPatch() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();

        // Update the workoutStep using partial update
        WorkoutStep partialUpdatedWorkoutStep = new WorkoutStep();
        partialUpdatedWorkoutStep.setId(workoutStep.getId());

        partialUpdatedWorkoutStep.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).stepNumber(UPDATED_STEP_NUMBER);

        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(UPDATED_STEP_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutStepDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkoutStep() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeDelete = workoutStepRepository.findAll().size();

        // Delete the workoutStep
        restWorkoutStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutStep.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

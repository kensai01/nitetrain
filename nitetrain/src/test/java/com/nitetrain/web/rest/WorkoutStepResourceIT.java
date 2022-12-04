package com.nitetrain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.nitetrain.IntegrationTest;
import com.nitetrain.domain.WorkoutStep;
import com.nitetrain.repository.EntityManager;
import com.nitetrain.repository.WorkoutStepRepository;
import com.nitetrain.service.dto.WorkoutStepDTO;
import com.nitetrain.service.mapper.WorkoutStepMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link WorkoutStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(WorkoutStep.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        workoutStep = createEntity(em);
    }

    @Test
    void createWorkoutStep() throws Exception {
        int databaseSizeBeforeCreate = workoutStepRepository.findAll().collectList().block().size();
        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(DEFAULT_STEP_NUMBER);
    }

    @Test
    void createWorkoutStepWithExistingId() throws Exception {
        // Create the WorkoutStep with an existing ID
        workoutStep.setId(1L);
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        int databaseSizeBeforeCreate = workoutStepRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().collectList().block().size();
        // set the field null
        workoutStep.setTitle(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().collectList().block().size();
        // set the field null
        workoutStep.setDescription(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStepNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().collectList().block().size();
        // set the field null
        workoutStep.setStepNumber(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllWorkoutSteps() {
        // Initialize the database
        workoutStepRepository.save(workoutStep).block();

        // Get all the workoutStepList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(workoutStep.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].stepNumber")
            .value(hasItem(DEFAULT_STEP_NUMBER));
    }

    @Test
    void getWorkoutStep() {
        // Initialize the database
        workoutStepRepository.save(workoutStep).block();

        // Get the workoutStep
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, workoutStep.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(workoutStep.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.stepNumber")
            .value(is(DEFAULT_STEP_NUMBER));
    }

    @Test
    void getNonExistingWorkoutStep() {
        // Get the workoutStep
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWorkoutStep() throws Exception {
        // Initialize the database
        workoutStepRepository.save(workoutStep).block();

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();

        // Update the workoutStep
        WorkoutStep updatedWorkoutStep = workoutStepRepository.findById(workoutStep.getId()).block();
        updatedWorkoutStep.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).stepNumber(UPDATED_STEP_NUMBER);
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(updatedWorkoutStep);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workoutStepDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(UPDATED_STEP_NUMBER);
    }

    @Test
    void putNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workoutStepDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWorkoutStepWithPatch() throws Exception {
        // Initialize the database
        workoutStepRepository.save(workoutStep).block();

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();

        // Update the workoutStep using partial update
        WorkoutStep partialUpdatedWorkoutStep = new WorkoutStep();
        partialUpdatedWorkoutStep.setId(workoutStep.getId());

        partialUpdatedWorkoutStep.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkoutStep.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutStep))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(DEFAULT_STEP_NUMBER);
    }

    @Test
    void fullUpdateWorkoutStepWithPatch() throws Exception {
        // Initialize the database
        workoutStepRepository.save(workoutStep).block();

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();

        // Update the workoutStep using partial update
        WorkoutStep partialUpdatedWorkoutStep = new WorkoutStep();
        partialUpdatedWorkoutStep.setId(workoutStep.getId());

        partialUpdatedWorkoutStep.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).stepNumber(UPDATED_STEP_NUMBER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkoutStep.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutStep))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkoutStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutStep.getStepNumber()).isEqualTo(UPDATED_STEP_NUMBER);
    }

    @Test
    void patchNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, workoutStepDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().collectList().block().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWorkoutStep() {
        // Initialize the database
        workoutStepRepository.save(workoutStep).block();

        int databaseSizeBeforeDelete = workoutStepRepository.findAll().collectList().block().size();

        // Delete the workoutStep
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, workoutStep.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll().collectList().block();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

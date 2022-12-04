package com.nitetrain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.nitetrain.IntegrationTest;
import com.nitetrain.domain.Workout;
import com.nitetrain.repository.EntityManager;
import com.nitetrain.repository.WorkoutRepository;
import com.nitetrain.service.dto.WorkoutDTO;
import com.nitetrain.service.mapper.WorkoutMapper;
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
 * Integration tests for the {@link WorkoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WorkoutResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME = 0;
    private static final Integer UPDATED_TIME = 1;

    private static final String DEFAULT_VIDEO_ID = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SCALING = "AAAAAAAAAA";
    private static final String UPDATED_SCALING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutMapper workoutMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Workout workout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workout createEntity(EntityManager em) {
        Workout workout = new Workout()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .time(DEFAULT_TIME)
            .videoId(DEFAULT_VIDEO_ID)
            .scaling(DEFAULT_SCALING);
        return workout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workout createUpdatedEntity(EntityManager em) {
        Workout workout = new Workout()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .videoId(UPDATED_VIDEO_ID)
            .scaling(UPDATED_SCALING);
        return workout;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Workout.class).block();
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
        workout = createEntity(em);
    }

    @Test
    void createWorkout() throws Exception {
        int databaseSizeBeforeCreate = workoutRepository.findAll().collectList().block().size();
        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate + 1);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(DEFAULT_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(DEFAULT_SCALING);
    }

    @Test
    void createWorkoutWithExistingId() throws Exception {
        // Create the Workout with an existing ID
        workout.setId(1L);
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        int databaseSizeBeforeCreate = workoutRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutRepository.findAll().collectList().block().size();
        // set the field null
        workout.setTitle(null);

        // Create the Workout, which fails.
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutRepository.findAll().collectList().block().size();
        // set the field null
        workout.setDescription(null);

        // Create the Workout, which fails.
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllWorkoutsAsStream() {
        // Initialize the database
        workoutRepository.save(workout).block();

        List<Workout> workoutList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(WorkoutDTO.class)
            .getResponseBody()
            .map(workoutMapper::toEntity)
            .filter(workout::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(workoutList).isNotNull();
        assertThat(workoutList).hasSize(1);
        Workout testWorkout = workoutList.get(0);
        assertThat(testWorkout.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(DEFAULT_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(DEFAULT_SCALING);
    }

    @Test
    void getAllWorkouts() {
        // Initialize the database
        workoutRepository.save(workout).block();

        // Get all the workoutList
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
            .value(hasItem(workout.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].time")
            .value(hasItem(DEFAULT_TIME))
            .jsonPath("$.[*].videoId")
            .value(hasItem(DEFAULT_VIDEO_ID))
            .jsonPath("$.[*].scaling")
            .value(hasItem(DEFAULT_SCALING));
    }

    @Test
    void getWorkout() {
        // Initialize the database
        workoutRepository.save(workout).block();

        // Get the workout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, workout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(workout.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.time")
            .value(is(DEFAULT_TIME))
            .jsonPath("$.videoId")
            .value(is(DEFAULT_VIDEO_ID))
            .jsonPath("$.scaling")
            .value(is(DEFAULT_SCALING));
    }

    @Test
    void getNonExistingWorkout() {
        // Get the workout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWorkout() throws Exception {
        // Initialize the database
        workoutRepository.save(workout).block();

        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();

        // Update the workout
        Workout updatedWorkout = workoutRepository.findById(workout.getId()).block();
        updatedWorkout
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .videoId(UPDATED_VIDEO_ID)
            .scaling(UPDATED_SCALING);
        WorkoutDTO workoutDTO = workoutMapper.toDto(updatedWorkout);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(UPDATED_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(UPDATED_SCALING);
    }

    @Test
    void putNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.save(workout).block();

        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout.description(UPDATED_DESCRIPTION).time(UPDATED_TIME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(DEFAULT_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(DEFAULT_SCALING);
    }

    @Test
    void fullUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.save(workout).block();

        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .videoId(UPDATED_VIDEO_ID)
            .scaling(UPDATED_SCALING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(UPDATED_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(UPDATED_SCALING);
    }

    @Test
    void patchNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, workoutDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().collectList().block().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWorkout() {
        // Initialize the database
        workoutRepository.save(workout).block();

        int databaseSizeBeforeDelete = workoutRepository.findAll().collectList().block().size();

        // Delete the workout
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, workout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Workout> workoutList = workoutRepository.findAll().collectList().block();
        assertThat(workoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

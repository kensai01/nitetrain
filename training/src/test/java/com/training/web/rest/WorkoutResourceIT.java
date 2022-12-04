package com.training.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.training.IntegrationTest;
import com.training.domain.Workout;
import com.training.repository.WorkoutRepository;
import com.training.service.dto.WorkoutDTO;
import com.training.service.mapper.WorkoutMapper;
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
 * Integration tests for the {@link WorkoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restWorkoutMockMvc;

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

    @BeforeEach
    public void initTest() {
        workout = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkout() throws Exception {
        int databaseSizeBeforeCreate = workoutRepository.findAll().size();
        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);
        restWorkoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutDTO)))
            .andExpect(status().isCreated());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate + 1);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(DEFAULT_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(DEFAULT_SCALING);
    }

    @Test
    @Transactional
    void createWorkoutWithExistingId() throws Exception {
        // Create the Workout with an existing ID
        workout.setId(1L);
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        int databaseSizeBeforeCreate = workoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutRepository.findAll().size();
        // set the field null
        workout.setTitle(null);

        // Create the Workout, which fails.
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        restWorkoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutDTO)))
            .andExpect(status().isBadRequest());

        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutRepository.findAll().size();
        // set the field null
        workout.setDescription(null);

        // Create the Workout, which fails.
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        restWorkoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutDTO)))
            .andExpect(status().isBadRequest());

        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkouts() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workout.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
            .andExpect(jsonPath("$.[*].videoId").value(hasItem(DEFAULT_VIDEO_ID)))
            .andExpect(jsonPath("$.[*].scaling").value(hasItem(DEFAULT_SCALING)));
    }

    @Test
    @Transactional
    void getWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get the workout
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL_ID, workout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workout.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME))
            .andExpect(jsonPath("$.videoId").value(DEFAULT_VIDEO_ID))
            .andExpect(jsonPath("$.scaling").value(DEFAULT_SCALING));
    }

    @Test
    @Transactional
    void getNonExistingWorkout() throws Exception {
        // Get the workout
        restWorkoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout
        Workout updatedWorkout = workoutRepository.findById(workout.getId()).get();
        // Disconnect from session so that the updates on updatedWorkout are not directly saved in db
        em.detach(updatedWorkout);
        updatedWorkout
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .videoId(UPDATED_VIDEO_ID)
            .scaling(UPDATED_SCALING);
        WorkoutDTO workoutDTO = workoutMapper.toDto(updatedWorkout);

        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(UPDATED_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(UPDATED_SCALING);
    }

    @Test
    @Transactional
    void putNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout.description(UPDATED_DESCRIPTION).time(UPDATED_TIME);

        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(DEFAULT_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(DEFAULT_SCALING);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .videoId(UPDATED_VIDEO_ID)
            .scaling(UPDATED_SCALING);

        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkout.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWorkout.getVideoId()).isEqualTo(UPDATED_VIDEO_ID);
        assertThat(testWorkout.getScaling()).isEqualTo(UPDATED_SCALING);
    }

    @Test
    @Transactional
    void patchNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // Create the Workout
        WorkoutDTO workoutDTO = workoutMapper.toDto(workout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workoutDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeDelete = workoutRepository.findAll().size();

        // Delete the workout
        restWorkoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, workout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

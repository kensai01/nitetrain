package com.training.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.training.IntegrationTest;
import com.training.domain.BeginnerWorkout;
import com.training.repository.BeginnerWorkoutRepository;
import com.training.service.dto.BeginnerWorkoutDTO;
import com.training.service.mapper.BeginnerWorkoutMapper;
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
 * Integration tests for the {@link BeginnerWorkoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BeginnerWorkoutResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/beginner-workouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BeginnerWorkoutRepository beginnerWorkoutRepository;

    @Autowired
    private BeginnerWorkoutMapper beginnerWorkoutMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeginnerWorkoutMockMvc;

    private BeginnerWorkout beginnerWorkout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeginnerWorkout createEntity(EntityManager em) {
        BeginnerWorkout beginnerWorkout = new BeginnerWorkout().description(DEFAULT_DESCRIPTION);
        return beginnerWorkout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeginnerWorkout createUpdatedEntity(EntityManager em) {
        BeginnerWorkout beginnerWorkout = new BeginnerWorkout().description(UPDATED_DESCRIPTION);
        return beginnerWorkout;
    }

    @BeforeEach
    public void initTest() {
        beginnerWorkout = createEntity(em);
    }

    @Test
    @Transactional
    void createBeginnerWorkout() throws Exception {
        int databaseSizeBeforeCreate = beginnerWorkoutRepository.findAll().size();
        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);
        restBeginnerWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeCreate + 1);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBeginnerWorkoutWithExistingId() throws Exception {
        // Create the BeginnerWorkout with an existing ID
        beginnerWorkout.setId(1L);
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        int databaseSizeBeforeCreate = beginnerWorkoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeginnerWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = beginnerWorkoutRepository.findAll().size();
        // set the field null
        beginnerWorkout.setDescription(null);

        // Create the BeginnerWorkout, which fails.
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        restBeginnerWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBeginnerWorkouts() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.saveAndFlush(beginnerWorkout);

        // Get all the beginnerWorkoutList
        restBeginnerWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beginnerWorkout.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBeginnerWorkout() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.saveAndFlush(beginnerWorkout);

        // Get the beginnerWorkout
        restBeginnerWorkoutMockMvc
            .perform(get(ENTITY_API_URL_ID, beginnerWorkout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beginnerWorkout.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBeginnerWorkout() throws Exception {
        // Get the beginnerWorkout
        restBeginnerWorkoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBeginnerWorkout() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.saveAndFlush(beginnerWorkout);

        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();

        // Update the beginnerWorkout
        BeginnerWorkout updatedBeginnerWorkout = beginnerWorkoutRepository.findById(beginnerWorkout.getId()).get();
        // Disconnect from session so that the updates on updatedBeginnerWorkout are not directly saved in db
        em.detach(updatedBeginnerWorkout);
        updatedBeginnerWorkout.description(UPDATED_DESCRIPTION);
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(updatedBeginnerWorkout);

        restBeginnerWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beginnerWorkoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeginnerWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beginnerWorkoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeginnerWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeginnerWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeginnerWorkoutWithPatch() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.saveAndFlush(beginnerWorkout);

        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();

        // Update the beginnerWorkout using partial update
        BeginnerWorkout partialUpdatedBeginnerWorkout = new BeginnerWorkout();
        partialUpdatedBeginnerWorkout.setId(beginnerWorkout.getId());

        partialUpdatedBeginnerWorkout.description(UPDATED_DESCRIPTION);

        restBeginnerWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeginnerWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeginnerWorkout))
            )
            .andExpect(status().isOk());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBeginnerWorkoutWithPatch() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.saveAndFlush(beginnerWorkout);

        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();

        // Update the beginnerWorkout using partial update
        BeginnerWorkout partialUpdatedBeginnerWorkout = new BeginnerWorkout();
        partialUpdatedBeginnerWorkout.setId(beginnerWorkout.getId());

        partialUpdatedBeginnerWorkout.description(UPDATED_DESCRIPTION);

        restBeginnerWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeginnerWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeginnerWorkout))
            )
            .andExpect(status().isOk());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeginnerWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beginnerWorkoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeginnerWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeginnerWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeginnerWorkout() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.saveAndFlush(beginnerWorkout);

        int databaseSizeBeforeDelete = beginnerWorkoutRepository.findAll().size();

        // Delete the beginnerWorkout
        restBeginnerWorkoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, beginnerWorkout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

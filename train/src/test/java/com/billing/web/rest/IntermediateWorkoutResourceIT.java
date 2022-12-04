package com.billing.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.billing.IntegrationTest;
import com.billing.domain.IntermediateWorkout;
import com.billing.repository.IntermediateWorkoutRepository;
import com.billing.service.dto.IntermediateWorkoutDTO;
import com.billing.service.mapper.IntermediateWorkoutMapper;
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
 * Integration tests for the {@link IntermediateWorkoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntermediateWorkoutResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/intermediate-workouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntermediateWorkoutRepository intermediateWorkoutRepository;

    @Autowired
    private IntermediateWorkoutMapper intermediateWorkoutMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntermediateWorkoutMockMvc;

    private IntermediateWorkout intermediateWorkout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntermediateWorkout createEntity(EntityManager em) {
        IntermediateWorkout intermediateWorkout = new IntermediateWorkout().description(DEFAULT_DESCRIPTION);
        return intermediateWorkout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntermediateWorkout createUpdatedEntity(EntityManager em) {
        IntermediateWorkout intermediateWorkout = new IntermediateWorkout().description(UPDATED_DESCRIPTION);
        return intermediateWorkout;
    }

    @BeforeEach
    public void initTest() {
        intermediateWorkout = createEntity(em);
    }

    @Test
    @Transactional
    void createIntermediateWorkout() throws Exception {
        int databaseSizeBeforeCreate = intermediateWorkoutRepository.findAll().size();
        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);
        restIntermediateWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeCreate + 1);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createIntermediateWorkoutWithExistingId() throws Exception {
        // Create the IntermediateWorkout with an existing ID
        intermediateWorkout.setId(1L);
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        int databaseSizeBeforeCreate = intermediateWorkoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntermediateWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = intermediateWorkoutRepository.findAll().size();
        // set the field null
        intermediateWorkout.setDescription(null);

        // Create the IntermediateWorkout, which fails.
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        restIntermediateWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIntermediateWorkouts() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.saveAndFlush(intermediateWorkout);

        // Get all the intermediateWorkoutList
        restIntermediateWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intermediateWorkout.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getIntermediateWorkout() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.saveAndFlush(intermediateWorkout);

        // Get the intermediateWorkout
        restIntermediateWorkoutMockMvc
            .perform(get(ENTITY_API_URL_ID, intermediateWorkout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intermediateWorkout.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingIntermediateWorkout() throws Exception {
        // Get the intermediateWorkout
        restIntermediateWorkoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntermediateWorkout() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.saveAndFlush(intermediateWorkout);

        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();

        // Update the intermediateWorkout
        IntermediateWorkout updatedIntermediateWorkout = intermediateWorkoutRepository.findById(intermediateWorkout.getId()).get();
        // Disconnect from session so that the updates on updatedIntermediateWorkout are not directly saved in db
        em.detach(updatedIntermediateWorkout);
        updatedIntermediateWorkout.description(UPDATED_DESCRIPTION);
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(updatedIntermediateWorkout);

        restIntermediateWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intermediateWorkoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntermediateWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intermediateWorkoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntermediateWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntermediateWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntermediateWorkoutWithPatch() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.saveAndFlush(intermediateWorkout);

        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();

        // Update the intermediateWorkout using partial update
        IntermediateWorkout partialUpdatedIntermediateWorkout = new IntermediateWorkout();
        partialUpdatedIntermediateWorkout.setId(intermediateWorkout.getId());

        partialUpdatedIntermediateWorkout.description(UPDATED_DESCRIPTION);

        restIntermediateWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntermediateWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntermediateWorkout))
            )
            .andExpect(status().isOk());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateIntermediateWorkoutWithPatch() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.saveAndFlush(intermediateWorkout);

        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();

        // Update the intermediateWorkout using partial update
        IntermediateWorkout partialUpdatedIntermediateWorkout = new IntermediateWorkout();
        partialUpdatedIntermediateWorkout.setId(intermediateWorkout.getId());

        partialUpdatedIntermediateWorkout.description(UPDATED_DESCRIPTION);

        restIntermediateWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntermediateWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntermediateWorkout))
            )
            .andExpect(status().isOk());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntermediateWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intermediateWorkoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntermediateWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntermediateWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntermediateWorkout() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.saveAndFlush(intermediateWorkout);

        int databaseSizeBeforeDelete = intermediateWorkoutRepository.findAll().size();

        // Delete the intermediateWorkout
        restIntermediateWorkoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, intermediateWorkout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

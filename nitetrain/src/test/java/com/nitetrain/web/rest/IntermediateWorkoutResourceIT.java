package com.nitetrain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.nitetrain.IntegrationTest;
import com.nitetrain.domain.IntermediateWorkout;
import com.nitetrain.repository.EntityManager;
import com.nitetrain.repository.IntermediateWorkoutRepository;
import com.nitetrain.service.dto.IntermediateWorkoutDTO;
import com.nitetrain.service.mapper.IntermediateWorkoutMapper;
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
 * Integration tests for the {@link IntermediateWorkoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IntermediateWorkout.class).block();
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
        intermediateWorkout = createEntity(em);
    }

    @Test
    void createIntermediateWorkout() throws Exception {
        int databaseSizeBeforeCreate = intermediateWorkoutRepository.findAll().collectList().block().size();
        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeCreate + 1);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createIntermediateWorkoutWithExistingId() throws Exception {
        // Create the IntermediateWorkout with an existing ID
        intermediateWorkout.setId(1L);
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        int databaseSizeBeforeCreate = intermediateWorkoutRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = intermediateWorkoutRepository.findAll().collectList().block().size();
        // set the field null
        intermediateWorkout.setDescription(null);

        // Create the IntermediateWorkout, which fails.
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIntermediateWorkouts() {
        // Initialize the database
        intermediateWorkoutRepository.save(intermediateWorkout).block();

        // Get all the intermediateWorkoutList
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
            .value(hasItem(intermediateWorkout.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getIntermediateWorkout() {
        // Initialize the database
        intermediateWorkoutRepository.save(intermediateWorkout).block();

        // Get the intermediateWorkout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, intermediateWorkout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(intermediateWorkout.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingIntermediateWorkout() {
        // Get the intermediateWorkout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIntermediateWorkout() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.save(intermediateWorkout).block();

        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();

        // Update the intermediateWorkout
        IntermediateWorkout updatedIntermediateWorkout = intermediateWorkoutRepository.findById(intermediateWorkout.getId()).block();
        updatedIntermediateWorkout.description(UPDATED_DESCRIPTION);
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(updatedIntermediateWorkout);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, intermediateWorkoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, intermediateWorkoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIntermediateWorkoutWithPatch() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.save(intermediateWorkout).block();

        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();

        // Update the intermediateWorkout using partial update
        IntermediateWorkout partialUpdatedIntermediateWorkout = new IntermediateWorkout();
        partialUpdatedIntermediateWorkout.setId(intermediateWorkout.getId());

        partialUpdatedIntermediateWorkout.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntermediateWorkout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntermediateWorkout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateIntermediateWorkoutWithPatch() throws Exception {
        // Initialize the database
        intermediateWorkoutRepository.save(intermediateWorkout).block();

        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();

        // Update the intermediateWorkout using partial update
        IntermediateWorkout partialUpdatedIntermediateWorkout = new IntermediateWorkout();
        partialUpdatedIntermediateWorkout.setId(intermediateWorkout.getId());

        partialUpdatedIntermediateWorkout.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntermediateWorkout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntermediateWorkout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
        IntermediateWorkout testIntermediateWorkout = intermediateWorkoutList.get(intermediateWorkoutList.size() - 1);
        assertThat(testIntermediateWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, intermediateWorkoutDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIntermediateWorkout() throws Exception {
        int databaseSizeBeforeUpdate = intermediateWorkoutRepository.findAll().collectList().block().size();
        intermediateWorkout.setId(count.incrementAndGet());

        // Create the IntermediateWorkout
        IntermediateWorkoutDTO intermediateWorkoutDTO = intermediateWorkoutMapper.toDto(intermediateWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(intermediateWorkoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntermediateWorkout in the database
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIntermediateWorkout() {
        // Initialize the database
        intermediateWorkoutRepository.save(intermediateWorkout).block();

        int databaseSizeBeforeDelete = intermediateWorkoutRepository.findAll().collectList().block().size();

        // Delete the intermediateWorkout
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, intermediateWorkout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IntermediateWorkout> intermediateWorkoutList = intermediateWorkoutRepository.findAll().collectList().block();
        assertThat(intermediateWorkoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

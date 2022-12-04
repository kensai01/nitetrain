package com.nitetrain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.nitetrain.IntegrationTest;
import com.nitetrain.domain.BeginnerWorkout;
import com.nitetrain.repository.BeginnerWorkoutRepository;
import com.nitetrain.repository.EntityManager;
import com.nitetrain.service.dto.BeginnerWorkoutDTO;
import com.nitetrain.service.mapper.BeginnerWorkoutMapper;
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
 * Integration tests for the {@link BeginnerWorkoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BeginnerWorkout.class).block();
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
        beginnerWorkout = createEntity(em);
    }

    @Test
    void createBeginnerWorkout() throws Exception {
        int databaseSizeBeforeCreate = beginnerWorkoutRepository.findAll().collectList().block().size();
        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeCreate + 1);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createBeginnerWorkoutWithExistingId() throws Exception {
        // Create the BeginnerWorkout with an existing ID
        beginnerWorkout.setId(1L);
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        int databaseSizeBeforeCreate = beginnerWorkoutRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = beginnerWorkoutRepository.findAll().collectList().block().size();
        // set the field null
        beginnerWorkout.setDescription(null);

        // Create the BeginnerWorkout, which fails.
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBeginnerWorkoutsAsStream() {
        // Initialize the database
        beginnerWorkoutRepository.save(beginnerWorkout).block();

        List<BeginnerWorkout> beginnerWorkoutList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(BeginnerWorkoutDTO.class)
            .getResponseBody()
            .map(beginnerWorkoutMapper::toEntity)
            .filter(beginnerWorkout::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(beginnerWorkoutList).isNotNull();
        assertThat(beginnerWorkoutList).hasSize(1);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(0);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void getAllBeginnerWorkouts() {
        // Initialize the database
        beginnerWorkoutRepository.save(beginnerWorkout).block();

        // Get all the beginnerWorkoutList
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
            .value(hasItem(beginnerWorkout.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getBeginnerWorkout() {
        // Initialize the database
        beginnerWorkoutRepository.save(beginnerWorkout).block();

        // Get the beginnerWorkout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, beginnerWorkout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(beginnerWorkout.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingBeginnerWorkout() {
        // Get the beginnerWorkout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBeginnerWorkout() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.save(beginnerWorkout).block();

        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();

        // Update the beginnerWorkout
        BeginnerWorkout updatedBeginnerWorkout = beginnerWorkoutRepository.findById(beginnerWorkout.getId()).block();
        updatedBeginnerWorkout.description(UPDATED_DESCRIPTION);
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(updatedBeginnerWorkout);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, beginnerWorkoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, beginnerWorkoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBeginnerWorkoutWithPatch() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.save(beginnerWorkout).block();

        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();

        // Update the beginnerWorkout using partial update
        BeginnerWorkout partialUpdatedBeginnerWorkout = new BeginnerWorkout();
        partialUpdatedBeginnerWorkout.setId(beginnerWorkout.getId());

        partialUpdatedBeginnerWorkout.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBeginnerWorkout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBeginnerWorkout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateBeginnerWorkoutWithPatch() throws Exception {
        // Initialize the database
        beginnerWorkoutRepository.save(beginnerWorkout).block();

        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();

        // Update the beginnerWorkout using partial update
        BeginnerWorkout partialUpdatedBeginnerWorkout = new BeginnerWorkout();
        partialUpdatedBeginnerWorkout.setId(beginnerWorkout.getId());

        partialUpdatedBeginnerWorkout.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBeginnerWorkout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBeginnerWorkout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
        BeginnerWorkout testBeginnerWorkout = beginnerWorkoutList.get(beginnerWorkoutList.size() - 1);
        assertThat(testBeginnerWorkout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, beginnerWorkoutDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBeginnerWorkout() throws Exception {
        int databaseSizeBeforeUpdate = beginnerWorkoutRepository.findAll().collectList().block().size();
        beginnerWorkout.setId(count.incrementAndGet());

        // Create the BeginnerWorkout
        BeginnerWorkoutDTO beginnerWorkoutDTO = beginnerWorkoutMapper.toDto(beginnerWorkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(beginnerWorkoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BeginnerWorkout in the database
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBeginnerWorkout() {
        // Initialize the database
        beginnerWorkoutRepository.save(beginnerWorkout).block();

        int databaseSizeBeforeDelete = beginnerWorkoutRepository.findAll().collectList().block().size();

        // Delete the beginnerWorkout
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, beginnerWorkout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<BeginnerWorkout> beginnerWorkoutList = beginnerWorkoutRepository.findAll().collectList().block();
        assertThat(beginnerWorkoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

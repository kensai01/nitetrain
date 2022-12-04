package com.nitetrain.web.rest;

import static com.nitetrain.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.nitetrain.IntegrationTest;
import com.nitetrain.domain.Price;
import com.nitetrain.repository.EntityManager;
import com.nitetrain.repository.PriceRepository;
import com.nitetrain.service.dto.PriceDTO;
import com.nitetrain.service.mapper.PriceMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PriceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Price price;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createEntity(EntityManager em) {
        Price price = new Price().name(DEFAULT_NAME).location(DEFAULT_LOCATION).description(DEFAULT_DESCRIPTION).cost(DEFAULT_COST);
        return price;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createUpdatedEntity(EntityManager em) {
        Price price = new Price().name(UPDATED_NAME).location(UPDATED_LOCATION).description(UPDATED_DESCRIPTION).cost(UPDATED_COST);
        return price;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Price.class).block();
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
        price = createEntity(em);
    }

    @Test
    void createPrice() throws Exception {
        int databaseSizeBeforeCreate = priceRepository.findAll().collectList().block().size();
        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate + 1);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrice.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPrice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrice.getCost()).isEqualByComparingTo(DEFAULT_COST);
    }

    @Test
    void createPriceWithExistingId() throws Exception {
        // Create the Price with an existing ID
        price.setId(1L);
        PriceDTO priceDTO = priceMapper.toDto(price);

        int databaseSizeBeforeCreate = priceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().collectList().block().size();
        // set the field null
        price.setName(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().collectList().block().size();
        // set the field null
        price.setLocation(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().collectList().block().size();
        // set the field null
        price.setDescription(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPrices() {
        // Initialize the database
        priceRepository.save(price).block();

        // Get all the priceList
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
            .value(hasItem(price.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].cost")
            .value(hasItem(sameNumber(DEFAULT_COST)));
    }

    @Test
    void getPrice() {
        // Initialize the database
        priceRepository.save(price).block();

        // Get the price
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, price.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(price.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.cost")
            .value(is(sameNumber(DEFAULT_COST)));
    }

    @Test
    void getNonExistingPrice() {
        // Get the price
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPrice() throws Exception {
        // Initialize the database
        priceRepository.save(price).block();

        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();

        // Update the price
        Price updatedPrice = priceRepository.findById(price.getId()).block();
        updatedPrice.name(UPDATED_NAME).location(UPDATED_LOCATION).description(UPDATED_DESCRIPTION).cost(UPDATED_COST);
        PriceDTO priceDTO = priceMapper.toDto(updatedPrice);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, priceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrice.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPrice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrice.getCost()).isEqualByComparingTo(UPDATED_COST);
    }

    @Test
    void putNonExistingPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, priceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.save(price).block();

        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPrice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrice.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPrice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrice.getCost()).isEqualByComparingTo(DEFAULT_COST);
    }

    @Test
    void fullUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.save(price).block();

        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        partialUpdatedPrice.name(UPDATED_NAME).location(UPDATED_LOCATION).description(UPDATED_DESCRIPTION).cost(UPDATED_COST);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPrice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrice.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPrice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrice.getCost()).isEqualByComparingTo(UPDATED_COST);
    }

    @Test
    void patchNonExistingPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, priceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().collectList().block().size();
        price.setId(count.incrementAndGet());

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(priceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePrice() {
        // Initialize the database
        priceRepository.save(price).block();

        int databaseSizeBeforeDelete = priceRepository.findAll().collectList().block().size();

        // Delete the price
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, price.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Price> priceList = priceRepository.findAll().collectList().block();
        assertThat(priceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

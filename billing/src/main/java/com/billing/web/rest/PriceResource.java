package com.billing.web.rest;

import com.billing.repository.PriceRepository;
import com.billing.service.PriceService;
import com.billing.service.dto.PriceDTO;
import com.billing.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.billing.domain.Price}.
 */
@RestController
@RequestMapping("/api")
public class PriceResource {

    private final Logger log = LoggerFactory.getLogger(PriceResource.class);

    private static final String ENTITY_NAME = "billingPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceService priceService;

    private final PriceRepository priceRepository;

    public PriceResource(PriceService priceService, PriceRepository priceRepository) {
        this.priceService = priceService;
        this.priceRepository = priceRepository;
    }

    /**
     * {@code POST  /prices} : Create a new price.
     *
     * @param priceDTO the priceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceDTO, or with status {@code 400 (Bad Request)} if the price has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prices")
    public ResponseEntity<PriceDTO> createPrice(@Valid @RequestBody PriceDTO priceDTO) throws URISyntaxException {
        log.debug("REST request to save Price : {}", priceDTO);
        if (priceDTO.getId() != null) {
            throw new BadRequestAlertException("A new price cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceDTO result = priceService.save(priceDTO);
        return ResponseEntity
            .created(new URI("/api/prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prices/:id} : Updates an existing price.
     *
     * @param id the id of the priceDTO to save.
     * @param priceDTO the priceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceDTO,
     * or with status {@code 400 (Bad Request)} if the priceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prices/{id}")
    public ResponseEntity<PriceDTO> updatePrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PriceDTO priceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Price : {}, {}", id, priceDTO);
        if (priceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PriceDTO result = priceService.update(priceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prices/:id} : Partial updates given fields of an existing price, field will ignore if it is null
     *
     * @param id the id of the priceDTO to save.
     * @param priceDTO the priceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceDTO,
     * or with status {@code 400 (Bad Request)} if the priceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the priceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the priceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PriceDTO> partialUpdatePrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PriceDTO priceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Price partially : {}, {}", id, priceDTO);
        if (priceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceDTO> result = priceService.partialUpdate(priceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prices} : get all the prices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prices in body.
     */
    @GetMapping("/prices")
    public ResponseEntity<List<PriceDTO>> getAllPrices(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Prices");
        Page<PriceDTO> page = priceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prices/:id} : get the "id" price.
     *
     * @param id the id of the priceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prices/{id}")
    public ResponseEntity<PriceDTO> getPrice(@PathVariable Long id) {
        log.debug("REST request to get Price : {}", id);
        Optional<PriceDTO> priceDTO = priceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceDTO);
    }

    /**
     * {@code DELETE  /prices/:id} : delete the "id" price.
     *
     * @param id the id of the priceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prices/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        log.debug("REST request to delete Price : {}", id);
        priceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

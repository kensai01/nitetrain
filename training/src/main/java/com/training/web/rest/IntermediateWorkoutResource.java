package com.training.web.rest;

import com.training.repository.IntermediateWorkoutRepository;
import com.training.service.IntermediateWorkoutService;
import com.training.service.dto.IntermediateWorkoutDTO;
import com.training.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.training.domain.IntermediateWorkout}.
 */
@RestController
@RequestMapping("/api")
public class IntermediateWorkoutResource {

    private final Logger log = LoggerFactory.getLogger(IntermediateWorkoutResource.class);

    private static final String ENTITY_NAME = "trainingIntermediateWorkout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntermediateWorkoutService intermediateWorkoutService;

    private final IntermediateWorkoutRepository intermediateWorkoutRepository;

    public IntermediateWorkoutResource(
        IntermediateWorkoutService intermediateWorkoutService,
        IntermediateWorkoutRepository intermediateWorkoutRepository
    ) {
        this.intermediateWorkoutService = intermediateWorkoutService;
        this.intermediateWorkoutRepository = intermediateWorkoutRepository;
    }

    /**
     * {@code POST  /intermediate-workouts} : Create a new intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the intermediateWorkoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intermediateWorkoutDTO, or with status {@code 400 (Bad Request)} if the intermediateWorkout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/intermediate-workouts")
    public ResponseEntity<IntermediateWorkoutDTO> createIntermediateWorkout(
        @Valid @RequestBody IntermediateWorkoutDTO intermediateWorkoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IntermediateWorkout : {}", intermediateWorkoutDTO);
        if (intermediateWorkoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new intermediateWorkout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IntermediateWorkoutDTO result = intermediateWorkoutService.save(intermediateWorkoutDTO);
        return ResponseEntity
            .created(new URI("/api/intermediate-workouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /intermediate-workouts/:id} : Updates an existing intermediateWorkout.
     *
     * @param id the id of the intermediateWorkoutDTO to save.
     * @param intermediateWorkoutDTO the intermediateWorkoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intermediateWorkoutDTO,
     * or with status {@code 400 (Bad Request)} if the intermediateWorkoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intermediateWorkoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/intermediate-workouts/{id}")
    public ResponseEntity<IntermediateWorkoutDTO> updateIntermediateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IntermediateWorkoutDTO intermediateWorkoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IntermediateWorkout : {}, {}", id, intermediateWorkoutDTO);
        if (intermediateWorkoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intermediateWorkoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intermediateWorkoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IntermediateWorkoutDTO result = intermediateWorkoutService.update(intermediateWorkoutDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intermediateWorkoutDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /intermediate-workouts/:id} : Partial updates given fields of an existing intermediateWorkout, field will ignore if it is null
     *
     * @param id the id of the intermediateWorkoutDTO to save.
     * @param intermediateWorkoutDTO the intermediateWorkoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intermediateWorkoutDTO,
     * or with status {@code 400 (Bad Request)} if the intermediateWorkoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the intermediateWorkoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the intermediateWorkoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/intermediate-workouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IntermediateWorkoutDTO> partialUpdateIntermediateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IntermediateWorkoutDTO intermediateWorkoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IntermediateWorkout partially : {}, {}", id, intermediateWorkoutDTO);
        if (intermediateWorkoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intermediateWorkoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intermediateWorkoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IntermediateWorkoutDTO> result = intermediateWorkoutService.partialUpdate(intermediateWorkoutDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intermediateWorkoutDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /intermediate-workouts} : get all the intermediateWorkouts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of intermediateWorkouts in body.
     */
    @GetMapping("/intermediate-workouts")
    public List<IntermediateWorkoutDTO> getAllIntermediateWorkouts() {
        log.debug("REST request to get all IntermediateWorkouts");
        return intermediateWorkoutService.findAll();
    }

    /**
     * {@code GET  /intermediate-workouts/:id} : get the "id" intermediateWorkout.
     *
     * @param id the id of the intermediateWorkoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intermediateWorkoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intermediate-workouts/{id}")
    public ResponseEntity<IntermediateWorkoutDTO> getIntermediateWorkout(@PathVariable Long id) {
        log.debug("REST request to get IntermediateWorkout : {}", id);
        Optional<IntermediateWorkoutDTO> intermediateWorkoutDTO = intermediateWorkoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(intermediateWorkoutDTO);
    }

    /**
     * {@code DELETE  /intermediate-workouts/:id} : delete the "id" intermediateWorkout.
     *
     * @param id the id of the intermediateWorkoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intermediate-workouts/{id}")
    public ResponseEntity<Void> deleteIntermediateWorkout(@PathVariable Long id) {
        log.debug("REST request to delete IntermediateWorkout : {}", id);
        intermediateWorkoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.nitetrain.web.rest;

import com.nitetrain.repository.IntermediateWorkoutRepository;
import com.nitetrain.service.IntermediateWorkoutService;
import com.nitetrain.service.dto.IntermediateWorkoutDTO;
import com.nitetrain.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.nitetrain.domain.IntermediateWorkout}.
 */
@RestController
@RequestMapping("/api")
public class IntermediateWorkoutResource {

    private final Logger log = LoggerFactory.getLogger(IntermediateWorkoutResource.class);

    private static final String ENTITY_NAME = "intermediateWorkout";

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
    public Mono<ResponseEntity<IntermediateWorkoutDTO>> createIntermediateWorkout(
        @Valid @RequestBody IntermediateWorkoutDTO intermediateWorkoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IntermediateWorkout : {}", intermediateWorkoutDTO);
        if (intermediateWorkoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new intermediateWorkout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return intermediateWorkoutService
            .save(intermediateWorkoutDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/intermediate-workouts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<IntermediateWorkoutDTO>> updateIntermediateWorkout(
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

        return intermediateWorkoutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return intermediateWorkoutService
                    .update(intermediateWorkoutDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<IntermediateWorkoutDTO>> partialUpdateIntermediateWorkout(
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

        return intermediateWorkoutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IntermediateWorkoutDTO> result = intermediateWorkoutService.partialUpdate(intermediateWorkoutDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /intermediate-workouts} : get all the intermediateWorkouts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of intermediateWorkouts in body.
     */
    @GetMapping("/intermediate-workouts")
    public Mono<List<IntermediateWorkoutDTO>> getAllIntermediateWorkouts() {
        log.debug("REST request to get all IntermediateWorkouts");
        return intermediateWorkoutService.findAll().collectList();
    }

    /**
     * {@code GET  /intermediate-workouts} : get all the intermediateWorkouts as a stream.
     * @return the {@link Flux} of intermediateWorkouts.
     */
    @GetMapping(value = "/intermediate-workouts", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IntermediateWorkoutDTO> getAllIntermediateWorkoutsAsStream() {
        log.debug("REST request to get all IntermediateWorkouts as a stream");
        return intermediateWorkoutService.findAll();
    }

    /**
     * {@code GET  /intermediate-workouts/:id} : get the "id" intermediateWorkout.
     *
     * @param id the id of the intermediateWorkoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intermediateWorkoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intermediate-workouts/{id}")
    public Mono<ResponseEntity<IntermediateWorkoutDTO>> getIntermediateWorkout(@PathVariable Long id) {
        log.debug("REST request to get IntermediateWorkout : {}", id);
        Mono<IntermediateWorkoutDTO> intermediateWorkoutDTO = intermediateWorkoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(intermediateWorkoutDTO);
    }

    /**
     * {@code DELETE  /intermediate-workouts/:id} : delete the "id" intermediateWorkout.
     *
     * @param id the id of the intermediateWorkoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intermediate-workouts/{id}")
    public Mono<ResponseEntity<Void>> deleteIntermediateWorkout(@PathVariable Long id) {
        log.debug("REST request to delete IntermediateWorkout : {}", id);
        return intermediateWorkoutService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}

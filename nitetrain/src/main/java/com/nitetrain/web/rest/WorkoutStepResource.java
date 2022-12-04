package com.nitetrain.web.rest;

import com.nitetrain.repository.WorkoutStepRepository;
import com.nitetrain.service.WorkoutStepService;
import com.nitetrain.service.dto.WorkoutStepDTO;
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
 * REST controller for managing {@link com.nitetrain.domain.WorkoutStep}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutStepResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutStepResource.class);

    private static final String ENTITY_NAME = "workoutStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutStepService workoutStepService;

    private final WorkoutStepRepository workoutStepRepository;

    public WorkoutStepResource(WorkoutStepService workoutStepService, WorkoutStepRepository workoutStepRepository) {
        this.workoutStepService = workoutStepService;
        this.workoutStepRepository = workoutStepRepository;
    }

    /**
     * {@code POST  /workout-steps} : Create a new workoutStep.
     *
     * @param workoutStepDTO the workoutStepDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutStepDTO, or with status {@code 400 (Bad Request)} if the workoutStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-steps")
    public Mono<ResponseEntity<WorkoutStepDTO>> createWorkoutStep(@Valid @RequestBody WorkoutStepDTO workoutStepDTO)
        throws URISyntaxException {
        log.debug("REST request to save WorkoutStep : {}", workoutStepDTO);
        if (workoutStepDTO.getId() != null) {
            throw new BadRequestAlertException("A new workoutStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return workoutStepService
            .save(workoutStepDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/workout-steps/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /workout-steps/:id} : Updates an existing workoutStep.
     *
     * @param id the id of the workoutStepDTO to save.
     * @param workoutStepDTO the workoutStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutStepDTO,
     * or with status {@code 400 (Bad Request)} if the workoutStepDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-steps/{id}")
    public Mono<ResponseEntity<WorkoutStepDTO>> updateWorkoutStep(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkoutStepDTO workoutStepDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutStep : {}, {}", id, workoutStepDTO);
        if (workoutStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workoutStepRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return workoutStepService
                    .update(workoutStepDTO)
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
     * {@code PATCH  /workout-steps/:id} : Partial updates given fields of an existing workoutStep, field will ignore if it is null
     *
     * @param id the id of the workoutStepDTO to save.
     * @param workoutStepDTO the workoutStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutStepDTO,
     * or with status {@code 400 (Bad Request)} if the workoutStepDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workoutStepDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-steps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WorkoutStepDTO>> partialUpdateWorkoutStep(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkoutStepDTO workoutStepDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutStep partially : {}, {}", id, workoutStepDTO);
        if (workoutStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workoutStepRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WorkoutStepDTO> result = workoutStepService.partialUpdate(workoutStepDTO);

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
     * {@code GET  /workout-steps} : get all the workoutSteps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutSteps in body.
     */
    @GetMapping("/workout-steps")
    public Mono<List<WorkoutStepDTO>> getAllWorkoutSteps() {
        log.debug("REST request to get all WorkoutSteps");
        return workoutStepService.findAll().collectList();
    }

    /**
     * {@code GET  /workout-steps} : get all the workoutSteps as a stream.
     * @return the {@link Flux} of workoutSteps.
     */
    @GetMapping(value = "/workout-steps", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<WorkoutStepDTO> getAllWorkoutStepsAsStream() {
        log.debug("REST request to get all WorkoutSteps as a stream");
        return workoutStepService.findAll();
    }

    /**
     * {@code GET  /workout-steps/:id} : get the "id" workoutStep.
     *
     * @param id the id of the workoutStepDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutStepDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-steps/{id}")
    public Mono<ResponseEntity<WorkoutStepDTO>> getWorkoutStep(@PathVariable Long id) {
        log.debug("REST request to get WorkoutStep : {}", id);
        Mono<WorkoutStepDTO> workoutStepDTO = workoutStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutStepDTO);
    }

    /**
     * {@code DELETE  /workout-steps/:id} : delete the "id" workoutStep.
     *
     * @param id the id of the workoutStepDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-steps/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkoutStep(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutStep : {}", id);
        return workoutStepService
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

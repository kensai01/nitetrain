package com.nitetrain.web.rest;

import com.nitetrain.repository.WorkoutRepository;
import com.nitetrain.service.WorkoutService;
import com.nitetrain.service.dto.WorkoutDTO;
import com.nitetrain.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.nitetrain.domain.Workout}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutResource.class);

    private static final String ENTITY_NAME = "workout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutService workoutService;

    private final WorkoutRepository workoutRepository;

    public WorkoutResource(WorkoutService workoutService, WorkoutRepository workoutRepository) {
        this.workoutService = workoutService;
        this.workoutRepository = workoutRepository;
    }

    /**
     * {@code POST  /workouts} : Create a new workout.
     *
     * @param workoutDTO the workoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutDTO, or with status {@code 400 (Bad Request)} if the workout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workouts")
    public Mono<ResponseEntity<WorkoutDTO>> createWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) throws URISyntaxException {
        log.debug("REST request to save Workout : {}", workoutDTO);
        if (workoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new workout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return workoutService
            .save(workoutDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/workouts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /workouts/:id} : Updates an existing workout.
     *
     * @param id the id of the workoutDTO to save.
     * @param workoutDTO the workoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutDTO,
     * or with status {@code 400 (Bad Request)} if the workoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workouts/{id}")
    public Mono<ResponseEntity<WorkoutDTO>> updateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkoutDTO workoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Workout : {}, {}", id, workoutDTO);
        if (workoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workoutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return workoutService
                    .update(workoutDTO)
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
     * {@code PATCH  /workouts/:id} : Partial updates given fields of an existing workout, field will ignore if it is null
     *
     * @param id the id of the workoutDTO to save.
     * @param workoutDTO the workoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutDTO,
     * or with status {@code 400 (Bad Request)} if the workoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WorkoutDTO>> partialUpdateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkoutDTO workoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Workout partially : {}, {}", id, workoutDTO);
        if (workoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workoutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WorkoutDTO> result = workoutService.partialUpdate(workoutDTO);

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
     * {@code GET  /workouts} : get all the workouts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workouts in body.
     */
    @GetMapping("/workouts")
    public Mono<ResponseEntity<List<WorkoutDTO>>> getAllWorkouts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("beginnerworkout-is-null".equals(filter)) {
            log.debug("REST request to get all Workouts where beginnerWorkout is null");
            return workoutService.findAllWhereBeginnerWorkoutIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("intermediateworkout-is-null".equals(filter)) {
            log.debug("REST request to get all Workouts where intermediateWorkout is null");
            return workoutService.findAllWhereIntermediateWorkoutIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of Workouts");
        return workoutService
            .countAll()
            .zipWith(workoutService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /workouts/:id} : get the "id" workout.
     *
     * @param id the id of the workoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workouts/{id}")
    public Mono<ResponseEntity<WorkoutDTO>> getWorkout(@PathVariable Long id) {
        log.debug("REST request to get Workout : {}", id);
        Mono<WorkoutDTO> workoutDTO = workoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutDTO);
    }

    /**
     * {@code DELETE  /workouts/:id} : delete the "id" workout.
     *
     * @param id the id of the workoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workouts/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkout(@PathVariable Long id) {
        log.debug("REST request to delete Workout : {}", id);
        return workoutService
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

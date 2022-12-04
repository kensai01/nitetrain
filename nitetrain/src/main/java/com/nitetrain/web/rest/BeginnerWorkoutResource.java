package com.nitetrain.web.rest;

import com.nitetrain.repository.BeginnerWorkoutRepository;
import com.nitetrain.service.BeginnerWorkoutService;
import com.nitetrain.service.dto.BeginnerWorkoutDTO;
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
 * REST controller for managing {@link com.nitetrain.domain.BeginnerWorkout}.
 */
@RestController
@RequestMapping("/api")
public class BeginnerWorkoutResource {

    private final Logger log = LoggerFactory.getLogger(BeginnerWorkoutResource.class);

    private static final String ENTITY_NAME = "beginnerWorkout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeginnerWorkoutService beginnerWorkoutService;

    private final BeginnerWorkoutRepository beginnerWorkoutRepository;

    public BeginnerWorkoutResource(BeginnerWorkoutService beginnerWorkoutService, BeginnerWorkoutRepository beginnerWorkoutRepository) {
        this.beginnerWorkoutService = beginnerWorkoutService;
        this.beginnerWorkoutRepository = beginnerWorkoutRepository;
    }

    /**
     * {@code POST  /beginner-workouts} : Create a new beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the beginnerWorkoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beginnerWorkoutDTO, or with status {@code 400 (Bad Request)} if the beginnerWorkout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beginner-workouts")
    public Mono<ResponseEntity<BeginnerWorkoutDTO>> createBeginnerWorkout(@Valid @RequestBody BeginnerWorkoutDTO beginnerWorkoutDTO)
        throws URISyntaxException {
        log.debug("REST request to save BeginnerWorkout : {}", beginnerWorkoutDTO);
        if (beginnerWorkoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new beginnerWorkout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return beginnerWorkoutService
            .save(beginnerWorkoutDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/beginner-workouts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /beginner-workouts/:id} : Updates an existing beginnerWorkout.
     *
     * @param id the id of the beginnerWorkoutDTO to save.
     * @param beginnerWorkoutDTO the beginnerWorkoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beginnerWorkoutDTO,
     * or with status {@code 400 (Bad Request)} if the beginnerWorkoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beginnerWorkoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beginner-workouts/{id}")
    public Mono<ResponseEntity<BeginnerWorkoutDTO>> updateBeginnerWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BeginnerWorkoutDTO beginnerWorkoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BeginnerWorkout : {}, {}", id, beginnerWorkoutDTO);
        if (beginnerWorkoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beginnerWorkoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return beginnerWorkoutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return beginnerWorkoutService
                    .update(beginnerWorkoutDTO)
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
     * {@code PATCH  /beginner-workouts/:id} : Partial updates given fields of an existing beginnerWorkout, field will ignore if it is null
     *
     * @param id the id of the beginnerWorkoutDTO to save.
     * @param beginnerWorkoutDTO the beginnerWorkoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beginnerWorkoutDTO,
     * or with status {@code 400 (Bad Request)} if the beginnerWorkoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the beginnerWorkoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the beginnerWorkoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beginner-workouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BeginnerWorkoutDTO>> partialUpdateBeginnerWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BeginnerWorkoutDTO beginnerWorkoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BeginnerWorkout partially : {}, {}", id, beginnerWorkoutDTO);
        if (beginnerWorkoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beginnerWorkoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return beginnerWorkoutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BeginnerWorkoutDTO> result = beginnerWorkoutService.partialUpdate(beginnerWorkoutDTO);

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
     * {@code GET  /beginner-workouts} : get all the beginnerWorkouts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beginnerWorkouts in body.
     */
    @GetMapping("/beginner-workouts")
    public Mono<List<BeginnerWorkoutDTO>> getAllBeginnerWorkouts() {
        log.debug("REST request to get all BeginnerWorkouts");
        return beginnerWorkoutService.findAll().collectList();
    }

    /**
     * {@code GET  /beginner-workouts} : get all the beginnerWorkouts as a stream.
     * @return the {@link Flux} of beginnerWorkouts.
     */
    @GetMapping(value = "/beginner-workouts", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<BeginnerWorkoutDTO> getAllBeginnerWorkoutsAsStream() {
        log.debug("REST request to get all BeginnerWorkouts as a stream");
        return beginnerWorkoutService.findAll();
    }

    /**
     * {@code GET  /beginner-workouts/:id} : get the "id" beginnerWorkout.
     *
     * @param id the id of the beginnerWorkoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beginnerWorkoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beginner-workouts/{id}")
    public Mono<ResponseEntity<BeginnerWorkoutDTO>> getBeginnerWorkout(@PathVariable Long id) {
        log.debug("REST request to get BeginnerWorkout : {}", id);
        Mono<BeginnerWorkoutDTO> beginnerWorkoutDTO = beginnerWorkoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beginnerWorkoutDTO);
    }

    /**
     * {@code DELETE  /beginner-workouts/:id} : delete the "id" beginnerWorkout.
     *
     * @param id the id of the beginnerWorkoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beginner-workouts/{id}")
    public Mono<ResponseEntity<Void>> deleteBeginnerWorkout(@PathVariable Long id) {
        log.debug("REST request to delete BeginnerWorkout : {}", id);
        return beginnerWorkoutService
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

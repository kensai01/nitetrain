package com.nitetrain.service;

import com.nitetrain.domain.Workout;
import com.nitetrain.repository.WorkoutRepository;
import com.nitetrain.service.dto.WorkoutDTO;
import com.nitetrain.service.mapper.WorkoutMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Workout}.
 */
@Service
@Transactional
public class WorkoutService {

    private final Logger log = LoggerFactory.getLogger(WorkoutService.class);

    private final WorkoutRepository workoutRepository;

    private final WorkoutMapper workoutMapper;

    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
    }

    /**
     * Save a workout.
     *
     * @param workoutDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkoutDTO> save(WorkoutDTO workoutDTO) {
        log.debug("Request to save Workout : {}", workoutDTO);
        return workoutRepository.save(workoutMapper.toEntity(workoutDTO)).map(workoutMapper::toDto);
    }

    /**
     * Update a workout.
     *
     * @param workoutDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkoutDTO> update(WorkoutDTO workoutDTO) {
        log.debug("Request to update Workout : {}", workoutDTO);
        return workoutRepository.save(workoutMapper.toEntity(workoutDTO)).map(workoutMapper::toDto);
    }

    /**
     * Partially update a workout.
     *
     * @param workoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WorkoutDTO> partialUpdate(WorkoutDTO workoutDTO) {
        log.debug("Request to partially update Workout : {}", workoutDTO);

        return workoutRepository
            .findById(workoutDTO.getId())
            .map(existingWorkout -> {
                workoutMapper.partialUpdate(existingWorkout, workoutDTO);

                return existingWorkout;
            })
            .flatMap(workoutRepository::save)
            .map(workoutMapper::toDto);
    }

    /**
     * Get all the workouts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkoutDTO> findAll() {
        log.debug("Request to get all Workouts");
        return workoutRepository.findAll().map(workoutMapper::toDto);
    }

    /**
     *  Get all the workouts where BeginnerWorkout is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkoutDTO> findAllWhereBeginnerWorkoutIsNull() {
        log.debug("Request to get all workouts where BeginnerWorkout is null");
        return workoutRepository.findAllWhereBeginnerWorkoutIsNull().map(workoutMapper::toDto);
    }

    /**
     *  Get all the workouts where IntermediateWorkout is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkoutDTO> findAllWhereIntermediateWorkoutIsNull() {
        log.debug("Request to get all workouts where IntermediateWorkout is null");
        return workoutRepository.findAllWhereIntermediateWorkoutIsNull().map(workoutMapper::toDto);
    }

    /**
     * Returns the number of workouts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return workoutRepository.count();
    }

    /**
     * Get one workout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WorkoutDTO> findOne(Long id) {
        log.debug("Request to get Workout : {}", id);
        return workoutRepository.findById(id).map(workoutMapper::toDto);
    }

    /**
     * Delete the workout by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Workout : {}", id);
        return workoutRepository.deleteById(id);
    }
}

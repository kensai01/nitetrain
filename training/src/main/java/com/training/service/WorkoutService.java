package com.training.service;

import com.training.service.dto.WorkoutDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.training.domain.Workout}.
 */
public interface WorkoutService {
    /**
     * Save a workout.
     *
     * @param workoutDTO the entity to save.
     * @return the persisted entity.
     */
    WorkoutDTO save(WorkoutDTO workoutDTO);

    /**
     * Updates a workout.
     *
     * @param workoutDTO the entity to update.
     * @return the persisted entity.
     */
    WorkoutDTO update(WorkoutDTO workoutDTO);

    /**
     * Partially updates a workout.
     *
     * @param workoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkoutDTO> partialUpdate(WorkoutDTO workoutDTO);

    /**
     * Get all the workouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkoutDTO> findAll(Pageable pageable);
    /**
     * Get all the WorkoutDTO where BeginnerWorkout is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<WorkoutDTO> findAllWhereBeginnerWorkoutIsNull();
    /**
     * Get all the WorkoutDTO where IntermediateWorkout is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<WorkoutDTO> findAllWhereIntermediateWorkoutIsNull();

    /**
     * Get the "id" workout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkoutDTO> findOne(Long id);

    /**
     * Delete the "id" workout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

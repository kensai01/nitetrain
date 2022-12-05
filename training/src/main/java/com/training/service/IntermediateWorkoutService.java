package com.training.service;

import com.training.service.dto.IntermediateWorkoutDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.training.domain.IntermediateWorkout}.
 */
public interface IntermediateWorkoutService {
    /**
     * Save a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    IntermediateWorkoutDTO save(IntermediateWorkoutDTO intermediateWorkoutDTO);

    /**
     * Updates a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to update.
     * @return the persisted entity.
     */
    IntermediateWorkoutDTO update(IntermediateWorkoutDTO intermediateWorkoutDTO);

    /**
     * Partially updates a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IntermediateWorkoutDTO> partialUpdate(IntermediateWorkoutDTO intermediateWorkoutDTO);

    /**
     * Get all the intermediateWorkouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IntermediateWorkoutDTO> findAll(Pageable pageable);

    /**
     * Get the "id" intermediateWorkout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IntermediateWorkoutDTO> findOne(Long id);

    /**
     * Delete the "id" intermediateWorkout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

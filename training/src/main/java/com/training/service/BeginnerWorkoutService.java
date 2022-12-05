package com.training.service;

import com.training.service.dto.BeginnerWorkoutDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.training.domain.BeginnerWorkout}.
 */
public interface BeginnerWorkoutService {
    /**
     * Save a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    BeginnerWorkoutDTO save(BeginnerWorkoutDTO beginnerWorkoutDTO);

    /**
     * Updates a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to update.
     * @return the persisted entity.
     */
    BeginnerWorkoutDTO update(BeginnerWorkoutDTO beginnerWorkoutDTO);

    /**
     * Partially updates a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BeginnerWorkoutDTO> partialUpdate(BeginnerWorkoutDTO beginnerWorkoutDTO);

    /**
     * Get all the beginnerWorkouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BeginnerWorkoutDTO> findAll(Pageable pageable);

    /**
     * Get the "id" beginnerWorkout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BeginnerWorkoutDTO> findOne(Long id);

    /**
     * Delete the "id" beginnerWorkout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

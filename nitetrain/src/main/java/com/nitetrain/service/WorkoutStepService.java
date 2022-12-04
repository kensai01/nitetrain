package com.nitetrain.service;

import com.nitetrain.domain.WorkoutStep;
import com.nitetrain.repository.WorkoutStepRepository;
import com.nitetrain.service.dto.WorkoutStepDTO;
import com.nitetrain.service.mapper.WorkoutStepMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link WorkoutStep}.
 */
@Service
@Transactional
public class WorkoutStepService {

    private final Logger log = LoggerFactory.getLogger(WorkoutStepService.class);

    private final WorkoutStepRepository workoutStepRepository;

    private final WorkoutStepMapper workoutStepMapper;

    public WorkoutStepService(WorkoutStepRepository workoutStepRepository, WorkoutStepMapper workoutStepMapper) {
        this.workoutStepRepository = workoutStepRepository;
        this.workoutStepMapper = workoutStepMapper;
    }

    /**
     * Save a workoutStep.
     *
     * @param workoutStepDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkoutStepDTO> save(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to save WorkoutStep : {}", workoutStepDTO);
        return workoutStepRepository.save(workoutStepMapper.toEntity(workoutStepDTO)).map(workoutStepMapper::toDto);
    }

    /**
     * Update a workoutStep.
     *
     * @param workoutStepDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkoutStepDTO> update(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to update WorkoutStep : {}", workoutStepDTO);
        return workoutStepRepository.save(workoutStepMapper.toEntity(workoutStepDTO)).map(workoutStepMapper::toDto);
    }

    /**
     * Partially update a workoutStep.
     *
     * @param workoutStepDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WorkoutStepDTO> partialUpdate(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to partially update WorkoutStep : {}", workoutStepDTO);

        return workoutStepRepository
            .findById(workoutStepDTO.getId())
            .map(existingWorkoutStep -> {
                workoutStepMapper.partialUpdate(existingWorkoutStep, workoutStepDTO);

                return existingWorkoutStep;
            })
            .flatMap(workoutStepRepository::save)
            .map(workoutStepMapper::toDto);
    }

    /**
     * Get all the workoutSteps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkoutStepDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSteps");
        return workoutStepRepository.findAllBy(pageable).map(workoutStepMapper::toDto);
    }

    /**
     * Returns the number of workoutSteps available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return workoutStepRepository.count();
    }

    /**
     * Get one workoutStep by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WorkoutStepDTO> findOne(Long id) {
        log.debug("Request to get WorkoutStep : {}", id);
        return workoutStepRepository.findById(id).map(workoutStepMapper::toDto);
    }

    /**
     * Delete the workoutStep by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete WorkoutStep : {}", id);
        return workoutStepRepository.deleteById(id);
    }
}

package com.training.service;

import com.training.domain.WorkoutStep;
import com.training.repository.WorkoutStepRepository;
import com.training.service.dto.WorkoutStepDTO;
import com.training.service.mapper.WorkoutStepMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public WorkoutStepDTO save(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to save WorkoutStep : {}", workoutStepDTO);
        WorkoutStep workoutStep = workoutStepMapper.toEntity(workoutStepDTO);
        workoutStep = workoutStepRepository.save(workoutStep);
        return workoutStepMapper.toDto(workoutStep);
    }

    /**
     * Update a workoutStep.
     *
     * @param workoutStepDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkoutStepDTO update(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to update WorkoutStep : {}", workoutStepDTO);
        WorkoutStep workoutStep = workoutStepMapper.toEntity(workoutStepDTO);
        workoutStep = workoutStepRepository.save(workoutStep);
        return workoutStepMapper.toDto(workoutStep);
    }

    /**
     * Partially update a workoutStep.
     *
     * @param workoutStepDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkoutStepDTO> partialUpdate(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to partially update WorkoutStep : {}", workoutStepDTO);

        return workoutStepRepository
            .findById(workoutStepDTO.getId())
            .map(existingWorkoutStep -> {
                workoutStepMapper.partialUpdate(existingWorkoutStep, workoutStepDTO);

                return existingWorkoutStep;
            })
            .map(workoutStepRepository::save)
            .map(workoutStepMapper::toDto);
    }

    /**
     * Get all the workoutSteps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkoutStepDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSteps");
        return workoutStepRepository.findAll(pageable).map(workoutStepMapper::toDto);
    }

    /**
     * Get one workoutStep by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkoutStepDTO> findOne(Long id) {
        log.debug("Request to get WorkoutStep : {}", id);
        return workoutStepRepository.findById(id).map(workoutStepMapper::toDto);
    }

    /**
     * Delete the workoutStep by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkoutStep : {}", id);
        workoutStepRepository.deleteById(id);
    }
}

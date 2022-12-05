package com.training.service.impl;

import com.training.domain.WorkoutStep;
import com.training.repository.WorkoutStepRepository;
import com.training.service.WorkoutStepService;
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
public class WorkoutStepServiceImpl implements WorkoutStepService {

    private final Logger log = LoggerFactory.getLogger(WorkoutStepServiceImpl.class);

    private final WorkoutStepRepository workoutStepRepository;

    private final WorkoutStepMapper workoutStepMapper;

    public WorkoutStepServiceImpl(WorkoutStepRepository workoutStepRepository, WorkoutStepMapper workoutStepMapper) {
        this.workoutStepRepository = workoutStepRepository;
        this.workoutStepMapper = workoutStepMapper;
    }

    @Override
    public WorkoutStepDTO save(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to save WorkoutStep : {}", workoutStepDTO);
        WorkoutStep workoutStep = workoutStepMapper.toEntity(workoutStepDTO);
        workoutStep = workoutStepRepository.save(workoutStep);
        return workoutStepMapper.toDto(workoutStep);
    }

    @Override
    public WorkoutStepDTO update(WorkoutStepDTO workoutStepDTO) {
        log.debug("Request to update WorkoutStep : {}", workoutStepDTO);
        WorkoutStep workoutStep = workoutStepMapper.toEntity(workoutStepDTO);
        workoutStep = workoutStepRepository.save(workoutStep);
        return workoutStepMapper.toDto(workoutStep);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutStepDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkoutSteps");
        return workoutStepRepository.findAll(pageable).map(workoutStepMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutStepDTO> findOne(Long id) {
        log.debug("Request to get WorkoutStep : {}", id);
        return workoutStepRepository.findById(id).map(workoutStepMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkoutStep : {}", id);
        workoutStepRepository.deleteById(id);
    }
}

package com.training.service.impl;

import com.training.domain.Workout;
import com.training.repository.WorkoutRepository;
import com.training.service.WorkoutService;
import com.training.service.dto.WorkoutDTO;
import com.training.service.mapper.WorkoutMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Workout}.
 */
@Service
@Transactional
public class WorkoutServiceImpl implements WorkoutService {

    private final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);

    private final WorkoutRepository workoutRepository;

    private final WorkoutMapper workoutMapper;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
    }

    @Override
    public WorkoutDTO save(WorkoutDTO workoutDTO) {
        log.debug("Request to save Workout : {}", workoutDTO);
        Workout workout = workoutMapper.toEntity(workoutDTO);
        workout = workoutRepository.save(workout);
        return workoutMapper.toDto(workout);
    }

    @Override
    public WorkoutDTO update(WorkoutDTO workoutDTO) {
        log.debug("Request to update Workout : {}", workoutDTO);
        Workout workout = workoutMapper.toEntity(workoutDTO);
        workout = workoutRepository.save(workout);
        return workoutMapper.toDto(workout);
    }

    @Override
    public Optional<WorkoutDTO> partialUpdate(WorkoutDTO workoutDTO) {
        log.debug("Request to partially update Workout : {}", workoutDTO);

        return workoutRepository
            .findById(workoutDTO.getId())
            .map(existingWorkout -> {
                workoutMapper.partialUpdate(existingWorkout, workoutDTO);

                return existingWorkout;
            })
            .map(workoutRepository::save)
            .map(workoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workouts");
        return workoutRepository.findAll(pageable).map(workoutMapper::toDto);
    }

    /**
     *  Get all the workouts where BeginnerWorkout is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkoutDTO> findAllWhereBeginnerWorkoutIsNull() {
        log.debug("Request to get all workouts where BeginnerWorkout is null");
        return StreamSupport
            .stream(workoutRepository.findAll().spliterator(), false)
            .filter(workout -> workout.getBeginnerWorkout() == null)
            .map(workoutMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the workouts where IntermediateWorkout is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkoutDTO> findAllWhereIntermediateWorkoutIsNull() {
        log.debug("Request to get all workouts where IntermediateWorkout is null");
        return StreamSupport
            .stream(workoutRepository.findAll().spliterator(), false)
            .filter(workout -> workout.getIntermediateWorkout() == null)
            .map(workoutMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkoutDTO> findOne(Long id) {
        log.debug("Request to get Workout : {}", id);
        return workoutRepository.findById(id).map(workoutMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workout : {}", id);
        workoutRepository.deleteById(id);
    }
}

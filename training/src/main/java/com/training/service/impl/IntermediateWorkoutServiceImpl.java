package com.training.service.impl;

import com.training.domain.IntermediateWorkout;
import com.training.repository.IntermediateWorkoutRepository;
import com.training.service.IntermediateWorkoutService;
import com.training.service.dto.IntermediateWorkoutDTO;
import com.training.service.mapper.IntermediateWorkoutMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IntermediateWorkout}.
 */
@Service
@Transactional
public class IntermediateWorkoutServiceImpl implements IntermediateWorkoutService {

    private final Logger log = LoggerFactory.getLogger(IntermediateWorkoutServiceImpl.class);

    private final IntermediateWorkoutRepository intermediateWorkoutRepository;

    private final IntermediateWorkoutMapper intermediateWorkoutMapper;

    public IntermediateWorkoutServiceImpl(
        IntermediateWorkoutRepository intermediateWorkoutRepository,
        IntermediateWorkoutMapper intermediateWorkoutMapper
    ) {
        this.intermediateWorkoutRepository = intermediateWorkoutRepository;
        this.intermediateWorkoutMapper = intermediateWorkoutMapper;
    }

    @Override
    public IntermediateWorkoutDTO save(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to save IntermediateWorkout : {}", intermediateWorkoutDTO);
        IntermediateWorkout intermediateWorkout = intermediateWorkoutMapper.toEntity(intermediateWorkoutDTO);
        intermediateWorkout = intermediateWorkoutRepository.save(intermediateWorkout);
        return intermediateWorkoutMapper.toDto(intermediateWorkout);
    }

    @Override
    public IntermediateWorkoutDTO update(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to update IntermediateWorkout : {}", intermediateWorkoutDTO);
        IntermediateWorkout intermediateWorkout = intermediateWorkoutMapper.toEntity(intermediateWorkoutDTO);
        intermediateWorkout = intermediateWorkoutRepository.save(intermediateWorkout);
        return intermediateWorkoutMapper.toDto(intermediateWorkout);
    }

    @Override
    public Optional<IntermediateWorkoutDTO> partialUpdate(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to partially update IntermediateWorkout : {}", intermediateWorkoutDTO);

        return intermediateWorkoutRepository
            .findById(intermediateWorkoutDTO.getId())
            .map(existingIntermediateWorkout -> {
                intermediateWorkoutMapper.partialUpdate(existingIntermediateWorkout, intermediateWorkoutDTO);

                return existingIntermediateWorkout;
            })
            .map(intermediateWorkoutRepository::save)
            .map(intermediateWorkoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IntermediateWorkoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IntermediateWorkouts");
        return intermediateWorkoutRepository.findAll(pageable).map(intermediateWorkoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IntermediateWorkoutDTO> findOne(Long id) {
        log.debug("Request to get IntermediateWorkout : {}", id);
        return intermediateWorkoutRepository.findById(id).map(intermediateWorkoutMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IntermediateWorkout : {}", id);
        intermediateWorkoutRepository.deleteById(id);
    }
}

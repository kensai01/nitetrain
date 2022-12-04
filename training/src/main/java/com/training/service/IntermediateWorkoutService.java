package com.training.service;

import com.training.domain.IntermediateWorkout;
import com.training.repository.IntermediateWorkoutRepository;
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
public class IntermediateWorkoutService {

    private final Logger log = LoggerFactory.getLogger(IntermediateWorkoutService.class);

    private final IntermediateWorkoutRepository intermediateWorkoutRepository;

    private final IntermediateWorkoutMapper intermediateWorkoutMapper;

    public IntermediateWorkoutService(
        IntermediateWorkoutRepository intermediateWorkoutRepository,
        IntermediateWorkoutMapper intermediateWorkoutMapper
    ) {
        this.intermediateWorkoutRepository = intermediateWorkoutRepository;
        this.intermediateWorkoutMapper = intermediateWorkoutMapper;
    }

    /**
     * Save a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    public IntermediateWorkoutDTO save(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to save IntermediateWorkout : {}", intermediateWorkoutDTO);
        IntermediateWorkout intermediateWorkout = intermediateWorkoutMapper.toEntity(intermediateWorkoutDTO);
        intermediateWorkout = intermediateWorkoutRepository.save(intermediateWorkout);
        return intermediateWorkoutMapper.toDto(intermediateWorkout);
    }

    /**
     * Update a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    public IntermediateWorkoutDTO update(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to update IntermediateWorkout : {}", intermediateWorkoutDTO);
        IntermediateWorkout intermediateWorkout = intermediateWorkoutMapper.toEntity(intermediateWorkoutDTO);
        intermediateWorkout = intermediateWorkoutRepository.save(intermediateWorkout);
        return intermediateWorkoutMapper.toDto(intermediateWorkout);
    }

    /**
     * Partially update a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the intermediateWorkouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IntermediateWorkoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IntermediateWorkouts");
        return intermediateWorkoutRepository.findAll(pageable).map(intermediateWorkoutMapper::toDto);
    }

    /**
     * Get one intermediateWorkout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IntermediateWorkoutDTO> findOne(Long id) {
        log.debug("Request to get IntermediateWorkout : {}", id);
        return intermediateWorkoutRepository.findById(id).map(intermediateWorkoutMapper::toDto);
    }

    /**
     * Delete the intermediateWorkout by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IntermediateWorkout : {}", id);
        intermediateWorkoutRepository.deleteById(id);
    }
}

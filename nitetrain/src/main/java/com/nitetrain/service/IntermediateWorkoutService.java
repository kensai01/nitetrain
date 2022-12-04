package com.nitetrain.service;

import com.nitetrain.domain.IntermediateWorkout;
import com.nitetrain.repository.IntermediateWorkoutRepository;
import com.nitetrain.service.dto.IntermediateWorkoutDTO;
import com.nitetrain.service.mapper.IntermediateWorkoutMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<IntermediateWorkoutDTO> save(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to save IntermediateWorkout : {}", intermediateWorkoutDTO);
        return intermediateWorkoutRepository
            .save(intermediateWorkoutMapper.toEntity(intermediateWorkoutDTO))
            .map(intermediateWorkoutMapper::toDto);
    }

    /**
     * Update a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntermediateWorkoutDTO> update(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to update IntermediateWorkout : {}", intermediateWorkoutDTO);
        return intermediateWorkoutRepository
            .save(intermediateWorkoutMapper.toEntity(intermediateWorkoutDTO))
            .map(intermediateWorkoutMapper::toDto);
    }

    /**
     * Partially update a intermediateWorkout.
     *
     * @param intermediateWorkoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IntermediateWorkoutDTO> partialUpdate(IntermediateWorkoutDTO intermediateWorkoutDTO) {
        log.debug("Request to partially update IntermediateWorkout : {}", intermediateWorkoutDTO);

        return intermediateWorkoutRepository
            .findById(intermediateWorkoutDTO.getId())
            .map(existingIntermediateWorkout -> {
                intermediateWorkoutMapper.partialUpdate(existingIntermediateWorkout, intermediateWorkoutDTO);

                return existingIntermediateWorkout;
            })
            .flatMap(intermediateWorkoutRepository::save)
            .map(intermediateWorkoutMapper::toDto);
    }

    /**
     * Get all the intermediateWorkouts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IntermediateWorkoutDTO> findAll() {
        log.debug("Request to get all IntermediateWorkouts");
        return intermediateWorkoutRepository.findAll().map(intermediateWorkoutMapper::toDto);
    }

    /**
     * Returns the number of intermediateWorkouts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return intermediateWorkoutRepository.count();
    }

    /**
     * Get one intermediateWorkout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IntermediateWorkoutDTO> findOne(Long id) {
        log.debug("Request to get IntermediateWorkout : {}", id);
        return intermediateWorkoutRepository.findById(id).map(intermediateWorkoutMapper::toDto);
    }

    /**
     * Delete the intermediateWorkout by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IntermediateWorkout : {}", id);
        return intermediateWorkoutRepository.deleteById(id);
    }
}

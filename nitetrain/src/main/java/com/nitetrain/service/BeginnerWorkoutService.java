package com.nitetrain.service;

import com.nitetrain.domain.BeginnerWorkout;
import com.nitetrain.repository.BeginnerWorkoutRepository;
import com.nitetrain.service.dto.BeginnerWorkoutDTO;
import com.nitetrain.service.mapper.BeginnerWorkoutMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link BeginnerWorkout}.
 */
@Service
@Transactional
public class BeginnerWorkoutService {

    private final Logger log = LoggerFactory.getLogger(BeginnerWorkoutService.class);

    private final BeginnerWorkoutRepository beginnerWorkoutRepository;

    private final BeginnerWorkoutMapper beginnerWorkoutMapper;

    public BeginnerWorkoutService(BeginnerWorkoutRepository beginnerWorkoutRepository, BeginnerWorkoutMapper beginnerWorkoutMapper) {
        this.beginnerWorkoutRepository = beginnerWorkoutRepository;
        this.beginnerWorkoutMapper = beginnerWorkoutMapper;
    }

    /**
     * Save a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BeginnerWorkoutDTO> save(BeginnerWorkoutDTO beginnerWorkoutDTO) {
        log.debug("Request to save BeginnerWorkout : {}", beginnerWorkoutDTO);
        return beginnerWorkoutRepository.save(beginnerWorkoutMapper.toEntity(beginnerWorkoutDTO)).map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Update a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BeginnerWorkoutDTO> update(BeginnerWorkoutDTO beginnerWorkoutDTO) {
        log.debug("Request to update BeginnerWorkout : {}", beginnerWorkoutDTO);
        return beginnerWorkoutRepository.save(beginnerWorkoutMapper.toEntity(beginnerWorkoutDTO)).map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Partially update a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BeginnerWorkoutDTO> partialUpdate(BeginnerWorkoutDTO beginnerWorkoutDTO) {
        log.debug("Request to partially update BeginnerWorkout : {}", beginnerWorkoutDTO);

        return beginnerWorkoutRepository
            .findById(beginnerWorkoutDTO.getId())
            .map(existingBeginnerWorkout -> {
                beginnerWorkoutMapper.partialUpdate(existingBeginnerWorkout, beginnerWorkoutDTO);

                return existingBeginnerWorkout;
            })
            .flatMap(beginnerWorkoutRepository::save)
            .map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Get all the beginnerWorkouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<BeginnerWorkoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BeginnerWorkouts");
        return beginnerWorkoutRepository.findAllBy(pageable).map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Returns the number of beginnerWorkouts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return beginnerWorkoutRepository.count();
    }

    /**
     * Get one beginnerWorkout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<BeginnerWorkoutDTO> findOne(Long id) {
        log.debug("Request to get BeginnerWorkout : {}", id);
        return beginnerWorkoutRepository.findById(id).map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Delete the beginnerWorkout by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete BeginnerWorkout : {}", id);
        return beginnerWorkoutRepository.deleteById(id);
    }
}

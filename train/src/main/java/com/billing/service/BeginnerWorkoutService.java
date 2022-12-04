package com.billing.service;

import com.billing.domain.BeginnerWorkout;
import com.billing.repository.BeginnerWorkoutRepository;
import com.billing.service.dto.BeginnerWorkoutDTO;
import com.billing.service.mapper.BeginnerWorkoutMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public BeginnerWorkoutDTO save(BeginnerWorkoutDTO beginnerWorkoutDTO) {
        log.debug("Request to save BeginnerWorkout : {}", beginnerWorkoutDTO);
        BeginnerWorkout beginnerWorkout = beginnerWorkoutMapper.toEntity(beginnerWorkoutDTO);
        beginnerWorkout = beginnerWorkoutRepository.save(beginnerWorkout);
        return beginnerWorkoutMapper.toDto(beginnerWorkout);
    }

    /**
     * Update a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to save.
     * @return the persisted entity.
     */
    public BeginnerWorkoutDTO update(BeginnerWorkoutDTO beginnerWorkoutDTO) {
        log.debug("Request to update BeginnerWorkout : {}", beginnerWorkoutDTO);
        BeginnerWorkout beginnerWorkout = beginnerWorkoutMapper.toEntity(beginnerWorkoutDTO);
        beginnerWorkout = beginnerWorkoutRepository.save(beginnerWorkout);
        return beginnerWorkoutMapper.toDto(beginnerWorkout);
    }

    /**
     * Partially update a beginnerWorkout.
     *
     * @param beginnerWorkoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BeginnerWorkoutDTO> partialUpdate(BeginnerWorkoutDTO beginnerWorkoutDTO) {
        log.debug("Request to partially update BeginnerWorkout : {}", beginnerWorkoutDTO);

        return beginnerWorkoutRepository
            .findById(beginnerWorkoutDTO.getId())
            .map(existingBeginnerWorkout -> {
                beginnerWorkoutMapper.partialUpdate(existingBeginnerWorkout, beginnerWorkoutDTO);

                return existingBeginnerWorkout;
            })
            .map(beginnerWorkoutRepository::save)
            .map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Get all the beginnerWorkouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BeginnerWorkoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BeginnerWorkouts");
        return beginnerWorkoutRepository.findAll(pageable).map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Get one beginnerWorkout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BeginnerWorkoutDTO> findOne(Long id) {
        log.debug("Request to get BeginnerWorkout : {}", id);
        return beginnerWorkoutRepository.findById(id).map(beginnerWorkoutMapper::toDto);
    }

    /**
     * Delete the beginnerWorkout by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BeginnerWorkout : {}", id);
        beginnerWorkoutRepository.deleteById(id);
    }
}

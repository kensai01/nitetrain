package com.training.service;

import com.training.domain.BeginnerWorkout;
import com.training.repository.BeginnerWorkoutRepository;
import com.training.service.dto.BeginnerWorkoutDTO;
import com.training.service.mapper.BeginnerWorkoutMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BeginnerWorkoutDTO> findAll() {
        log.debug("Request to get all BeginnerWorkouts");
        return beginnerWorkoutRepository
            .findAll()
            .stream()
            .map(beginnerWorkoutMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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

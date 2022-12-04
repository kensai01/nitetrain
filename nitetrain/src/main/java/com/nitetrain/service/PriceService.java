package com.nitetrain.service;

import com.nitetrain.domain.Price;
import com.nitetrain.repository.PriceRepository;
import com.nitetrain.service.dto.PriceDTO;
import com.nitetrain.service.mapper.PriceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Price}.
 */
@Service
@Transactional
public class PriceService {

    private final Logger log = LoggerFactory.getLogger(PriceService.class);

    private final PriceRepository priceRepository;

    private final PriceMapper priceMapper;

    public PriceService(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    /**
     * Save a price.
     *
     * @param priceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PriceDTO> save(PriceDTO priceDTO) {
        log.debug("Request to save Price : {}", priceDTO);
        return priceRepository.save(priceMapper.toEntity(priceDTO)).map(priceMapper::toDto);
    }

    /**
     * Update a price.
     *
     * @param priceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PriceDTO> update(PriceDTO priceDTO) {
        log.debug("Request to update Price : {}", priceDTO);
        return priceRepository.save(priceMapper.toEntity(priceDTO)).map(priceMapper::toDto);
    }

    /**
     * Partially update a price.
     *
     * @param priceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PriceDTO> partialUpdate(PriceDTO priceDTO) {
        log.debug("Request to partially update Price : {}", priceDTO);

        return priceRepository
            .findById(priceDTO.getId())
            .map(existingPrice -> {
                priceMapper.partialUpdate(existingPrice, priceDTO);

                return existingPrice;
            })
            .flatMap(priceRepository::save)
            .map(priceMapper::toDto);
    }

    /**
     * Get all the prices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PriceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prices");
        return priceRepository.findAllBy(pageable).map(priceMapper::toDto);
    }

    /**
     * Returns the number of prices available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return priceRepository.count();
    }

    /**
     * Get one price by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PriceDTO> findOne(Long id) {
        log.debug("Request to get Price : {}", id);
        return priceRepository.findById(id).map(priceMapper::toDto);
    }

    /**
     * Delete the price by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Price : {}", id);
        return priceRepository.deleteById(id);
    }
}

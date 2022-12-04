package com.billing.service;

import com.billing.domain.Price;
import com.billing.repository.PriceRepository;
import com.billing.service.dto.PriceDTO;
import com.billing.service.mapper.PriceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PriceDTO save(PriceDTO priceDTO) {
        log.debug("Request to save Price : {}", priceDTO);
        Price price = priceMapper.toEntity(priceDTO);
        price = priceRepository.save(price);
        return priceMapper.toDto(price);
    }

    /**
     * Update a price.
     *
     * @param priceDTO the entity to save.
     * @return the persisted entity.
     */
    public PriceDTO update(PriceDTO priceDTO) {
        log.debug("Request to update Price : {}", priceDTO);
        Price price = priceMapper.toEntity(priceDTO);
        price = priceRepository.save(price);
        return priceMapper.toDto(price);
    }

    /**
     * Partially update a price.
     *
     * @param priceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PriceDTO> partialUpdate(PriceDTO priceDTO) {
        log.debug("Request to partially update Price : {}", priceDTO);

        return priceRepository
            .findById(priceDTO.getId())
            .map(existingPrice -> {
                priceMapper.partialUpdate(existingPrice, priceDTO);

                return existingPrice;
            })
            .map(priceRepository::save)
            .map(priceMapper::toDto);
    }

    /**
     * Get all the prices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prices");
        return priceRepository.findAll(pageable).map(priceMapper::toDto);
    }

    /**
     * Get one price by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PriceDTO> findOne(Long id) {
        log.debug("Request to get Price : {}", id);
        return priceRepository.findById(id).map(priceMapper::toDto);
    }

    /**
     * Delete the price by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Price : {}", id);
        priceRepository.deleteById(id);
    }
}

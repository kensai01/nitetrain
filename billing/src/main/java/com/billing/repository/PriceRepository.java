package com.billing.repository;

import com.billing.domain.Price;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Price entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {}

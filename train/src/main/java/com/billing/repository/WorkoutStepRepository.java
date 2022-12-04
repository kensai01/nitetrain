package com.billing.repository;

import com.billing.domain.WorkoutStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkoutStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutStepRepository extends JpaRepository<WorkoutStep, Long> {}

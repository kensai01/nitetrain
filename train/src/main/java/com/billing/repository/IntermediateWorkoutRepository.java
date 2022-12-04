package com.billing.repository;

import com.billing.domain.IntermediateWorkout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IntermediateWorkout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntermediateWorkoutRepository extends JpaRepository<IntermediateWorkout, Long> {}

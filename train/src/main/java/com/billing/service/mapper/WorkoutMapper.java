package com.billing.service.mapper;

import com.billing.domain.Workout;
import com.billing.service.dto.WorkoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workout} and its DTO {@link WorkoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkoutMapper extends EntityMapper<WorkoutDTO, Workout> {}

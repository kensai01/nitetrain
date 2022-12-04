package com.training.service.mapper;

import com.training.domain.Workout;
import com.training.service.dto.WorkoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workout} and its DTO {@link WorkoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkoutMapper extends EntityMapper<WorkoutDTO, Workout> {}

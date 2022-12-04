package com.nitetrain.service.mapper;

import com.nitetrain.domain.Workout;
import com.nitetrain.service.dto.WorkoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workout} and its DTO {@link WorkoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkoutMapper extends EntityMapper<WorkoutDTO, Workout> {}

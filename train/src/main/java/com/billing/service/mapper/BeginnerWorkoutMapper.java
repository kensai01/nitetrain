package com.billing.service.mapper;

import com.billing.domain.BeginnerWorkout;
import com.billing.domain.Workout;
import com.billing.service.dto.BeginnerWorkoutDTO;
import com.billing.service.dto.WorkoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BeginnerWorkout} and its DTO {@link BeginnerWorkoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface BeginnerWorkoutMapper extends EntityMapper<BeginnerWorkoutDTO, BeginnerWorkout> {
    @Mapping(target = "workout", source = "workout", qualifiedByName = "workoutId")
    BeginnerWorkoutDTO toDto(BeginnerWorkout s);

    @Named("workoutId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkoutDTO toDtoWorkoutId(Workout workout);
}

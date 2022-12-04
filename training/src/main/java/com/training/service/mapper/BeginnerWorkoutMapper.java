package com.training.service.mapper;

import com.training.domain.BeginnerWorkout;
import com.training.domain.Workout;
import com.training.service.dto.BeginnerWorkoutDTO;
import com.training.service.dto.WorkoutDTO;
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

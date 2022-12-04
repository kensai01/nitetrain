package com.training.service.mapper;

import com.training.domain.IntermediateWorkout;
import com.training.domain.Workout;
import com.training.service.dto.IntermediateWorkoutDTO;
import com.training.service.dto.WorkoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IntermediateWorkout} and its DTO {@link IntermediateWorkoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntermediateWorkoutMapper extends EntityMapper<IntermediateWorkoutDTO, IntermediateWorkout> {
    @Mapping(target = "workout", source = "workout", qualifiedByName = "workoutId")
    IntermediateWorkoutDTO toDto(IntermediateWorkout s);

    @Named("workoutId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkoutDTO toDtoWorkoutId(Workout workout);
}

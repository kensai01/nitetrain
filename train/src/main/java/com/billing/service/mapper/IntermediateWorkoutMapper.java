package com.billing.service.mapper;

import com.billing.domain.IntermediateWorkout;
import com.billing.domain.Workout;
import com.billing.service.dto.IntermediateWorkoutDTO;
import com.billing.service.dto.WorkoutDTO;
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

package com.billing.service.mapper;

import com.billing.domain.BeginnerWorkout;
import com.billing.domain.IntermediateWorkout;
import com.billing.domain.Workout;
import com.billing.domain.WorkoutStep;
import com.billing.service.dto.BeginnerWorkoutDTO;
import com.billing.service.dto.IntermediateWorkoutDTO;
import com.billing.service.dto.WorkoutDTO;
import com.billing.service.dto.WorkoutStepDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkoutStep} and its DTO {@link WorkoutStepDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkoutStepMapper extends EntityMapper<WorkoutStepDTO, WorkoutStep> {
    @Mapping(target = "workout", source = "workout", qualifiedByName = "workoutId")
    @Mapping(target = "beginnerWorkout", source = "beginnerWorkout", qualifiedByName = "beginnerWorkoutId")
    @Mapping(target = "intermediateWorkout", source = "intermediateWorkout", qualifiedByName = "intermediateWorkoutId")
    WorkoutStepDTO toDto(WorkoutStep s);

    @Named("workoutId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkoutDTO toDtoWorkoutId(Workout workout);

    @Named("beginnerWorkoutId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BeginnerWorkoutDTO toDtoBeginnerWorkoutId(BeginnerWorkout beginnerWorkout);

    @Named("intermediateWorkoutId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IntermediateWorkoutDTO toDtoIntermediateWorkoutId(IntermediateWorkout intermediateWorkout);
}

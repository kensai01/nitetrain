package com.training.service.mapper;

import com.training.domain.BeginnerWorkout;
import com.training.domain.IntermediateWorkout;
import com.training.domain.Workout;
import com.training.domain.WorkoutStep;
import com.training.service.dto.BeginnerWorkoutDTO;
import com.training.service.dto.IntermediateWorkoutDTO;
import com.training.service.dto.WorkoutDTO;
import com.training.service.dto.WorkoutStepDTO;
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

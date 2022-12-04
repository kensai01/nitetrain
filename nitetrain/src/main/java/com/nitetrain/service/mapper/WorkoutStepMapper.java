package com.nitetrain.service.mapper;

import com.nitetrain.domain.BeginnerWorkout;
import com.nitetrain.domain.IntermediateWorkout;
import com.nitetrain.domain.Workout;
import com.nitetrain.domain.WorkoutStep;
import com.nitetrain.service.dto.BeginnerWorkoutDTO;
import com.nitetrain.service.dto.IntermediateWorkoutDTO;
import com.nitetrain.service.dto.WorkoutDTO;
import com.nitetrain.service.dto.WorkoutStepDTO;
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

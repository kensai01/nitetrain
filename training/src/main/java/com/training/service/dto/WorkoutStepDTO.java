package com.training.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.training.domain.WorkoutStep} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkoutStepDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Integer stepNumber;

    private WorkoutDTO workout;

    private BeginnerWorkoutDTO beginnerWorkout;

    private IntermediateWorkoutDTO intermediateWorkout;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public WorkoutDTO getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutDTO workout) {
        this.workout = workout;
    }

    public BeginnerWorkoutDTO getBeginnerWorkout() {
        return beginnerWorkout;
    }

    public void setBeginnerWorkout(BeginnerWorkoutDTO beginnerWorkout) {
        this.beginnerWorkout = beginnerWorkout;
    }

    public IntermediateWorkoutDTO getIntermediateWorkout() {
        return intermediateWorkout;
    }

    public void setIntermediateWorkout(IntermediateWorkoutDTO intermediateWorkout) {
        this.intermediateWorkout = intermediateWorkout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutStepDTO)) {
            return false;
        }

        WorkoutStepDTO workoutStepDTO = (WorkoutStepDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workoutStepDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutStepDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", stepNumber=" + getStepNumber() +
            ", workout=" + getWorkout() +
            ", beginnerWorkout=" + getBeginnerWorkout() +
            ", intermediateWorkout=" + getIntermediateWorkout() +
            "}";
    }
}

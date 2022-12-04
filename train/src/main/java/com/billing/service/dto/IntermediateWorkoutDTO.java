package com.billing.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.billing.domain.IntermediateWorkout} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntermediateWorkoutDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private WorkoutDTO workout;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkoutDTO getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutDTO workout) {
        this.workout = workout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntermediateWorkoutDTO)) {
            return false;
        }

        IntermediateWorkoutDTO intermediateWorkoutDTO = (IntermediateWorkoutDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, intermediateWorkoutDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntermediateWorkoutDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", workout=" + getWorkout() +
            "}";
    }
}

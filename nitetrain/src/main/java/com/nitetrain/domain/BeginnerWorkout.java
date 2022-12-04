package com.nitetrain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A BeginnerWorkout.
 */
@Table("beginner_workout")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeginnerWorkout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("description")
    private String description;

    @Transient
    private Workout workout;

    @Transient
    @JsonIgnoreProperties(value = { "workout", "beginnerWorkout", "intermediateWorkout" }, allowSetters = true)
    private Set<WorkoutStep> workoutSteps = new HashSet<>();

    @Column("workout_id")
    private Long workoutId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BeginnerWorkout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public BeginnerWorkout description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workout getWorkout() {
        return this.workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
        this.workoutId = workout != null ? workout.getId() : null;
    }

    public BeginnerWorkout workout(Workout workout) {
        this.setWorkout(workout);
        return this;
    }

    public Set<WorkoutStep> getWorkoutSteps() {
        return this.workoutSteps;
    }

    public void setWorkoutSteps(Set<WorkoutStep> workoutSteps) {
        if (this.workoutSteps != null) {
            this.workoutSteps.forEach(i -> i.setBeginnerWorkout(null));
        }
        if (workoutSteps != null) {
            workoutSteps.forEach(i -> i.setBeginnerWorkout(this));
        }
        this.workoutSteps = workoutSteps;
    }

    public BeginnerWorkout workoutSteps(Set<WorkoutStep> workoutSteps) {
        this.setWorkoutSteps(workoutSteps);
        return this;
    }

    public BeginnerWorkout addWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.add(workoutStep);
        workoutStep.setBeginnerWorkout(this);
        return this;
    }

    public BeginnerWorkout removeWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.remove(workoutStep);
        workoutStep.setBeginnerWorkout(null);
        return this;
    }

    public Long getWorkoutId() {
        return this.workoutId;
    }

    public void setWorkoutId(Long workout) {
        this.workoutId = workout;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeginnerWorkout)) {
            return false;
        }
        return id != null && id.equals(((BeginnerWorkout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeginnerWorkout{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}

package com.nitetrain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WorkoutStep.
 */
@Table("workout_step")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkoutStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("step_number")
    private Integer stepNumber;

    @Transient
    @JsonIgnoreProperties(value = { "workoutSteps", "beginnerWorkout", "intermediateWorkout" }, allowSetters = true)
    private Workout workout;

    @Transient
    @JsonIgnoreProperties(value = { "workout", "workoutSteps" }, allowSetters = true)
    private BeginnerWorkout beginnerWorkout;

    @Transient
    @JsonIgnoreProperties(value = { "workout", "workoutSteps" }, allowSetters = true)
    private IntermediateWorkout intermediateWorkout;

    @Column("workout_id")
    private Long workoutId;

    @Column("beginner_workout_id")
    private Long beginnerWorkoutId;

    @Column("intermediate_workout_id")
    private Long intermediateWorkoutId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkoutStep id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public WorkoutStep title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public WorkoutStep description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStepNumber() {
        return this.stepNumber;
    }

    public WorkoutStep stepNumber(Integer stepNumber) {
        this.setStepNumber(stepNumber);
        return this;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public Workout getWorkout() {
        return this.workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
        this.workoutId = workout != null ? workout.getId() : null;
    }

    public WorkoutStep workout(Workout workout) {
        this.setWorkout(workout);
        return this;
    }

    public BeginnerWorkout getBeginnerWorkout() {
        return this.beginnerWorkout;
    }

    public void setBeginnerWorkout(BeginnerWorkout beginnerWorkout) {
        this.beginnerWorkout = beginnerWorkout;
        this.beginnerWorkoutId = beginnerWorkout != null ? beginnerWorkout.getId() : null;
    }

    public WorkoutStep beginnerWorkout(BeginnerWorkout beginnerWorkout) {
        this.setBeginnerWorkout(beginnerWorkout);
        return this;
    }

    public IntermediateWorkout getIntermediateWorkout() {
        return this.intermediateWorkout;
    }

    public void setIntermediateWorkout(IntermediateWorkout intermediateWorkout) {
        this.intermediateWorkout = intermediateWorkout;
        this.intermediateWorkoutId = intermediateWorkout != null ? intermediateWorkout.getId() : null;
    }

    public WorkoutStep intermediateWorkout(IntermediateWorkout intermediateWorkout) {
        this.setIntermediateWorkout(intermediateWorkout);
        return this;
    }

    public Long getWorkoutId() {
        return this.workoutId;
    }

    public void setWorkoutId(Long workout) {
        this.workoutId = workout;
    }

    public Long getBeginnerWorkoutId() {
        return this.beginnerWorkoutId;
    }

    public void setBeginnerWorkoutId(Long beginnerWorkout) {
        this.beginnerWorkoutId = beginnerWorkout;
    }

    public Long getIntermediateWorkoutId() {
        return this.intermediateWorkoutId;
    }

    public void setIntermediateWorkoutId(Long intermediateWorkout) {
        this.intermediateWorkoutId = intermediateWorkout;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutStep)) {
            return false;
        }
        return id != null && id.equals(((WorkoutStep) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutStep{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", stepNumber=" + getStepNumber() +
            "}";
    }
}

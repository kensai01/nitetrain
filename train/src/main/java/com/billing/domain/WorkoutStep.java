package com.billing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutStep.
 */
@Entity
@Table(name = "workout_step")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkoutStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutSteps", "beginnerWorkout", "intermediateWorkout" }, allowSetters = true)
    private Workout workout;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workout", "workoutSteps" }, allowSetters = true)
    private BeginnerWorkout beginnerWorkout;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workout", "workoutSteps" }, allowSetters = true)
    private IntermediateWorkout intermediateWorkout;

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
    }

    public WorkoutStep intermediateWorkout(IntermediateWorkout intermediateWorkout) {
        this.setIntermediateWorkout(intermediateWorkout);
        return this;
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

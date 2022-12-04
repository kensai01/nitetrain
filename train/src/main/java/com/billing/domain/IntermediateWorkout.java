package com.billing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IntermediateWorkout.
 */
@Entity
@Table(name = "intermediate_workout")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntermediateWorkout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @JsonIgnoreProperties(value = { "workoutSteps", "beginnerWorkout", "intermediateWorkout" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Workout workout;

    @OneToMany(mappedBy = "intermediateWorkout")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workout", "beginnerWorkout", "intermediateWorkout" }, allowSetters = true)
    private Set<WorkoutStep> workoutSteps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IntermediateWorkout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public IntermediateWorkout description(String description) {
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
    }

    public IntermediateWorkout workout(Workout workout) {
        this.setWorkout(workout);
        return this;
    }

    public Set<WorkoutStep> getWorkoutSteps() {
        return this.workoutSteps;
    }

    public void setWorkoutSteps(Set<WorkoutStep> workoutSteps) {
        if (this.workoutSteps != null) {
            this.workoutSteps.forEach(i -> i.setIntermediateWorkout(null));
        }
        if (workoutSteps != null) {
            workoutSteps.forEach(i -> i.setIntermediateWorkout(this));
        }
        this.workoutSteps = workoutSteps;
    }

    public IntermediateWorkout workoutSteps(Set<WorkoutStep> workoutSteps) {
        this.setWorkoutSteps(workoutSteps);
        return this;
    }

    public IntermediateWorkout addWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.add(workoutStep);
        workoutStep.setIntermediateWorkout(this);
        return this;
    }

    public IntermediateWorkout removeWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.remove(workoutStep);
        workoutStep.setIntermediateWorkout(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntermediateWorkout)) {
            return false;
        }
        return id != null && id.equals(((IntermediateWorkout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntermediateWorkout{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}

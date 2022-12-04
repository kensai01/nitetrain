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
 * A BeginnerWorkout.
 */
@Entity
@Table(name = "beginner_workout")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeginnerWorkout implements Serializable {

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

    @OneToMany(mappedBy = "beginnerWorkout")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workout", "beginnerWorkout", "intermediateWorkout" }, allowSetters = true)
    private Set<WorkoutStep> workoutSteps = new HashSet<>();

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

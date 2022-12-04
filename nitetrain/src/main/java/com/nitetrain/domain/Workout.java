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
 * A Workout.
 */
@Table("workout")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Workout implements Serializable {

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

    @Min(value = 0)
    @Max(value = 180)
    @Column("time")
    private Integer time;

    @Column("video_id")
    private String videoId;

    @Column("scaling")
    private String scaling;

    @Transient
    @JsonIgnoreProperties(value = { "workout", "beginnerWorkout", "intermediateWorkout" }, allowSetters = true)
    private Set<WorkoutStep> workoutSteps = new HashSet<>();

    @Transient
    private BeginnerWorkout beginnerWorkout;

    @Transient
    private IntermediateWorkout intermediateWorkout;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Workout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Workout title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Workout description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return this.time;
    }

    public Workout time(Integer time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public Workout videoId(String videoId) {
        this.setVideoId(videoId);
        return this;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getScaling() {
        return this.scaling;
    }

    public Workout scaling(String scaling) {
        this.setScaling(scaling);
        return this;
    }

    public void setScaling(String scaling) {
        this.scaling = scaling;
    }

    public Set<WorkoutStep> getWorkoutSteps() {
        return this.workoutSteps;
    }

    public void setWorkoutSteps(Set<WorkoutStep> workoutSteps) {
        if (this.workoutSteps != null) {
            this.workoutSteps.forEach(i -> i.setWorkout(null));
        }
        if (workoutSteps != null) {
            workoutSteps.forEach(i -> i.setWorkout(this));
        }
        this.workoutSteps = workoutSteps;
    }

    public Workout workoutSteps(Set<WorkoutStep> workoutSteps) {
        this.setWorkoutSteps(workoutSteps);
        return this;
    }

    public Workout addWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.add(workoutStep);
        workoutStep.setWorkout(this);
        return this;
    }

    public Workout removeWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.remove(workoutStep);
        workoutStep.setWorkout(null);
        return this;
    }

    public BeginnerWorkout getBeginnerWorkout() {
        return this.beginnerWorkout;
    }

    public void setBeginnerWorkout(BeginnerWorkout beginnerWorkout) {
        if (this.beginnerWorkout != null) {
            this.beginnerWorkout.setWorkout(null);
        }
        if (beginnerWorkout != null) {
            beginnerWorkout.setWorkout(this);
        }
        this.beginnerWorkout = beginnerWorkout;
    }

    public Workout beginnerWorkout(BeginnerWorkout beginnerWorkout) {
        this.setBeginnerWorkout(beginnerWorkout);
        return this;
    }

    public IntermediateWorkout getIntermediateWorkout() {
        return this.intermediateWorkout;
    }

    public void setIntermediateWorkout(IntermediateWorkout intermediateWorkout) {
        if (this.intermediateWorkout != null) {
            this.intermediateWorkout.setWorkout(null);
        }
        if (intermediateWorkout != null) {
            intermediateWorkout.setWorkout(this);
        }
        this.intermediateWorkout = intermediateWorkout;
    }

    public Workout intermediateWorkout(IntermediateWorkout intermediateWorkout) {
        this.setIntermediateWorkout(intermediateWorkout);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Workout)) {
            return false;
        }
        return id != null && id.equals(((Workout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Workout{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", time=" + getTime() +
            ", videoId='" + getVideoId() + "'" +
            ", scaling='" + getScaling() + "'" +
            "}";
    }
}

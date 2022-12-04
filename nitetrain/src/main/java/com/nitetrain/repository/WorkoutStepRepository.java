package com.nitetrain.repository;

import com.nitetrain.domain.WorkoutStep;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WorkoutStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutStepRepository extends ReactiveCrudRepository<WorkoutStep, Long>, WorkoutStepRepositoryInternal {
    @Query("SELECT * FROM workout_step entity WHERE entity.workout_id = :id")
    Flux<WorkoutStep> findByWorkout(Long id);

    @Query("SELECT * FROM workout_step entity WHERE entity.workout_id IS NULL")
    Flux<WorkoutStep> findAllWhereWorkoutIsNull();

    @Query("SELECT * FROM workout_step entity WHERE entity.beginner_workout_id = :id")
    Flux<WorkoutStep> findByBeginnerWorkout(Long id);

    @Query("SELECT * FROM workout_step entity WHERE entity.beginner_workout_id IS NULL")
    Flux<WorkoutStep> findAllWhereBeginnerWorkoutIsNull();

    @Query("SELECT * FROM workout_step entity WHERE entity.intermediate_workout_id = :id")
    Flux<WorkoutStep> findByIntermediateWorkout(Long id);

    @Query("SELECT * FROM workout_step entity WHERE entity.intermediate_workout_id IS NULL")
    Flux<WorkoutStep> findAllWhereIntermediateWorkoutIsNull();

    @Override
    <S extends WorkoutStep> Mono<S> save(S entity);

    @Override
    Flux<WorkoutStep> findAll();

    @Override
    Mono<WorkoutStep> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WorkoutStepRepositoryInternal {
    <S extends WorkoutStep> Mono<S> save(S entity);

    Flux<WorkoutStep> findAllBy(Pageable pageable);

    Flux<WorkoutStep> findAll();

    Mono<WorkoutStep> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WorkoutStep> findAllBy(Pageable pageable, Criteria criteria);

}

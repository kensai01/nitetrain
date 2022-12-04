package com.nitetrain.repository;

import com.nitetrain.domain.BeginnerWorkout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the BeginnerWorkout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeginnerWorkoutRepository extends ReactiveCrudRepository<BeginnerWorkout, Long>, BeginnerWorkoutRepositoryInternal {
    @Query("SELECT * FROM beginner_workout entity WHERE entity.workout_id = :id")
    Flux<BeginnerWorkout> findByWorkout(Long id);

    @Query("SELECT * FROM beginner_workout entity WHERE entity.workout_id IS NULL")
    Flux<BeginnerWorkout> findAllWhereWorkoutIsNull();

    @Override
    <S extends BeginnerWorkout> Mono<S> save(S entity);

    @Override
    Flux<BeginnerWorkout> findAll();

    @Override
    Mono<BeginnerWorkout> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BeginnerWorkoutRepositoryInternal {
    <S extends BeginnerWorkout> Mono<S> save(S entity);

    Flux<BeginnerWorkout> findAllBy(Pageable pageable);

    Flux<BeginnerWorkout> findAll();

    Mono<BeginnerWorkout> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BeginnerWorkout> findAllBy(Pageable pageable, Criteria criteria);

}

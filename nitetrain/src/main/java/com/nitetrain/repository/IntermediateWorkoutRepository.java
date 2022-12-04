package com.nitetrain.repository;

import com.nitetrain.domain.IntermediateWorkout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IntermediateWorkout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntermediateWorkoutRepository
    extends ReactiveCrudRepository<IntermediateWorkout, Long>, IntermediateWorkoutRepositoryInternal {
    Flux<IntermediateWorkout> findAllBy(Pageable pageable);

    @Query("SELECT * FROM intermediate_workout entity WHERE entity.workout_id = :id")
    Flux<IntermediateWorkout> findByWorkout(Long id);

    @Query("SELECT * FROM intermediate_workout entity WHERE entity.workout_id IS NULL")
    Flux<IntermediateWorkout> findAllWhereWorkoutIsNull();

    @Override
    <S extends IntermediateWorkout> Mono<S> save(S entity);

    @Override
    Flux<IntermediateWorkout> findAll();

    @Override
    Mono<IntermediateWorkout> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IntermediateWorkoutRepositoryInternal {
    <S extends IntermediateWorkout> Mono<S> save(S entity);

    Flux<IntermediateWorkout> findAllBy(Pageable pageable);

    Flux<IntermediateWorkout> findAll();

    Mono<IntermediateWorkout> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IntermediateWorkout> findAllBy(Pageable pageable, Criteria criteria);

}

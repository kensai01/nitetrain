package com.nitetrain.repository;

import com.nitetrain.domain.Workout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Workout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutRepository extends ReactiveCrudRepository<Workout, Long>, WorkoutRepositoryInternal {
    @Query("SELECT * FROM workout entity WHERE entity.id not in (select beginner_workout_id from beginner_workout)")
    Flux<Workout> findAllWhereBeginnerWorkoutIsNull();

    @Query("SELECT * FROM workout entity WHERE entity.id not in (select intermediate_workout_id from intermediate_workout)")
    Flux<Workout> findAllWhereIntermediateWorkoutIsNull();

    @Override
    <S extends Workout> Mono<S> save(S entity);

    @Override
    Flux<Workout> findAll();

    @Override
    Mono<Workout> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WorkoutRepositoryInternal {
    <S extends Workout> Mono<S> save(S entity);

    Flux<Workout> findAllBy(Pageable pageable);

    Flux<Workout> findAll();

    Mono<Workout> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Workout> findAllBy(Pageable pageable, Criteria criteria);

}

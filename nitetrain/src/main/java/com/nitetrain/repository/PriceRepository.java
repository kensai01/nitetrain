package com.nitetrain.repository;

import com.nitetrain.domain.Price;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Price entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriceRepository extends ReactiveCrudRepository<Price, Long>, PriceRepositoryInternal {
    Flux<Price> findAllBy(Pageable pageable);

    @Override
    <S extends Price> Mono<S> save(S entity);

    @Override
    Flux<Price> findAll();

    @Override
    Mono<Price> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PriceRepositoryInternal {
    <S extends Price> Mono<S> save(S entity);

    Flux<Price> findAllBy(Pageable pageable);

    Flux<Price> findAll();

    Mono<Price> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Price> findAllBy(Pageable pageable, Criteria criteria);

}

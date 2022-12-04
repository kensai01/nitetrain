package com.nitetrain.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.nitetrain.domain.BeginnerWorkout;
import com.nitetrain.repository.rowmapper.BeginnerWorkoutRowMapper;
import com.nitetrain.repository.rowmapper.WorkoutRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the BeginnerWorkout entity.
 */
@SuppressWarnings("unused")
class BeginnerWorkoutRepositoryInternalImpl
    extends SimpleR2dbcRepository<BeginnerWorkout, Long>
    implements BeginnerWorkoutRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkoutRowMapper workoutMapper;
    private final BeginnerWorkoutRowMapper beginnerworkoutMapper;

    private static final Table entityTable = Table.aliased("beginner_workout", EntityManager.ENTITY_ALIAS);
    private static final Table workoutTable = Table.aliased("workout", "workout");

    public BeginnerWorkoutRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkoutRowMapper workoutMapper,
        BeginnerWorkoutRowMapper beginnerworkoutMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(BeginnerWorkout.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workoutMapper = workoutMapper;
        this.beginnerworkoutMapper = beginnerworkoutMapper;
    }

    @Override
    public Flux<BeginnerWorkout> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<BeginnerWorkout> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BeginnerWorkoutSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkoutSqlHelper.getColumns(workoutTable, "workout"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workoutTable)
            .on(Column.create("workout_id", entityTable))
            .equals(Column.create("id", workoutTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, BeginnerWorkout.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<BeginnerWorkout> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<BeginnerWorkout> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private BeginnerWorkout process(Row row, RowMetadata metadata) {
        BeginnerWorkout entity = beginnerworkoutMapper.apply(row, "e");
        entity.setWorkout(workoutMapper.apply(row, "workout"));
        return entity;
    }

    @Override
    public <S extends BeginnerWorkout> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

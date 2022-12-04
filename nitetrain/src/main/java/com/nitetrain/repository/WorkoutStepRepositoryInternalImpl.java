package com.nitetrain.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.nitetrain.domain.WorkoutStep;
import com.nitetrain.repository.rowmapper.BeginnerWorkoutRowMapper;
import com.nitetrain.repository.rowmapper.IntermediateWorkoutRowMapper;
import com.nitetrain.repository.rowmapper.WorkoutRowMapper;
import com.nitetrain.repository.rowmapper.WorkoutStepRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the WorkoutStep entity.
 */
@SuppressWarnings("unused")
class WorkoutStepRepositoryInternalImpl extends SimpleR2dbcRepository<WorkoutStep, Long> implements WorkoutStepRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkoutRowMapper workoutMapper;
    private final BeginnerWorkoutRowMapper beginnerworkoutMapper;
    private final IntermediateWorkoutRowMapper intermediateworkoutMapper;
    private final WorkoutStepRowMapper workoutstepMapper;

    private static final Table entityTable = Table.aliased("workout_step", EntityManager.ENTITY_ALIAS);
    private static final Table workoutTable = Table.aliased("workout", "workout");
    private static final Table beginnerWorkoutTable = Table.aliased("beginner_workout", "beginnerWorkout");
    private static final Table intermediateWorkoutTable = Table.aliased("intermediate_workout", "intermediateWorkout");

    public WorkoutStepRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkoutRowMapper workoutMapper,
        BeginnerWorkoutRowMapper beginnerworkoutMapper,
        IntermediateWorkoutRowMapper intermediateworkoutMapper,
        WorkoutStepRowMapper workoutstepMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(WorkoutStep.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workoutMapper = workoutMapper;
        this.beginnerworkoutMapper = beginnerworkoutMapper;
        this.intermediateworkoutMapper = intermediateworkoutMapper;
        this.workoutstepMapper = workoutstepMapper;
    }

    @Override
    public Flux<WorkoutStep> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<WorkoutStep> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WorkoutStepSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkoutSqlHelper.getColumns(workoutTable, "workout"));
        columns.addAll(BeginnerWorkoutSqlHelper.getColumns(beginnerWorkoutTable, "beginnerWorkout"));
        columns.addAll(IntermediateWorkoutSqlHelper.getColumns(intermediateWorkoutTable, "intermediateWorkout"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workoutTable)
            .on(Column.create("workout_id", entityTable))
            .equals(Column.create("id", workoutTable))
            .leftOuterJoin(beginnerWorkoutTable)
            .on(Column.create("beginner_workout_id", entityTable))
            .equals(Column.create("id", beginnerWorkoutTable))
            .leftOuterJoin(intermediateWorkoutTable)
            .on(Column.create("intermediate_workout_id", entityTable))
            .equals(Column.create("id", intermediateWorkoutTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, WorkoutStep.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<WorkoutStep> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<WorkoutStep> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private WorkoutStep process(Row row, RowMetadata metadata) {
        WorkoutStep entity = workoutstepMapper.apply(row, "e");
        entity.setWorkout(workoutMapper.apply(row, "workout"));
        entity.setBeginnerWorkout(beginnerworkoutMapper.apply(row, "beginnerWorkout"));
        entity.setIntermediateWorkout(intermediateworkoutMapper.apply(row, "intermediateWorkout"));
        return entity;
    }

    @Override
    public <S extends WorkoutStep> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

package com.nitetrain.repository.rowmapper;

import com.nitetrain.domain.WorkoutStep;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link WorkoutStep}, with proper type conversions.
 */
@Service
public class WorkoutStepRowMapper implements BiFunction<Row, String, WorkoutStep> {

    private final ColumnConverter converter;

    public WorkoutStepRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link WorkoutStep} stored in the database.
     */
    @Override
    public WorkoutStep apply(Row row, String prefix) {
        WorkoutStep entity = new WorkoutStep();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setStepNumber(converter.fromRow(row, prefix + "_step_number", Integer.class));
        entity.setWorkoutId(converter.fromRow(row, prefix + "_workout_id", Long.class));
        entity.setBeginnerWorkoutId(converter.fromRow(row, prefix + "_beginner_workout_id", Long.class));
        entity.setIntermediateWorkoutId(converter.fromRow(row, prefix + "_intermediate_workout_id", Long.class));
        return entity;
    }
}

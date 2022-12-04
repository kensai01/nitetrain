package com.nitetrain.repository.rowmapper;

import com.nitetrain.domain.BeginnerWorkout;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link BeginnerWorkout}, with proper type conversions.
 */
@Service
public class BeginnerWorkoutRowMapper implements BiFunction<Row, String, BeginnerWorkout> {

    private final ColumnConverter converter;

    public BeginnerWorkoutRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link BeginnerWorkout} stored in the database.
     */
    @Override
    public BeginnerWorkout apply(Row row, String prefix) {
        BeginnerWorkout entity = new BeginnerWorkout();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setWorkoutId(converter.fromRow(row, prefix + "_workout_id", Long.class));
        return entity;
    }
}

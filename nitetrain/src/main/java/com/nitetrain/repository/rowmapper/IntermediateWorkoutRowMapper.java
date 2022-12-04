package com.nitetrain.repository.rowmapper;

import com.nitetrain.domain.IntermediateWorkout;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IntermediateWorkout}, with proper type conversions.
 */
@Service
public class IntermediateWorkoutRowMapper implements BiFunction<Row, String, IntermediateWorkout> {

    private final ColumnConverter converter;

    public IntermediateWorkoutRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IntermediateWorkout} stored in the database.
     */
    @Override
    public IntermediateWorkout apply(Row row, String prefix) {
        IntermediateWorkout entity = new IntermediateWorkout();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setWorkoutId(converter.fromRow(row, prefix + "_workout_id", Long.class));
        return entity;
    }
}

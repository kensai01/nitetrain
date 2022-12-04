package com.nitetrain.repository.rowmapper;

import com.nitetrain.domain.Workout;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Workout}, with proper type conversions.
 */
@Service
public class WorkoutRowMapper implements BiFunction<Row, String, Workout> {

    private final ColumnConverter converter;

    public WorkoutRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Workout} stored in the database.
     */
    @Override
    public Workout apply(Row row, String prefix) {
        Workout entity = new Workout();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setTime(converter.fromRow(row, prefix + "_time", Integer.class));
        entity.setVideoId(converter.fromRow(row, prefix + "_video_id", String.class));
        entity.setScaling(converter.fromRow(row, prefix + "_scaling", String.class));
        return entity;
    }
}

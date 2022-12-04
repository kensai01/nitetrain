package com.nitetrain.repository.rowmapper;

import com.nitetrain.domain.Price;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Price}, with proper type conversions.
 */
@Service
public class PriceRowMapper implements BiFunction<Row, String, Price> {

    private final ColumnConverter converter;

    public PriceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Price} stored in the database.
     */
    @Override
    public Price apply(Row row, String prefix) {
        Price entity = new Price();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setLocation(converter.fromRow(row, prefix + "_location", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCost(converter.fromRow(row, prefix + "_cost", BigDecimal.class));
        return entity;
    }
}

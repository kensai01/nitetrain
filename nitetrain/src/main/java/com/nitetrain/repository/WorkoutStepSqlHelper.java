package com.nitetrain.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class WorkoutStepSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("step_number", table, columnPrefix + "_step_number"));

        columns.add(Column.aliased("workout_id", table, columnPrefix + "_workout_id"));
        columns.add(Column.aliased("beginner_workout_id", table, columnPrefix + "_beginner_workout_id"));
        columns.add(Column.aliased("intermediate_workout_id", table, columnPrefix + "_intermediate_workout_id"));
        return columns;
    }
}

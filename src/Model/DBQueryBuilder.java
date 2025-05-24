package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import Lib.ArrayBuilder;

import java.sql.*;
import java.util.*;

public class DBQueryBuilder {
    DBConnection db = new DBConnection();
    private Connection con = db.getConnection();

    private StringBuilder query, select, table, where, order_by, group_by, join;
    public DBQueryBuilder() {
        query = new StringBuilder();
        select = new StringBuilder();
        table = new StringBuilder();
        where = new StringBuilder();
        order_by = new StringBuilder();
        group_by = new StringBuilder();
        join = new StringBuilder();
    }

    // SELECT
    public DBQueryBuilder select(String... columns) {
        select.append("SELECT ");
        if (columns.length == 0) {
            select.append("* ");
        } else {
            select.append(String.join(", ", columns)).append(" ");
        }
        return this;
    }

    public DBQueryBuilder from(String tableName) {
        table.append("FROM ").append(tableName).append(" ");
        return this;
    }

    // LEFT JOIN
    public DBQueryBuilder leftJoin(String tableName, String condition) {
        join.append("LEFT JOIN ").append(tableName).append(" ON ").append(condition).append(" ");
        return this;
    }

    // WHERE 
    public DBQueryBuilder where(ArrayBuilder[] conditions) {
        if (conditions.length == 0) return this;

        where.append("WHERE ");
        StringJoiner whereClause = new StringJoiner(" AND ");
        for (ArrayBuilder condition : conditions) {
            whereClause.add(condition.key + " = '" + condition.value + "'");
        }

        where.append(whereClause.toString()).append(" ");
        return this;
    }

    // INSERT
    public DBQueryBuilder insert(String table, Map<String, Object> data) {
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            columns.add(entry.getKey());
            Object value = entry.getValue();
            values.add(value instanceof String ? "'" + value + "'" : value.toString());
        }

        query.append("INSERT INTO ").append(table)
             .append(" (").append(columns.toString()).append(")")
             .append(" VALUES (").append(values.toString()).append(")");
        return this;
    }

    // UPDATE
    public DBQueryBuilder update(String table, Map<String, Object> data) {
        StringJoiner setClause = new StringJoiner(", ");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            setClause.add(entry.getKey() + " = " + (value instanceof String ? "'" + value + "'" : value.toString()));
        }

        query.append("UPDATE ").append(table)
             .append(" SET ").append(setClause.toString()).append(" ");
        return this;
    }

    // DELETE
    public DBQueryBuilder delete(String table) {
        query.append("DELETE FROM ").append(table).append(" ").append(where);
        return this;
    }

    // GET all records
    public List<Map<String, Object>> get() {
        try {
            String finalQuery = buildQuery();
            PreparedStatement stmt = con.prepareStatement(finalQuery);
            ResultSet rs = stmt.executeQuery();

            List<Map<String, Object>> results = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // GET first record
    public Map<String, Object> first() {
        try {
            String finalQuery = buildQuery() + "LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(finalQuery);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                ResultSetMetaData meta = rs.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                return row;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Build final query
    private String buildQuery() {
        if (query.length() > 0) {
            return query.toString(); // For insert, update, delete
        }

        StringBuilder fullQuery = new StringBuilder();
        fullQuery.append(select)
                 .append(table)
                 .append(join)
                 .append(where)
                 .append(group_by)
                 .append(order_by);
        return fullQuery.toString();
    }
}

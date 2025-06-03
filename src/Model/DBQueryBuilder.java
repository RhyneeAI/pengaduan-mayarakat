package Model;

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
    
    public DBQueryBuilder orderBy(String field, String orderType) {
        order_by.append("ORDER BY ").append(field).append(" ").append(orderType);
        return this;
    }

    // INSERT
    public DBQueryBuilder insert(String table, ArrayBuilder[] data) {
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");

        for (ArrayBuilder item : data) {
            columns.add(item.key);
            values.add("'" + item.value + "'");
        }

        query.append("INSERT INTO ").append(table)
             .append(" (").append(columns.toString()).append(")")
             .append(" VALUES (").append(values.toString()).append(")");
        return this;
    }

    // UPDATE
    public DBQueryBuilder update(String table, ArrayBuilder[] data) {
        StringJoiner setClause = new StringJoiner(", ");

        for (ArrayBuilder item : data) {
            setClause.add(item.key + " = '" + item.value + "'");
        }

        query.append("UPDATE ").append(table)
             .append(" SET ").append(setClause.toString()).append(" ").append(where);
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

    private void reset() {
        query.setLength(0);
        select.setLength(0);
        table.setLength(0);
        where.setLength(0);
        order_by.setLength(0);
        group_by.setLength(0);
        join.setLength(0);
    }

    // Build final query
    public String buildQuery() {
        String result;
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
        result = fullQuery.toString();
        reset();
        return result;
    }
}

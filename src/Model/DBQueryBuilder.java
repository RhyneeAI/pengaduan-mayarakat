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

public class DBQueryBuilder {
    DBConnection db = new DBConnection();
    private Connection con = db.getConnection();
    
    private StringBuilder query, select, table, where, order_by, group_by;
    public DBQueryBuilder() {
        query = new StringBuilder();
        select = new StringBuilder();
        table = new StringBuilder();
        where = new StringBuilder();
        order_by = new StringBuilder();
        group_by = new StringBuilder();
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
        query.append("DELETE FROM ").append(table).append(" ");
        return this;
    }

    public ResultSet result() 
    {
        try {
            PreparedStatement ps = con.prepareStatement(query.toString().trim());
            return ps.executeQuery(); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat menjalankan SELECT: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return null;
        }
    }
    
    public boolean execute() {
        try {
            PreparedStatement ps = con.prepareStatement(query.toString().trim());
            int result = ps.executeUpdate();

            if (result > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Operasi gagal : Tidak ada data yang terpengaruh.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat menjalankan query: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return false;
        }
    }

}
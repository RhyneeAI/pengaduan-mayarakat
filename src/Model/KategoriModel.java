package Model;

import Helper.MessageHelper;
import Lib.*;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

public class KategoriModel {
    DBConnection db = new DBConnection();
    private final Connection con = db.getConnection();
    
    public KategoriModel() {
        if(con == null) {
            MessageHelper.showError("Tidak terhubung ke database");
        }
    }

    public List<Map<String, Object>> getKategori() {
        DBQueryBuilder qb = new DBQueryBuilder();

        return qb.select("*")
                 .from("complaint_categories")
                 .get(); 
    }

    // Ambil pengaduan berdasarkan id (single row)
    public Map<String, Object> getKategoriById(String id) {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] condition = {
            new ArrayBuilder("id", id),
        };

        qb.select("*")
          .from("complaint_categories")
          .where(condition);

        return qb.first();
    }
    
    public boolean insert(List<ArrayBuilder> data) {
        try {
            DBQueryBuilder builder = new DBQueryBuilder();
            builder.insert("complaint_categories", data.toArray(new ArrayBuilder[0]));

            String sql = builder.buildQuery(); 
            Statement stmt = con.createStatement();
            int result = stmt.executeUpdate(sql);

            return result > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat login: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return false;
        }
    }
    
    public boolean delete(String id) {
        try {
            ArrayBuilder[] condition = {
                new ArrayBuilder("id", id)
            };

            DBQueryBuilder builder = new DBQueryBuilder();
            builder.where(condition).delete("complaint_categories");

            String sql = builder.buildQuery(); 
            Statement stmt = con.createStatement();
            int result = stmt.executeUpdate(sql);

            return result > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat login: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return false;
        }
    }
    
    public boolean update(String id, List<ArrayBuilder> data) {
        try {
            ArrayBuilder[] condition = {
                new ArrayBuilder("id", id)
            };

            DBQueryBuilder builder = new DBQueryBuilder();
            builder.where(condition).update("complaint_categories", data.toArray(new ArrayBuilder[0]));

            String sql = builder.buildQuery(); 
            // System.out.print(sql);
            Statement stmt = con.createStatement();
            int result = stmt.executeUpdate(sql);

            return result > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat login: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return false;
        }
    }
}

package Model;

import Helper.MessageHelper;
import Lib.*;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

public class PengaduanModel {
    DBConnection db = new DBConnection();
    private final Connection con = db.getConnection();
    
    public PengaduanModel() {
        if(con == null) {
            MessageHelper.showError("Tidak terhubung ke database");
        }
    }

    public List<Map<String, Object>> getKategoriPengaduan() {
        DBQueryBuilder qb = new DBQueryBuilder();

        return qb.select("category_name")
                 .from("complaint_categories")
                 .get(); 
    }
    
    // Ambil semua pengaduan
    public List<Map<String, Object>> getPengaduan() {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] condition = {
            new ArrayBuilder("c.user_id", Session.get("id"))
        };

        qb.select("c.id, c.date, c.title, cc.category_name, c.status")
          .from("complaints as c")
//          .where(condition)
          .leftJoin("complaint_categories as cc", "c.category_id = cc.id")
          .orderBy("id", "DESC");

        return qb.get();
    }

    // Ambil pengaduan berdasarkan id (single row)
    public Map<String, Object> getPengaduanById(String id) {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] condition = {
            new ArrayBuilder("c.id", id),
//            new ArrayBuilder("c.user_id", Session.get("id"))
        };

        qb.select("c.*, cc.category_name")
          .from("complaints as c")
          .leftJoin("complaint_categories as cc", "c.category_id = cc.id")
          .where(condition);

        return qb.first();
    }
    
    public Map<String, Object> getPengaduanCategory(String category_name) {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] condition = {
            new ArrayBuilder("category_name", category_name),
        };

        qb.select("id")
          .from("complaint_categories")
          .where(condition);

        return qb.first();
    } 
    
    public boolean insert(List<ArrayBuilder> data) {
        try {
            DBQueryBuilder builder = new DBQueryBuilder();
            builder.insert("complaints", data.toArray(new ArrayBuilder[0]));

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
            DBQueryBuilder builder = new DBQueryBuilder();
            ArrayBuilder[] condition = {
                new ArrayBuilder("id", id),
            };
            builder.where(condition).update("complaints", data.toArray(new ArrayBuilder[0]));

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
            builder.where(condition).delete("complaints");

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
}

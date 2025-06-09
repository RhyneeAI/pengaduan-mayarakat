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
    public List<Map<String, Object>> getPengaduan(ArrayBuilder[] where, ArrayBuilder orderBy) {
        DBQueryBuilder qb = new DBQueryBuilder();

        qb.select("c.*, cc.category_name, u.name")
          .from("complaints as c")
          .whereDate(where)
          .leftJoin("complaint_categories as cc", "c.category_id = cc.id")
          .leftJoin("users as u", "c.user_id = u.id");
        
        if("newest".equals(orderBy.key)) {
            qb.orderByCustom("CASE WHEN c.status = 'New' THEN 0 ELSE 1 END, c.status DESC");
        } else {
            qb.orderBy(orderBy.key, orderBy.value);
        }
        
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
    
    public Map<String, Object> getTanggapanById(String complaintId) {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] condition = {
            new ArrayBuilder("complaint_id", complaintId),
//            new ArrayBuilder("c.user_id", Session.get("id"))
        };

        qb.select("*")
          .from("complaint_completions")
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
    
    public List<Map<String, Object>> getPengaduanByUserId() {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] condition = {
            new ArrayBuilder("c.user_id", Session.get("id"))
        };

        qb.select("c.*, cc.category_name")
          .from("complaints as c")
          .leftJoin("complaint_categories as cc", "c.category_id = cc.id")
          .where(condition);

        return qb.get();
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
    
    public boolean finishPengaduan(String idPengaduan, List<ArrayBuilder> data, String finishType) {
        try {
            boolean result = false;
            if("Insert".equals(finishType)) {
                System.out.println("Atas");
                // 1. Insert ke complaint_completions
                DBQueryBuilder builderTanggapan = new DBQueryBuilder();
                builderTanggapan.insert("complaint_completions", data.toArray(new ArrayBuilder[0]));
                String sqlInsert = builderTanggapan.buildQuery();
                Statement stmt = con.createStatement();
                int resultInsert = stmt.executeUpdate(sqlInsert);

                // 2. Update status di complaints
                ArrayBuilder[] condition = { new ArrayBuilder("id", idPengaduan) };
                ArrayBuilder[] updateData = { new ArrayBuilder("status", "Finished") };
                DBQueryBuilder builderUpdate = new DBQueryBuilder();
                builderUpdate.where(condition).update("complaints", updateData);
                String sqlUpdate = builderUpdate.buildQuery();
                int resultUpdate = stmt.executeUpdate(sqlUpdate);
                
                result = resultInsert > 0 && resultUpdate > 0;
            } else {
                System.out.println("Bawah");
                ArrayBuilder[] condition = { new ArrayBuilder("complaint_id", idPengaduan) };
                DBQueryBuilder builderUpdate = new DBQueryBuilder();
                builderUpdate.where(condition).update("complaint_completions", data.toArray(new ArrayBuilder[0]));
                String sqlUpdate = builderUpdate.buildQuery();
                Statement stmt = con.createStatement();
                int resultUpdate = stmt.executeUpdate(sqlUpdate);
                
                result = resultUpdate > 0;
            }
            
            return result;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat menyelesaikan pengaduan: " + e.getMessage() +
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

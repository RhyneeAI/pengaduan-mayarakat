package Model;

import Helper.MessageHelper;
import Lib.*;
import java.sql.*;

public class DashboardModel {
    DBConnection db = new DBConnection();
    private final Connection con = db.getConnection();
    
    public DashboardModel() {
        if(con == null) {
            MessageHelper.showError("Tidak terhubung ke database");
        }
    }

    public String countPengaduanByStatus(String status, String status2) {
        DBQueryBuilder qb = new DBQueryBuilder();

        ArrayBuilder[] condition = { new ArrayBuilder("status", status) };
        ArrayBuilder[] optionalCondition = { new ArrayBuilder("status", status2) };

        String sql = qb.select("COUNT(*) as total")
                       .from("complaints")
                       .where(condition)
                       .orWhere(optionalCondition)
                       .buildQuery();

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String countPengaduanByAgeCategory(String ageCategory) {
        DBQueryBuilder qb = new DBQueryBuilder();

        ArrayBuilder[] condition = { new ArrayBuilder("u.age_category", ageCategory) };

        String sql = qb.select("COUNT(*) as total")
                       .from("complaints as c")
                       .leftJoin("users as u", "u.id = c.user_id")
                       .where(condition)
                       .buildQuery();
        
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }
}

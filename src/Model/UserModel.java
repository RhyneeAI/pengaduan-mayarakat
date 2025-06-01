package Model;

import Helper.MessageHelper;
import Lib.*;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

public class UserModel {
    DBConnection db = new DBConnection();
    private final Connection con = db.getConnection();
    
    public UserModel() {
        if(con == null) {
            MessageHelper.showError("Tidak terhubung ke database");
        }
    }

    public boolean login(String username, String plainPassword) {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] conditions = {
            new ArrayBuilder("username", username)
        };

        String query = qb.select("*").from("users").where(conditions).buildQuery();

        try (PreparedStatement ps = con.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");

                    if (BCrypt.checkpw(plainPassword, hashedPassword)) {
                        Map<String, String> userData = new HashMap<>();
                        userData.put("id", rs.getString("id"));
                        userData.put("nik", rs.getString("nik"));
                        userData.put("name", rs.getString("name"));
                        userData.put("birth_date", rs.getString("birth_date"));
                        userData.put("age_category", rs.getString("age_category"));
                        userData.put("gender", rs.getString("gender"));
                        userData.put("phone_number", rs.getString("phone_number"));
                        userData.put("address", rs.getString("address"));
                        userData.put("access_level", rs.getString("access_level"));
                        
                        Session.setUserData(userData);

                        return true;
                    }
                }

                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat login: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return false;
        }
    }
    
    public boolean register(List<ArrayBuilder> data) {
        try {
            DBQueryBuilder builder = new DBQueryBuilder();
            builder.insert("users", data.toArray(new ArrayBuilder[0]));

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

    public Map<String, Object> getUserByNIK(String nik) {
        DBQueryBuilder qb = new DBQueryBuilder();
        
        ArrayBuilder[] condition = {
            new ArrayBuilder("nik", nik)
        };

        return qb.select("*")
                 .from("users")
                 .where(condition)
                 .first();
    }
    
    public Map<String, Object> getUserByUsername(String username) {
        DBQueryBuilder qb = new DBQueryBuilder();
        ArrayBuilder[] condition = {
            new ArrayBuilder("username", username)
        };

        return qb.select("*")
                 .from("users")
                 .where(condition)
                 .first();
    }

}

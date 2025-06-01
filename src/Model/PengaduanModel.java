package Model;

import Helper.MessageHelper;
import Lib.*;
import java.sql.*;
import java.util.*;

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

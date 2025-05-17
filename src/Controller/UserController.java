package Controller;

import Model.UserModel;  
import java.util.HashMap;
import java.util.Map;
import View.RegisterForm;
import Model.DBQueryBuilder;
import Helper.PasswordHelper;
import java.text.SimpleDateFormat;

public class UserController {
    public static void main(String[] args) {
        RegisterForm f = new RegisterForm();
        f.setVisible(true);
    }
    
    public Map<String, Object> login(String[] userData) {
        Map<String, Object> result = new HashMap<>();
        String[] fieldNames = {
            "Username", "Password"
        };

        for (int i = 0; i < userData.length; i++) {
            if (userData[i] == null || userData[i].trim().isEmpty()) {
                result.put("status", false);
                result.put("message", fieldNames[i] + " harus diisi.");
                return result;
            }
        }
        
        String username = userData[0];
        String password = userData[1];
        
        PasswordHelper pw = new PasswordHelper();
        String generatePW = pw.hashPassword(password); 
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", userData[0]);
        userMap.put("password", generatePW);
        
        UserModel user = new UserModel();
        user.login(username, password);
     
        return null;
    }

    public Map<String, Object> store(String[] userData) {
        Map<String, Object> result = new HashMap<>();

        String[] fieldNames = {
            "NIK", "Nama", "Tanggal Lahir", "Kategori Umur", 
            "Jenis Kelamin", "Nomor Telepon", "Alamat", "Password"
        };

        for (int i = 0; i < userData.length; i++) {
            if (userData[i] == null || userData[i].trim().isEmpty()) {
                result.put("status", false);
                result.put("message", fieldNames[i] + " harus diisi.");
                return result;
            }
            
            System.out.println(userData[i]);
        }
        
        if (new UserModel().isNikExists(userData[0])) {
            result.put("status", false);
            result.put("message", "NIK sudah terdaftar.");
            return result;
        }
        
        PasswordHelper pw = new PasswordHelper();
        String generate_pw = pw.hashPassword(userData[8]); 

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("nik", userData[0]);
        userMap.put("name", userData[1]);
        userMap.put("birthDate", userData[2]);
        userMap.put("ageCategory", userData[3]);
        userMap.put("gender", userData[4]);
        userMap.put("phoneNumber", userData[5]);
        userMap.put("address", userData[6]);
        userMap.put("username", userData[7]);
        userMap.put("password", generate_pw);
        
//        DBQueryBuilder qb = new DBQueryBuilder();
//        String query = qb.insertInto("users", userData).build();

        UserModel user = new UserModel.Builder()
            .nik(userData[0])
            .name(userData[1])
            .birthDate(userData[2])
            .ageCategory(userData[3])
            .gender(userData[4])
            .phoneNumber(userData[5])
            .address(userData[6])
            .username(userData[7])
            .password(generate_pw)
            .build();

        boolean isInserted = user.insertUser(); 
        if (isInserted) {
            result.put("status", true);
            result.put("message", "Berhasil mendaftar!");
        } else {
            result.put("status", false);
            result.put("message", "Gagal menyimpan ke database.");
        }

        return result;
    }
}

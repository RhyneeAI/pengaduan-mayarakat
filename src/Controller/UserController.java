package Controller;

import Model.UserModel;  
import java.util.HashMap;
import java.util.Map;
import View.RegisterForm;
import Model.DBQueryBuilder;
import Helper.PasswordHelper;
import Helper.ValidationHelper;
import Lib.ArrayBuilder;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserController {
    public static void main(String[] args) {
        RegisterForm f = new RegisterForm();
        f.setVisible(true);
    }
    
    public Map<String, Object> login(List<ArrayBuilder> userdata) {
        Map<String, String> userMap = new HashMap<>();
        for (ArrayBuilder ab : userdata) {
            userMap.put(ab.key, ab.value);
        }

        List<String> requiredFields = List.of("username", "password");
        Map<String, Object> validationResult = ValidationHelper.validateFields(userMap, requiredFields);

        if (validationResult != null) {
            return validationResult; 
        }

        String username = userMap.get("username");
        String password = userMap.get("password");

        UserModel user = new UserModel();
        Boolean r = user.login(username, password);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", true);
        result.put("message", r ? "Login berhasil! Selamat datang, " + username : "Login Gagal! Username atau password salah.");
        return result;
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

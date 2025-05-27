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
        result.put("status", r);
        result.put("message", r ? "Login berhasil! Selamat datang, " + username : "Login Gagal! Username atau password salah.");
        return result;
    }

    public Map<String, Object> register(List<ArrayBuilder> userdata) {
        Map<String, String> userMap = new HashMap<>();
        for (ArrayBuilder ab : userdata) {
            userMap.put(ab.key, ab.value);
        }

        // Hash password
        for (ArrayBuilder ab : userdata) {
            if (ab.key.equals("password")) {
                ab.value = PasswordHelper.hashPassword(ab.value);
            }
            userMap.put(ab.key, ab.value);
        }

        // Cek field wajib
        List<String> requiredFields = List.of("nik", "name", "birth_date", "age_category", "gender", "phone_number", "address", "username", "password");
        Map<String, Object> validationResult = ValidationHelper.validateFields(userMap, requiredFields);

        if (validationResult != null) {
            return validationResult;
        }
        
        UserModel userModel = new UserModel();
        if (userModel.getUserByNIK(userMap.get("nik")) != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", false);
            result.put("message", "NIK sudah terdaftar.");
            return result;
        }

        // ✅ Cek username sudah ada atau belum
        if (userModel.getUserByUsername(userMap.get("username")) != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", false);
            result.put("message", "Username sudah digunakan.");
            return result;
        }

        Boolean r = userModel.register(userdata);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Registrasi berhasil!" : "Registrasi gagal.");
        return result;
    }

}

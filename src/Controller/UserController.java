package Controller;

import Helper.PasswordHelper;
import Helper.ValidationHelper;
import Lib.ArrayBuilder;
import Lib.Session;
import Model.UserModel;
import View.RegisterForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, Object> result = new HashMap<>();
        
        UserModel user = new UserModel();
        Map<String, Object> checkUser = user.getUserByUsername(username);
        if (!Boolean.parseBoolean(checkUser.get("is_active").toString())) {
            result.put("status", false);
            result.put("message", "Login Gagal! Akun anda belum diaktivasi oleh Petugas.");
            return result;
        }
    
        Boolean r = user.login(username, password);
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
    
    public Map<String, Object> logout() {
        Session.clear();

        Map<String, Object> result = new HashMap<>();
        result.put("status", true);
        result.put("message", "Logout berhasil!");
        return result;
    }
    
    public List<Map<String, Object>> getUserList() {
        UserModel um = new UserModel();
        return um.getUserList();
    }
    
    public Map<String, Object> activateUser(String id, String isActive) {
        UserModel um = new UserModel();
        Boolean r = um.activateUser(id, isActive);
        
        boolean aktif = isActive.equals("1") || isActive.equalsIgnoreCase("true") || isActive.equals("Y");

        Map<String, Object> result = new HashMap<>();
        result.put("status", aktif);
        result.put("message", aktif ? "Pengguna berhasil di Non Aktifkan!" : "Pengguna berhasil di Aktifkan!");
        return result;
    }
}

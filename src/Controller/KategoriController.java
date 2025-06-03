package Controller;

import Helper.PasswordHelper;
import Helper.ValidationHelper;
import Lib.ArrayBuilder;
import Model.PengaduanModel;
import Model.UserModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PengaduanController {
    PengaduanModel pm = new PengaduanModel();
    public String[] getKategoriOptions() {
        List<Map<String, Object>> kategoriList = pm.getKategoriPengaduan();

        String[] kategoriOptions = new String[kategoriList.size()];
        for (int i = 0; i < kategoriList.size(); i++) {
            kategoriOptions[i] = kategoriList.get(i).get("category_name").toString();
        }

        return kategoriOptions;
    }
    
    public List<Map<String, Object>> getPengaduan() {
        return pm.getPengaduan();
    }
    
    public Map<String, Object> getPengaduanById(String id) {
        return pm.getPengaduanById(id);
    }
    
    public Map<String, Object> setPengaduan(List<ArrayBuilder> pengaduanData) {
        Map<String, String> pengaduanMap = new HashMap<>();
        for (ArrayBuilder ab : pengaduanData) {
            if (ab.key.equals("category")) {
                Map<String, Object> categoryData = pm.getPengaduanCategory(ab.value);
                if (categoryData != null && categoryData.containsKey("id")) {
                    ab.key = "category_id";
                    ab.value = categoryData.get("id").toString();
                }
            }
            pengaduanMap.put(ab.key, ab.value);
        }

        // Cek field wajib
        List<String> requiredFields = List.of("title", "date", "body");
        Map<String, Object> validationResult = ValidationHelper.validateFields(pengaduanMap, requiredFields);

        if (validationResult != null) {
            return validationResult;
        }
        
        Boolean r = pm.insert(pengaduanData);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Pengaduan baru berhasil ditambahkan, Silahkan tunggu sampai pengaduan diperiksa!" : "Pengaduan gagal ditambahkan.");
        return result;
    }
}

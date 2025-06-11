package Controller;

import Helper.ValidationHelper;
import Lib.ArrayBuilder;
import Model.PengaduanModel;
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
    
    public List<Map<String, Object>> getPengaduan(ArrayBuilder[] where, ArrayBuilder orderBy) {
        return pm.getPengaduan(where, orderBy);
    }
    
    public Map<String, Object> getPengaduanById(String idPengaduan) {
        return pm.getPengaduanById(idPengaduan);
    }
    
    public Map<String, Object> getTanggapanById(String idPengaduan) {
        return pm.getTanggapanById(idPengaduan);
    }
    
    public List<Map<String, Object>> getKomenPengaduanById(String idPengaduan) {
        return pm.getKomenPengaduanById(idPengaduan);
    }
    
    public List<Map<String, Object>> getPengaduanByUserId(ArrayBuilder[] condition) {
        return pm.getPengaduanByUserId(condition);
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

    public Map<String, Object> updatePengaduan(String id, List<ArrayBuilder> pengaduanData) {
        for (ArrayBuilder ab : pengaduanData) {
            if (ab.key.equals("category")) {
                Map<String, Object> categoryData = pm.getPengaduanCategory(ab.value);
                if (categoryData != null && categoryData.containsKey("id")) {
                    ab.key = "category_id";
                    ab.value = categoryData.get("id").toString();
                }
                break;
            }
            continue;
        }

        Boolean r = pm.update(id, pengaduanData);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Pengaduan berhasil diupdate, Silahkan tunggu sampai pengaduan diperiksa!" : "Pengaduan gagal ditambahkan.");
        return result;
    }
    
    public Map<String, Object> finishPengaduan(String idPengaduan, List<ArrayBuilder> pengaduanData, String finishType) {
        Boolean r = pm.finishPengaduan(idPengaduan, pengaduanData, finishType);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        if("Inser".equals(finishType)) {
            result.put("message", r ? "Pengaduan berhasil diselesaikan!" : "Pengaduan gagal diselesaikan.");
        } else {
            result.put("message", r ? "Tanggapan Pengaduan berhasil diperbarui!" : "Tanggapan Pengaduan gagal diperbarui.");
        }
        return result;
    }

    public Map<String, Object> deletePengaduan(String id) {
        Boolean r = pm.delete(id);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Pengaduan berhasil dihapus!" : "Pengaduan gagal dihapus.");
        return result;
    }
    
    public Map<String, Object> addKomentar(List<ArrayBuilder> data) {
        Boolean r = pm.addKomentar(data);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Komentar berhasil ditambahkan!" : "Komentar gagal ditambahkan.");
        return result;
    } 
}

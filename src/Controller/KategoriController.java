package Controller;

import Helper.ValidationHelper;
import Lib.ArrayBuilder;
import Model.KategoriModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KategoriController {
    KategoriModel km = new KategoriModel();
    
    public List<Map<String, Object>> getKategori() {
        return km.getKategori();
    }
    
    public Map<String, Object> getKategoriById(String id) {
        return km.getKategoriById(id);
    }
    
    public Map<String, Object> deleteKategori(String id) {
        Boolean r = km.delete(id);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Kategori Pengaduan berhasil dihapus!" : "Kategori Pengaduan gagal dihapus.");
        return result;
    }
    
    public Map<String, Object> setKategori(List<ArrayBuilder> kategoriData) {
        Map<String, String> kategoriMap = new HashMap<>();
        for (ArrayBuilder ab : kategoriData) {
            kategoriMap.put(ab.key, ab.value);
        }

        // Cek field wajib
        List<String> requiredFields = List.of("category_name");
        Map<String, Object> validationResult = ValidationHelper.validateFields(kategoriMap, requiredFields);

        if (validationResult != null) {
            return validationResult;
        }
        
        Boolean r = km.insert(kategoriData);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Kategori Pengaduan baru berhasil ditambahkan!" : "Kategori Pengaduan gagal ditambahkan.");
        return result;
    }
    
    public Map<String, Object> updateKategori(String id, List<ArrayBuilder> kategoriData) {
        Boolean r = km.update(id, kategoriData);

        Map<String, Object> result = new HashMap<>();
        result.put("status", r);
        result.put("message", r ? "Kategori Pengaduan berhasil diupdate!" : "Kategori Pengaduan gagal diupdate!");
        return result;
    }
}

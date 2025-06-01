package Controller;

import Model.PengaduanModel;
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
}

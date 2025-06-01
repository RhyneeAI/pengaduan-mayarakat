package Controller;

import Model.PengaduanModel;
import java.util.List;
import java.util.Map;

public class PengaduanController {
    public String[] getKategoriOptions() {
        PengaduanModel pm = new PengaduanModel();
        List<Map<String, Object>> kategoriList = pm.getKategoriPengaduan();

        String[] kategoriOptions = new String[kategoriList.size()];
        for (int i = 0; i < kategoriList.size(); i++) {
            kategoriOptions[i] = kategoriList.get(i).get("category_name").toString();
        }

        return kategoriOptions;
    }

}

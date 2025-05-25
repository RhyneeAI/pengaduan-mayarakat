package Helper;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ValidationHelper {

    public static Map<String, Object> validateFields(Map<String, String> data, List<String> requiredKeys) {
        Map<String, Object> result = new HashMap<>();

        for (String key : requiredKeys) {
            String value = data.get(key);
            if (value == null || value.trim().isEmpty()) {
                String fieldName = key.substring(0, 1).toUpperCase() + key.substring(1);
                result.put("status", false);
                result.put("message", fieldName + " harus diisi.");
                return result;
            }
        }

        return null;
    }
}

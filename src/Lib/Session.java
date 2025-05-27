package Lib;


import java.util.HashMap;
import java.util.Map;

public class Session {
    private static final Map<String, String> sessionData = new HashMap<>();

    public static void setUserData(Map<String, String> userData) {
        sessionData.clear();
        sessionData.putAll(userData);
    }

    public static String get(String key) {
        return sessionData.get(key);
    }

    public static Map<String, String> getAll() {
        return sessionData;
    }

    public static void clear() {
        sessionData.clear();
    }
}

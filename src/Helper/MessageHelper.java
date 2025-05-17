package Helper;

import java.util.Map;
import javax.swing.JOptionPane;

public class MessageHelper {
    public static void showMessageFromResult(Map<String, Object> result) {
        boolean status = (boolean) result.get("status");
        String message = (String) result.get("message");

        if (status) {
            showSuccess(message);
        } else {
            showWarning(message);
        }
    }
    
    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showDelete(String message) {
        int option = JOptionPane.showConfirmDialog(
            null,
            message,
            "Konfirmasi Penghapusan",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        return option == JOptionPane.YES_OPTION;
    }
}

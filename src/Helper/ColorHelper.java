package Helper;
import java.awt.Color;

public class ColorHelper {

    public static final Color PRIMARY   = new Color(0x0d6efd); // Bootstrap Primary
    public static final Color SECONDARY = new Color(0x6c757d); // Bootstrap Secondary
    public static final Color INFO      = new Color(0x0dcaf0); // Bootstrap Info
    public static final Color SUCCESS   = new Color(0x198754); // Bootstrap Success
    public static final Color DANGER    = new Color(0xdc3545); // Bootstrap Danger
    public static final Color WARNING   = new Color(0xffc107); // Bootstrap Warning
    public static final Color LIGHT     = new Color(0xf8f9fa); // Bootstrap Light
    public static final Color DARK      = new Color(0x212529); // Bootstrap Dark

    // Optional: text color agar kontras (white/dark)
    public static Color getContrastColor(Color bgColor) {
        double luminance = (0.299 * bgColor.getRed() + 0.587 * bgColor.getGreen() + 0.114 * bgColor.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }
}

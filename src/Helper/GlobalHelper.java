
package Helper;

public class GlobalHelper {
    public static void isEmpty(String text) {
        if(text.equals("")) {
            MessageHelper.showWarning("Input");
        }
    }
}

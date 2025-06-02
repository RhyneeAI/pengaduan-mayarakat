package Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeHelper {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Date getFirstDayOfMonth() {
        Date date = new Date();
        return new Date(date.getYear(), date.getMonth(), 1);
    }

    public static Date getDateNow() {
        return new Date();
    }

    public static String setYMD(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    public static String humanizeDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        return sdf.format(date);
    }
}

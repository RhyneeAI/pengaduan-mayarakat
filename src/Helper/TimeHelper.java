package Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {
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
}

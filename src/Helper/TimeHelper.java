package Helper;

//import java.text.DateFormat;
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
    
    public static int getYearsElapsed(String dateString) {
        Date date = parseDate(dateString);
        if (date == null) return -1; // Return -1 jika parsing gagal

        Calendar start = Calendar.getInstance();
        start.setTime(date);

        Calendar now = Calendar.getInstance();

        int yearsElapsed = now.get(Calendar.YEAR) - start.get(Calendar.YEAR);

        // Pastikan bulan & tanggal sudah lewat dalam tahun ini, jika belum, kurangi 1 tahun
        if (now.get(Calendar.MONTH) < start.get(Calendar.MONTH) ||
            (now.get(Calendar.MONTH) == start.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) < start.get(Calendar.DAY_OF_MONTH))) {
            yearsElapsed--;
        }

        return yearsElapsed;
    }
}

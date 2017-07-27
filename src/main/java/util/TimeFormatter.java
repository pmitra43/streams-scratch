package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeFormatter {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
    public static Date stringToDate(String timeString) {
        Date utcDate = null;
        try {
            Date istDate = simpleDateFormat.parse(timeString);
            SimpleDateFormat sdfOut = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy", Locale.ENGLISH);
            sdfOut.setTimeZone(TimeZone.getTimeZone("UTC"));
            String utcFormat = sdfOut.format(istDate);
            utcDate = sdfOut.parse(utcFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcDate;
    }
}

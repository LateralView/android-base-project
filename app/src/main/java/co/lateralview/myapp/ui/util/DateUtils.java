package co.lateralview.myapp.ui.util;


import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import co.lateralview.myapp.R;

public final class DateUtils {
    public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";

    public static final long SECOND = 1;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = DAY * 365;
    public static final long DEFAULT_NOW_CRITERIA = MINUTE * 5;
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_SLASH = "yyyy/MM/dd";
    public static final String MM_DD_YY_SLASH = "MM/dd/yy";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd'T'HH:mm";
    public static final String YYYYMMDD = "yyyyMMdd";

    private DateUtils() {
    }

    public enum DateOrderType {
        BEFORE,
        AFTER,
        EQUALS
    }

    public static Date getDateFromString(String dateString) {
        return getDateFromDateFormat(dateString, YYYY_MM_DD_HH_MM);
    }

    public static Date getDateFromDateFormat(String dateString, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getTimeFromTimeFormat(String timeString) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        Date date = null;
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = formatter.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getTime(Date date, Long minutes) {
        return getTime(new Date(date.getTime() + (minutes * 60 * 1000)));
    }

    public static String getTime(Date date) {
        return getFormatString(date, "HH:mm");
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static long getUnixTimestampFromTimeFormat(String timeString) {
        return getTimeFromTimeFormat(timeString).getTime() / 1000L;
    }

    public static long getUnixTimestampFromDateFormat(String dateString, String dateFormat) {
        return getDateFromDateFormat(dateString, dateFormat).getTime() / 1000L;
    }

    public static long getUnixTimestampFromDateFormat(String dateString) {
        return getUnixTimestampFromDateFormat(dateString, YYYY_MM_DD_HH_MM_SS);
    }

    public static long nowUnixTimestamp() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static String aproxTimeStringSince(Date since, Context context) {
        long time = Math.abs(nowUnixTimestamp() - (since.getTime() / 1000L));

        if (time < MINUTE) {
            return String.valueOf(time) + " " + context.getString(R.string.dateUtils_sec);
        } else if (time < HOUR) {
            return String.valueOf(time / MINUTE) + " " + context.getString(R.string.dateUtils_min);
        } else if (time < DAY) {
            return String.valueOf(time / HOUR) + " " + context.getString(R.string.dateUtils_h);
        } else if (time < MONTH) {
            return String.valueOf(time / DAY) + " " + context.getString(R.string.dateUtils_days);
        } else if (time < YEAR) {
            return String.valueOf(time / MONTH) + " " + context.getString(
                R.string.dateUtils_months);
        } else {
            return String.valueOf(time / YEAR) + " " + context.getString(R.string.dateUtils_years);
        }
    }

    public static DateOrderType getDateOrderType(String dateFormat1, String dateFormat2) {
        long unixTimestamp1 = getUnixTimestampFromDateFormat(dateFormat1);
        long unixTimestamp2 = getUnixTimestampFromDateFormat(dateFormat2);

        if (unixTimestamp1 > unixTimestamp2) {
            return DateOrderType.AFTER;
        }

        if (unixTimestamp1 < unixTimestamp2) {
            return DateOrderType.BEFORE;
        }

        return DateOrderType.EQUALS;
    }

    public static long getRemainingHoursFromMillis(long millis) {
        return TimeUnit.MILLISECONDS.toHours(millis);
    }

    public static long getRemainingMinutesFromMillis(long millis) {
        return TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1);
    }

    public static long getRemainingSecondsFromMillis(long millis) {
        return TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1);
    }

    public static String getRemainingHoursStringFromMillis(long millis) {
        return formatTimeString(String.valueOf(getRemainingHoursFromMillis(millis)));
    }

    public static String getRemainingMinutesStringFromMillis(long millis) {
        return formatTimeString(String.valueOf(getRemainingMinutesFromMillis(millis)));
    }

    public static String getRemainingSecondsStringFromMillis(long millis) {
        return formatTimeString(String.valueOf(getRemainingSecondsFromMillis(millis)));
    }

    public static String formatTimeString(String time) {
        if (time.length() < 2) {
            time = "0" + time;
        }

        return time;
    }

    public static boolean isDateValid(String dateToValidate, String dateFromat) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
            || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(
                Calendar.DATE))) {
            diff--;
        }

        return diff;
    }

    public static int getDiffDays(Date first, Date last) {
        return (int) ((last.getTime() - first.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatUnixtimestamp(int unixtimestamp, String format) {
        Date date = new Date(unixtimestamp * 1000L); // *1000 is to convert seconds to milliseconds

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(date);
    }

    public static Date dateFromUnixtimestamp(int unixtimestamp) {
        return new Date(unixtimestamp * 1000L);
    }

    public static boolean hasSameDate(Date date1, Date date2) {
        Calendar a = getCalendar(date1);
        Calendar b = getCalendar(date2);

        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
            && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR);
    }

    public static String getDay(Date date) {
        return String.valueOf(getCalendar(date).get(Calendar.DAY_OF_MONTH));
    }

    public static String getYear(Date date) {
        return String.valueOf(getCalendar(date).get(Calendar.YEAR));
    }

    public static String getWeekDay(Date date) {
        return getFormatString(date, "EEE");
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();

        Calendar calendar = getCalendar(startdate);
        calendar.add(Calendar.DATE, 1);

        Date nextDate = calendar.getTime();

        while (nextDate.before(enddate) && !hasSameDate(nextDate, enddate)) {
            dates.add(nextDate);

            calendar.add(Calendar.DATE, 1);

            nextDate = calendar.getTime();
        }

        return dates;
    }

    public static String getMonth(Date date) {
        return getFormatString(date, "MMMM");
    }

    public static String getNowFormatString(String dateFormat) {
        return getFormatString(now(), dateFormat);
    }

    public static String getFormatString(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static String getUTCFormatString(Date date, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date);
    }

    public static String getMonthYearString(Date date) {
        return getMonth(date) + " " + getYear(date);
    }

    public static Long getTranscurredMinutes(Date date) {
        return getRemainingMinutesFromMillis(new Date().getTime() - date.getTime());
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }
}

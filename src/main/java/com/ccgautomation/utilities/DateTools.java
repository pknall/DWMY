package com.ccgautomation.utilities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.ccgautomation.utilities.Configuration.MIDNIGHT_THRESHHOLD;

public class DateTools {

    private final SimpleDateFormat datePattern;
    private static final String DEFAULT_DATE_PATTERN = "MM/dd/yyyy hh:mm:ss";

    /**
     * Automatically assigns the SimpleDateFormat pattern to "MM/dd/yyyy" and Time Zone to
     * "Greenwich Mean Time".
     */
    public DateTools() {
        this(DEFAULT_DATE_PATTERN, TimeZone.getTimeZone("Greenwich Mean Time"));
    }

    /**
     * Automatically assigns the Time Zone to "Greenwich Mean Time".
     * @param datePattern SimpleDateFormat pattern for String to Date conversion.
     */
    public DateTools(String datePattern) {
        this(datePattern, TimeZone.getTimeZone("Greenwich Mean Time"));
    }

    /**
     * Automatically assigns the SimpleDateFormat pattern to "MM/dd/yyyy".
     *
     * @param timeZone Assigns the time zone to base calculations in.
     */
    public DateTools(TimeZone timeZone) {
        this(DEFAULT_DATE_PATTERN, timeZone);
    }

    /**
     *
     * @param datePattern SimpleDateFormat string describing the format for Date and Time conversions.
     * @param timeZone Assigns the time zone to base calculations in.
     */
    public DateTools(String datePattern, TimeZone timeZone) {
        if (timeZone == null) throw new NullPointerException("TimeZone Object is null.");
        this.datePattern = new SimpleDateFormat(datePattern);
        this.datePattern.setTimeZone(timeZone);
    }

    /**
     * Returns the SimpleDateFormat pattern being used.
     * @return Date object assigned to the Date and Time of dateString.
     */
    public String getSimpleDateFormatPattern() {
        return datePattern.toLocalizedPattern();
    }

    /**
     *
     * @return The default date pattern.
     */
    public String getDefaultDatePattern() {
        return DEFAULT_DATE_PATTERN;
    }

    /**
     *
     * @return Display Name of the current Time Zone.
     */
    public String getTimeZoneDisplayName() {
        return datePattern.getTimeZone().getDisplayName();
    }

    /**
     *
     * @param date Date object to be converted.
     * @return Date and Time contained in the supplied Date Object formatted with the SimpleDateFormat assigned.
     */
    public String convertDateToStringWithFormat(Date date) {
        return datePattern.format(date);
    }

    /**
     *
     */
    public static String convertDateToStringWithFormat(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static Date convertStringToDateWithFormat(String dateString, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        try {
            return sdf.parse(dateString);
        }
        catch(Exception ex) {
            return null;
        }
    }


    /**
     * Converts a Date String to a Date value.  Returns ParseException on failure.
     * @param dateString Date to be converted.
     * @return Date object assigned to the date provided or null on failure.
     */
    public Date convertStringToDate(String dateString) throws ParseException {
        return datePattern.parse(dateString);
    }

    public static String removeDecimalPortionOfSeconds(String dateString) {
        int start = dateString.indexOf(".");
        if (start < 0) return dateString;

        int end = dateString.indexOf(" ", start);
        if (end < 0) return dateString;

        return dateString.substring(0, start) + dateString.substring(end);
    }

    public static boolean isTheNextDay(Date earlierDate, Date laterDate) {
        int earlierDay, laterDay;
        Calendar c = Calendar.getInstance();
        c.setTime(earlierDate);
        earlierDay = c.get(Calendar.DAY_OF_YEAR);
        c.setTime(laterDate);
        laterDay = c.get(Calendar.DAY_OF_YEAR);

        if ((laterDay - earlierDay) > 0) return true;
        if ((laterDay == 1) && (earlierDay > 354)) return true;

        return false;
    }

    public static Date getThisMidnight(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR, 0);

        return c.getTime();
    }

    public static Date incrementHour(Date date) {
        date = getThisMidnight(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 1);
        return c.getTime();
    }

    public static Date incrementDate(Date date, int value) {
        date = getThisMidnight(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, value);
        return c.getTime();
    }

    public static boolean isAtMidnight(Date date) {
        Date dateAtMidnight = getThisMidnight(date);

        if (Math.abs(date.getTime() - dateAtMidnight.getTime()) < MIDNIGHT_THRESHHOLD) return true;
        else return false;
    }

    public static Date incrementMS(Date date, Integer value) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MILLISECOND, value);

        return c.getTime();
    }

}
package com.eric.savingsmanager.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hshao on 10/04/2017.
 */

public class Utils {

    /**
     * Format a date to string with format
     *
     * @param date   date to format
     * @param format format to follow
     * @return formatted date string
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }

    /**
     * Get the diff days between two dates
     * @param start start date
     * @param end end date
     * @return days
     */
    public static int getDiffDays(Date start, Date end) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * Whether string is empty or null
     * @param str value
     * @return emptyOrNull or not
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * Format a double to xxx.xx
     * @param value a double to format
     * @return a formatted string
     */
    public static String formatDouble(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}

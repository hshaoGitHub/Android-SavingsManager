package com.eric.savingsmanager.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
     *
     * @param start start date
     * @param end   end date
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
     *
     * @param str value
     * @return emptyOrNull or not
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * Format a float to xxx.xx
     *
     * @param value a float to format
     * @return a formatted string
     */
    public static String formatFloat(float value) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(value);
    }

    /**
     * Format a float to xxx.xx
     *
     * @param str a string of float
     * @return a formatted string
     */
    public static String getFloat(String str) {
        float value;
        try {
            value = Float.valueOf(str);
        } catch (NumberFormatException ex) {
            value = 0.0f;
        }

        return formatFloat(value);
    }

    /**
     * round float to .00
     *
     * @param f float
     * @return the .00 float
     */
    public static float roundFloat(float f) {
        BigDecimal b = new BigDecimal(f);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}

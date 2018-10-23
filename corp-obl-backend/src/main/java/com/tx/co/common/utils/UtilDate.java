package com.tx.co.common.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormatSymbols;
import java.util.*;

public abstract class UtilDate {
    public static final int MILLISECOND = 14;
    public static final int SECOND = 13;
    public static final int MINUTE = 12;
    public static final int HOUR = 11;
    public static final int DAY = 5;
    public static final int MONTH = 2;
    public static final int YEAR = 1;
    public static final int INFINITE_YEAR = 3000;
    public static final int ZERO_YEAR = 1900;
    private static final int[] highDays = new int[]{1, 7};

    public UtilDate() {
    }

    public static Date encodeDateTime(int year, int month, int day, int hour, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minutes, seconds);
        Date result = calendar.getTime();
        return result;
    }

    public static Date encodeDateTime(int year, int month, int day, int hour, int minutes, int seconds, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.set(year, month - 1, day, hour, minutes, seconds);
        Date result = calendar.getTime();
        return result;
    }

    public static long dateDiff(Date date1, Date date2) {
        long result = date1.getTime() - date2.getTime();
        return result;
    }

    public static double dateDiff(Date date1, Date date2, int measureUnit) {
        long diff = dateDiff(date1, date2);
        double result = 0.0D;
        switch (measureUnit) {
            case 5:
                result = (double) diff / 8.64E7D;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            default:
                break;
            case 11:
                result = (double) diff / 3600000.0D;
                break;
            case 12:
                result = (double) diff / 60000.0D;
                break;
            case 13:
                result = (double) diff / 1000.0D;
                break;
            case 14:
                result = (double) diff;
        }

        return result;
    }

    public static Date postpone(Date date, int measureUnit, int amount) {
        return _postpone(date, measureUnit, amount, (Locale) null);
    }

    public static Date postpone(Date date, int measureUnit, int amount, Locale locale) {
        return _postpone(date, measureUnit, amount, locale);
    }

    public static Date postponeWorkingDay(Date date, int measureUnit, int amount, Long[] holidays) {
        Date result = date;

        for (int i = 0; i < amount; ++i) {
            result = postpone(result, measureUnit, 1);
            if (isWorkingDay(result, holidays)) {
                result = nextWorkingDay(result, holidays).getTime();
            }
        }

        return result;
    }

    public static boolean isWorkingDay(Date date, Long[] holidays) {
        return isWorkingDay(date.getTime(), holidays);
    }

    public static boolean isWorkingDay(long time, Long[] holidays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int var6;
        if (null != holidays) {
            Long[] var4 = holidays;
            int var5 = holidays.length;

            for (var6 = 0; var6 < var5; ++var6) {
                long d = var4[var6];
                if (time == d) {
                    return false;
                }
            }
        }

        int dayOfWeek = calendar.get(7);
        int[] var10 = highDays;
        var6 = var10.length;

        for (int var11 = 0; var11 < var6; ++var11) {
            int i = var10[var11];
            if (i == dayOfWeek) {
                return false;
            }
        }

        return true;
    }

    public static Calendar nextWorkingDay(Date date, Long[] holidays) {
        return nextWorkingDay(date.getTime(), holidays);
    }

    public static Calendar nextWorkingDay(Long time, Long[] holidays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(5, 1);

        while (!isWorkingDay(calendar.getTimeInMillis(), holidays)) {
            calendar.add(5, 1);
        }

        return calendar;
    }

    public static Calendar nextWorkingDay(Calendar calendar, Long[] holidays) {
        calendar.add(5, 1);

        while (!isWorkingDay(calendar.getTimeInMillis(), holidays)) {
            calendar.add(5, 1);
        }

        return calendar;
    }

    public static Calendar previousWorkingDay(Calendar calendar, Long[] holidays) {
        calendar.add(5, -1);

        while (!isWorkingDay(calendar.getTimeInMillis(), holidays)) {
            calendar.add(5, -1);
        }

        return calendar;
    }

    public static Long getWorkingDays(long startTime, long endTime, Long[] holidays) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime);
        return getWorkingDays(startCalendar, endCalendar, holidays);
    }

    public static Long getWorkingDays(Date startDate, Date finalDate, Long[] holidays) {
        return getWorkingDays(startDate.getTime(), finalDate.getTime(), holidays);
    }

    public static Long getWorkingDays(Calendar start, Calendar end, Long[] holidays) {
        Long result = 0L;
        int startDayOfWeek = start.get(7);
        int endDayOfWeek = end.get(7);
        if (!isWorkingDay(start.getTime(), holidays)) {
            start = nextWorkingDay(start, holidays);
        }

        if (!isWorkingDay(end.getTime(), holidays)) {
            end = previousWorkingDay(end, holidays);
        }

        Long dayDiff = Math.abs(dateDiff(start.getTime(), end.getTime()) / 86400000L);
        Long weekEnds = dayDiff / 7L;
        if (startDayOfWeek < endDayOfWeek) {
            weekEnds = weekEnds + 1L;
        }

        result = dayDiff - weekEnds * (long) highDays.length;
        return result;
    }

    public static Long[] getPeriod(int startY, int startM, int startD, int endY, int endM, int endD) {
        Calendar start = Calendar.getInstance();
        start.setMinimalDaysInFirstWeek(7);
        start.set(startY, startM - 1, startD);
        Calendar end = Calendar.getInstance();
        end.setMinimalDaysInFirstWeek(7);
        end.set(endY, endM - 1, endD);
        return getPeriod(start, end);
    }

    public static Long[] getPeriod(Date start, Date end) {
        Calendar startCal = Calendar.getInstance();
        startCal.setMinimalDaysInFirstWeek(7);
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setMinimalDaysInFirstWeek(7);
        endCal.setTime(end);
        return getPeriod(startCal, endCal);
    }

    public static Long[] getPeriod(Calendar startCal, Calendar endCal) {
        List<Long> result = new LinkedList();
        int count = 0;

        while (!startCal.getTime().after(endCal.getTime())) {
            result.add(startCal.getTimeInMillis());
            startCal.add(5, 1);
            ++count;
            if (count > 3650) {
                break;
            }
        }

        return (Long[]) result.toArray(new Long[result.size()]);
    }

    public static boolean equals(Date date1, Date date2) {
        return dateDiff(date1, date2) == 0L;
    }

    public static boolean equals(Date date1, Date date2, long tolleranceMs) {
        return dateDiff(date1, date2) <= tolleranceMs;
    }

    public static Date now() {
        return new Date();
    }

    public static Date infinite() {
        Date date = encodeDateTime(3000, 1, 1, 0, 0, 0);
        return date;
    }

    public static boolean isInfinite(Date date) {
        int year = getYear(date);
        boolean result = year >= 3000;
        return result;
    }

    public static Date zero() {
        Date date = encodeDateTime(1900, 1, 1, 0, 0, 0);
        return date;
    }

    public static boolean isZero(Date date) {
        int year = getYear(date);
        boolean result = year <= 1900;
        return result;
    }

    public static boolean isToday(Date date) {
        return _isToday(date, (Locale) null);
    }

    public static boolean isToday(Date date, Locale locale) {
        return _isToday(date, locale);
    }

    public static int getMinutes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(12);
    }

    public static int getMinutes(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(12);
    }

    public static int getMinutes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int getMinutes(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int getSeconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(13);
    }

    public static int getSeconds(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(13);
    }

    public static int getSeconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static int getSeconds(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static int getHourOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(11);
    }

    public static int getHourOfDay(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(11);
    }

    public static int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(11);
    }

    public static int getHourOfDay(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(11);
    }

    public static int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(5);
    }

    public static int getDayOfMonth(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(5);
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(7);
    }

    public static int getDayOfWeek(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(7);
    }

    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int getDayOfMonth(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int getWeekOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(4);
    }

    public static int getWeekOfMonth(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(4);
    }

    public static int getWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(4);
    }

    public static int getWeekOfMonth(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(4);
    }

    public static int getWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(3);
    }

    public static int getWeekOfYear(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(3);
    }

    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(3);
    }

    public static int getWeekOfYear(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(3);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(2) + 1;
    }

    public static int getMonth(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(2) + 1;
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static int getMonth(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static String getMonthAsString(Date date) {
        return getMonthAsString(getMonth(date));
    }

    public static String getMonthAsString(int month) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        return months[month - 1];
    }

    public static String getMonthAsString(Date date, Locale locale) {
        return getMonthAsString(getMonth(date), locale);
    }

    public static String getMonthAsString(int month, Locale locale) {
        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        String[] months = dfs.getMonths();
        return months[month - 1];
    }

    public static String getShortMonthAsString(Date date) {
        return getShortMonthAsString(getMonth(date));
    }

    public static String getShortMonthAsString(int month) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        return months[month - 1];
    }

    public static String getShortMonthAsString(Date date, Locale locale) {
        return getShortMonthAsString(getMonth(date), locale);
    }

    public static String getShortMonthAsString(int month, Locale locale) {
        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        String[] months = dfs.getShortMonths();
        return months[month - 1];
    }

    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(1);
    }

    public static int getYear(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        return calendar.get(1);
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(1);
    }

    public static int getYear(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(1);
    }

    public static Date getDate(Date date, int dayOfWeek) {
        return _getDate(date, dayOfWeek, (Locale) null);
    }

    public static Date getDate(Date date, int dayOfWeek, Locale locale) {
        return _getDate(date, dayOfWeek, locale);
    }

    public static Date getDate(int year, int weekOfYear, int dayOfWeek) {
        return _getDate(year, weekOfYear, dayOfWeek, (Locale) null);
    }

    public static Date getDate(int year, int weekOfYear, int dayOfWeek, Locale locale) {
        return _getDate(year, weekOfYear, dayOfWeek, locale);
    }

    public static int getActualMaximumDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.getActualMaximum(5);
    }

    /**
     * @param iDate:   integer date
     * @param pattern: i.e. "yyyyMMdd"
     * @return date to parse as date
     */
    public static Date getIntDateAsDate(final int iDate, final String pattern) throws Exception {
        if (iDate > 0 && StringUtils.hasText(pattern)) {
            final DateWrapper dw = new DateWrapper(iDate + "", pattern, Locale.ITALIAN);
            return dw.getDateTime();
        }
        return null;
    }

    /**
     * @param sDate:   String date
     * @param pattern: i.e. "yyyyMMdd"
     * @return date to parse as date
     */
    public static Date getStringDateAsDate(final String sDate, final String pattern) throws Exception {
        if (StringUtils.hasText(sDate) && StringUtils.hasText(pattern)) {
            final DateWrapper dw = new DateWrapper(sDate, pattern, Locale.ITALIAN);
            return dw.getDateTime();
        }
        return null;
    }

    /**
     * Convert date to int with pattern yyyyMMdd
     *
     * @param date
     * @return Int date
     */
    public static int getDateAsInt(final Date date, final String pattern) throws Exception {
        final String sDate = formatDate(date, pattern);
        if (StringUtils.hasText(sDate)) {
            return Integer.parseInt(sDate);
        }
        return -1;
    }


    /**
     * Converting Date to String
     *
     * @param dateToFormat
     * @param outputFormat
     * @return String date
     */
    public static String formatDate(Date dateToFormat, String outputFormat) throws Exception {
        final DateWrapper date = new DateWrapper(dateToFormat);
        final String result = date.toString(outputFormat);

        return result;
    }

    private static Date _postpone(Date date, int measureUnit, int amount, Locale locale) {
        Calendar calendar = null != locale ? Calendar.getInstance(locale) : Calendar.getInstance();
        calendar.setTime(date);
        switch (measureUnit) {
            case 1:
                calendar.add(1, amount);
                break;
            case 2:
                calendar.add(2, amount);
            case 3:
            case 4:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            default:
                break;
            case 5:
                calendar.add(5, amount);
                break;
            case 11:
                calendar.add(11, amount);
                break;
            case 12:
                calendar.add(12, amount);
                break;
            case 13:
                calendar.add(13, amount);
                break;
            case 14:
                calendar.add(14, amount);
        }

        return calendar.getTime();
    }

    private static boolean _isToday(Date date, Locale locale) {
        Calendar calendar = null != locale ? Calendar.getInstance(locale) : Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        int year = calendar.get(1);
        int month = calendar.get(2);
        int day = calendar.get(5);
        Calendar today = null != locale ? Calendar.getInstance(locale) : Calendar.getInstance();
        today.setTime(now());
        int tyear = today.get(1);
        int tmonth = today.get(2);
        int tday = today.get(5);
        boolean result = tyear == year && tmonth == month && tday == day;
        return result;
    }

    private static Date _getDate(Date date, int dayOfWeek, Locale locale) {
        Calendar calendar = null != locale ? Calendar.getInstance(locale) : Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        int weekOfYear = calendar.get(3);
        int year = calendar.get(1);
        int numWeeks = calendar.getWeeksInWeekYear();
        if (weekOfYear == numWeeks && calendar.get(2) == 11) {
            return _getDate(year + 1, 0, dayOfWeek, locale);
        } else {
            return weekOfYear == numWeeks ? _getDate(year, 0, dayOfWeek, locale) : _getDate(year, weekOfYear, dayOfWeek, locale);
        }
    }

    private static Date _getDate(int year, int weekOfYear, int dayOfWeek, Locale locale) {
        Calendar calendar = null != locale ? Calendar.getInstance(locale) : Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setWeekDate(year, weekOfYear, dayOfWeek);
        return calendar.getTime();
    }
}
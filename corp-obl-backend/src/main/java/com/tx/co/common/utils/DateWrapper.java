package com.tx.co.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to wrap a date
 * @author rfratti
 */
public class DateWrapper {
    public static final int STYLE_SHORT = 3;
    public static final int STYLE_MEDIUM = 2;
    public static final int STYLE_LONG = 1;
    public static final String[] PATTERNS = new String[]{"yyyyMMdd", "EEE, d MMM yyyy HH:mm:ss Z", "EEE, d MMM yyyy HH:mm:ss", "EEE, d-MMM-yyyy HH:mm:ss z", "EEE, d-MMM-yyyy HH:mm:ss", "EEE, d MM yyyy HH:mm:ss Z", "EEE, d MM yyyy HH:mm:ss", "EEE, d-MM-yyyy HH:mm:ss z", "EEE, d-MM-yyyy HH:mm:ss", "EEE, dd MMM yyyy HH:mm:ss Z", "EEE, dd MMM yyyy HH:mm:ss", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH:mm:ss", "EEE, dd MM yyyy HH:mm:ss Z", "EEE, dd MM yyyy HH:mm:ss", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss"};
    public static final String DATEFORMAT_DEFAULT;
    public static final String DATEFORMAT_GENERAL;
    public static final String TIMEFORMAT_DEFAULT = "hh:mm:ss";
    private int _year;
    private int _month;
    private int _day;
    private int _hour;
    private int _minutes;
    private int _seconds;
    private Calendar _calendar;

    public DateWrapper() {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        this.init();
    }

    public DateWrapper(Date date) {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        this.init(date);
    }

    public DateWrapper(Date date, TimeZone timeZone) {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        this.init(date, timeZone);
    }

    public DateWrapper(long dateTime) {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        Date dt = new Date(dateTime);
        this.init(dt);
    }

    public DateWrapper(String date, String pattern, Locale locale) throws Exception {
        this(date, pattern, locale, (TimeZone)null);
    }

    public DateWrapper(String date, String pattern, Locale locale, TimeZone zone) throws Exception {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
        if (null != zone) {
            format.setTimeZone(zone);
        }

        Date dt;
        try {
            dt = format.parse(date);
        } catch (ParseException var8) {
            new Date(0L);
            throw new Exception("Date Parse exception: " + var8.getMessage());
        }

        if (null != zone) {
            this.init(dt, zone);
        } else {
            this.init(dt);
        }

    }

    public DateWrapper(String date, int dateStyle) throws Exception {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        DateFormat format = DateFormat.getDateInstance(dateStyle);

        Date dt;
        try {
            dt = format.parse(date);
        } catch (ParseException var6) {
            new Date(0L);
            throw new Exception("Date Parse exception: " + var6.getMessage());
        }

        this.init(dt);
    }

    public DateWrapper(String date, int dateStyle, TimeZone zone) throws Exception {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        DateFormat format = DateFormat.getDateInstance(dateStyle);
        if (null != zone) {
            format.setTimeZone(zone);
        }

        Date dt;
        try {
            dt = format.parse(date);
        } catch (ParseException var7) {
            new Date(0L);
            throw new Exception("Date Parse exception: " + var7.getMessage());
        }

        if (null != zone) {
            this.init(dt, zone);
        } else {
            this.init(dt);
        }

    }

    public DateWrapper(String date, int dateStyle, Locale locale) throws Exception {
        this(date, dateStyle, locale, (TimeZone)null);
    }

    public DateWrapper(String date, int dateStyle, Locale locale, TimeZone zone) throws Exception {
        this._year = 0;
        this._month = 0;
        this._day = 0;
        this._hour = 0;
        this._minutes = 0;
        this._seconds = 0;
        this._calendar = null;
        DateFormat format = DateFormat.getDateInstance(dateStyle, locale);
        if (null != zone) {
            format.setTimeZone(zone);
        }

        Date dt;
        try {
            dt = format.parse(date);
        } catch (ParseException var8) {
            new Date(0L);
            throw new Exception("Date Parse exception: " + var8.getMessage());
        }

        if (null != zone) {
            this.init(dt, zone);
        } else {
            this.init(dt);
        }

    }

    public Calendar getCalendar() {
        return this._calendar;
    }

    public Date getDateTime() {
        return this._calendar.getTime();
    }

    public void setTimeZone(TimeZone timeZone) {
        this.init(timeZone);
    }

    public void setDateTime(Date value) {
        this.init(value);
    }

    public void setDateTime(String date, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date dt = format.parse(date);
        this.init(dt);
    }

    public void setDateTime(String date, String pattern, Locale locale) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
        Date dt = format.parse(date);
        this.init(dt);
    }

    public void setDateTime(String date, int dateFormat, Locale locale) throws ParseException {
        DateFormat format = DateFormat.getDateInstance(dateFormat, locale);
        Date dt = format.parse(date);
        this.init(dt);
    }

    public void setDateTime(String date, int dateFormat) throws ParseException {
        DateFormat format = DateFormat.getDateInstance(dateFormat);
        Date dt = format.parse(date);
        this.init(dt);
    }

    public int getYear() {
        return this._year;
    }

    public void setYear(int value) {
        this._calendar.set(1, value);
        this._year = value;
    }

    public String getMonthAsString() {
        return UtilDate.getMonthAsString(this._month);
    }

    public String getMonthShortAsString() {
        return UtilDate.getShortMonthAsString(this._month);
    }

    public int getMonth() {
        return this._month;
    }

    public void setMonth(int value) {
        int month = value - 1;
        this._calendar.set(2, month);
        this._month = month;
    }

    public int getDay() {
        return this._day;
    }

    public void setDay(int value) {
        this._calendar.set(5, value);
        this._day = value;
    }

    public int getHour() {
        return this._hour;
    }

    public void setHour(int value) {
        this._calendar.set(11, value);
        this._hour = value;
    }

    public int getMinute() {
        return this._minutes;
    }

    public void setMinute(int value) {
        this._calendar.set(12, value);
        this._minutes = value;
    }

    public int getSecond() {
        return this._seconds;
    }

    public void setSecond(int value) {
        this._calendar.set(13, value);
        this._seconds = value;
    }

    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        f.setTimeZone(this._calendar.getTimeZone());
        return f.format(this._calendar.getTime());
    }

    public String toString(TimeZone timeZone) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        f.setTimeZone(timeZone);
        return f.format(this._calendar.getTime());
    }

    public String toString(String dateFormat) {
        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
        f.setTimeZone(this._calendar.getTimeZone());
        return f.format(this._calendar.getTime());
    }

    public String toString(String dateFormat, TimeZone timeZone) {
        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
        f.setTimeZone(timeZone);
        return f.format(this._calendar.getTime());
    }

    public String toString(String dateFormat, Locale locale) {
        SimpleDateFormat f = new SimpleDateFormat(dateFormat, locale);
        f.setTimeZone(this._calendar.getTimeZone());
        return f.format(this._calendar.getTime());
    }

    public String toString(String dateFormat, Locale locale, TimeZone timeZone) {
        SimpleDateFormat f = new SimpleDateFormat(dateFormat, locale);
        f.setTimeZone(timeZone);
        return f.format(this._calendar.getTime());
    }

    public String toString(Locale locale) {
        int style = 3;
        return this.toString(style, locale);
    }

    public String toString(int style, Locale locale) {
        DateFormat f = DateFormat.getDateInstance(style, locale);
        f.setTimeZone(this._calendar.getTimeZone());
        return f.format(this._calendar.getTime());
    }

    public String toString(int style, Locale locale, TimeZone timeZone) {
        DateFormat f = DateFormat.getDateInstance(style, locale);
        f.setTimeZone(timeZone);
        return f.format(this._calendar.getTime());
    }

    public long getTime() {
        return this._calendar.getTimeInMillis();
    }

    public void set(int year, int month, int day) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
    }

    private void init() {
        if (null == this._calendar) {
            this._calendar = Calendar.getInstance();
        }

        this.init(this._calendar);
    }

    private void init(Date date) {
        if (null == this._calendar) {
            this._calendar = Calendar.getInstance();
        }

        this._calendar.setTime(date);
        this._calendar.setTimeZone(TimeZone.getDefault());
        this.init(this._calendar);
    }

    private void init(TimeZone timeZone) {
        if (null == this._calendar) {
            this._calendar = Calendar.getInstance();
        }

        this._calendar.setTimeZone(timeZone);
        this.init(this._calendar);
    }

    private void init(Date date, TimeZone timeZone) {
        if (null == this._calendar) {
            this._calendar = Calendar.getInstance();
        }

        this._calendar.setTime(date);
        this._calendar.setTimeZone(timeZone);
        this.init(this._calendar);
    }

    private void init(Calendar calendar) {
        this._year = calendar.get(1);
        this._month = calendar.get(2) + 1;
        this._day = calendar.get(5);
        this._hour = calendar.get(11);
        this._minutes = calendar.get(12);
        this._seconds = calendar.get(13);
    }

    public static DateWrapper parse(String date, Locale locale) {
        String[] var2 = PATTERNS;
        int var3 = var2.length;
        int var4 = 0;

        while(var4 < var3) {
            String pattern = var2[var4];

            try {
                DateWrapper dt = new DateWrapper(date, pattern, locale);
                return dt;
            } catch (Throwable var9) {
                ++var4;
            }
        }

        String clear = date.trim().replace("GMT", "").replace(", ", " ").replace("-", "");
        String[] var11 = PATTERNS;
        var4 = var11.length;
        int var12 = 0;

        while(var12 < var4) {
            String pattern = var11[var12];

            try {
                DateWrapper dt = new DateWrapper(clear, pattern, locale);
                return dt;
            } catch (Throwable var8) {
                ++var12;
            }
        }

        return null;
    }

    public static String getPatternFullNumeric(Locale locale) {
        DateFormat f = DateFormat.getDateInstance(3, locale);
        SimpleDateFormat sdf = (SimpleDateFormat)f;
        String pattern = sdf.toPattern().replaceAll("\\byy\\b", "yyyy").replaceAll("\\bM\\b", "MM");
        return pattern;
    }

    static {
        DATEFORMAT_DEFAULT = PATTERNS[0];
        DATEFORMAT_GENERAL = PATTERNS[1];
    }
}

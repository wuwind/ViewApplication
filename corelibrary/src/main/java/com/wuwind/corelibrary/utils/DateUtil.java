package com.wuwind.corelibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * yyyy-MM-dd
     */
    public static String PATTERN_1 = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String PATTERN_2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm
     */
    public static String PATTERN_3 = "yyyy-MM-dd HH:mm";

    /**
     * yyyyMMddHHmmss
     */
    public static String PATTERN_4 = "yyyyMMddHHmmss";
    /**
     * yyyy
     */
    public static String PATTERN_5 = "yyyy";
    /**
     * yyyy.MM.dd
     */
    public static String PATTERN_6 = "yyyy.MM.dd";

    /**
     * 把long型时间转换成指定字符串格式
     *
     * @param time
     * @param pattern
     *            如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String convertLongTime2Str(long time, String pattern) {
        // yyyy-MM-dd HH:mm:ss
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return format.format(new Date(time));
    }

    /**
     * 把Date型时间转换成指定字符串格式
     *
     * @param time
     * @param pattern
     *            如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String convertDate2Str(Date time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = time;
        String str = sdf.format(date);
        return str;
    }

    /**
     * 字符串时间转为long型
     *
     * @param strTime
     * @param pattert
     *            传入字符串时间对应的格式如：2010-1-1 10:10:10 格式为 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long convertStrTime2Long(String strTime, String pattert) {
        SimpleDateFormat format = new SimpleDateFormat(pattert,
                Locale.getDefault());
        try {
            return format.parse(strTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据输入日期，判断是否为“今天”、“昨天”、“明天”或输入日期
     *
     * @param inputDate
     * @return “今天”、“昨天”、“明天”或输入日期
     */
    public static String getToCompareDate(long inputDate) {
        String str = "";
        long oneDay = 86400000;
        String dateStr = convertLongTime2Str(inputDate, PATTERN_1);
        if (dateStr.equals(convertLongTime2Str(System.currentTimeMillis(),
                PATTERN_1))) {
            str = "今天";
        } else if (dateStr.equals(convertLongTime2Str(
                System.currentTimeMillis() - oneDay, PATTERN_1))) {
            str = "昨天";
        } else if (dateStr.equals(convertLongTime2Str(
                System.currentTimeMillis() + oneDay, PATTERN_1))) {
            str = "明天";
        } else {
            str = dateStr;
        }
        return str;
    }

    /**
     * 获取当前日期
     *
     */
    public static String[] getNowDate() {
        int y, m, d, h, mi, s;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1;
        d = cal.get(Calendar.DATE);
        h = cal.get(Calendar.HOUR_OF_DAY);
        mi = cal.get(Calendar.MINUTE);
        s = cal.get(Calendar.SECOND);
        String[] time = { y + "", m + "", d + "" };
        return time;
    }

    /**
     * 获取今日日期
     *
     * @param pattern
     *            格式
     * @return
     */
    public static String getTodayTime(String pattern) {
        return convertLongTime2Str(System.currentTimeMillis(), pattern);
    }

    /**
     * 判断当前日期是星期几(返回String “周几”)
     *
     * @param dayTime
     * @return getWeekDay 判断结果
     */
    public static String getWeekDay(long dayTime) {
        int dayForWeek = 0;
        try {
            dayForWeek = getDayForWeek(dayTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String getWeekDay = null;
        if (dayForWeek == 1) {
            getWeekDay = "周一";
        }
        if (dayForWeek == 2) {
            getWeekDay = "周二";
        }
        if (dayForWeek == 3) {
            getWeekDay = "周三";
        }
        if (dayForWeek == 4) {
            getWeekDay = "周四";
        }
        if (dayForWeek == 5) {
            getWeekDay = "周五";
        }
        if (dayForWeek == 6) {
            getWeekDay = "周六";
        }
        if (dayForWeek == 7) {
            getWeekDay = "周日";
        }
        return getWeekDay;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param dayTime
     *            要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int getDayForWeek(long dayTime) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dayTime);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 根据当前日期得到当前周
     *
     */
    public static String[] getWeek() {
        // Calendar c = Calendar.getInstance(Locale.getDefault());
        // c.setTimeInMillis(System.currentTimeMillis());
        // // 默认时，每周第一天为星期日，需要更改一下
        // c.setFirstDayOfWeek(Calendar.MONDAY);
        // // Date current=c.getTime();
        // c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // Date first = c.getTime();
        // c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // Date last = c.getTime();
        // String weekFirst = convertDate2Str(first, PATTERN_1);
        // String weekLast = convertDate2Str(last, PATTERN_1);
        String[] weekDay = { getMondayOfThisWeek(), getSundayOfThisWeek() };
        return weekDay;
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 1);
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_1);
        return sdf.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 7);
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_1);
        return sdf.format(c.getTime());
    }

    /**
     * 取得指定月份的第一天和最后一天（yyyy-MM-dd）
     *
     * @param year
     * @param month
     * @return String
     */
    public static String[] getMonthStartAndEndStr(int year, int month) {
        Date[] dateArr = getMonthStartAndEndDate(year, month);
        String firstDay = convertDate2Str(dateArr[0], PATTERN_1);
        String lastDay = convertDate2Str(dateArr[1], PATTERN_1);
        String[] monthOfDay = { firstDay, lastDay };
        return monthOfDay;
    }

    /**
     * 得指定月份的第一天和最后一天时间（long）
     *
     * @param year
     * @param month
     * @return
     */
    public static long[] getMonthStartAndEndLong(int year, int month) {
        Date[] dateArr = getMonthStartAndEndDate(year, month);
        Date firstDate = dateArr[0];// 第一天
        Date lastDate = dateArr[1];// 最后一天
        return new long[] { firstDate.getTime(), lastDate.getTime() };
    }

    private static Date[] getMonthStartAndEndDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = cal.getTime();// 最后一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDate = cal.getTime();// 第一天
        return new Date[] { firstDate, lastDate };
    }

    /**
     * 获取当前的年份
     *
     * @return
     */
    public static int getCurYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取指定年份的第一天和最后一天的时间
     *
     * @param year
     * @return long[]{第一天时间，最后一天时间}
     */
    public static long[] getYearStartAndEndLong(int year) {
        long yearStart = convertStrTime2Long(year + "-01-01", PATTERN_1);
        long yearEnd = convertStrTime2Long((year + 1) + "-01-01", PATTERN_1);
        return new long[] { yearStart, yearEnd };
    }
}

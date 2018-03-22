package com.zero.util.date;

import com.zero.util.number.NumberUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yezhaoxing
 * @date 2017/5/11
 */
public final class DateUtil {

    public static Date strToDate(String time) {
        Date d = null;
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = e.parse(time);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }
        return d;
    }

    public static Timestamp strToTimestamp(String time) {
        Timestamp ts = null;
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
            Date d = (Date) e.parseObject(time);
            ts = new Timestamp(d.getTime());
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return ts;
    }

    public static Date getCurrentDateTime() {
        return new Date(System.currentTimeMillis());
    }

    public static Date getDateFormat(Date date, String pattern) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String dateStr = dateFormat.format(date);
        return dateFormat.parse(dateStr);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static Date format(String source, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getYear(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static Date addYear(Date date, int years) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    public static Date addMonth(Date date, int months) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date addDay(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    public static Date addHour(Date date, int hours) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    public static Date addMinute(Date date, int minutes) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public static Date addSecond(Date date, int seconds) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static String dateDiff(String startTime, String endTime, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 86400000L;
        long nh = 3600000L;
        long nm = 60000L;
        long day = 0L;
        long hour = 0L;
        long min = 0L;

        try {
            long diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            day = diff / nd;
            hour = diff % nd / nh + day * 24L;
            min = diff % nd % nh / nm + day * 24L * 60L;
            return day < 1L ? (hour < 1L ? min + "分钟" : hour + "小时") : day + "天";
        } catch (ParseException var19) {
            var19.printStackTrace();
            return null;
        }
    }

    public static long getTime(String dateTime, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date = sdf.parse(dateTime);
            return date.getTime();
        } catch (ParseException var6) {
            var6.printStackTrace();
            return 0L;
        }
    }

    public static long getDiffDays(String time1, String time2) {
        long diffDays = 0L;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date e = ft.parse(time1);
            Date date2 = ft.parse(time2);
            diffDays = e.getTime() - date2.getTime();
            diffDays = diffDays / 1000L / 60L / 60L / 24L;
        } catch (ParseException var7) {
            var7.printStackTrace();
        }

        return diffDays;
    }

    public static long getDiffMinute(String startTime, String endTime) {
        long diffMinute = 0L;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date e = ft.parse(endTime);
            Date dateStartTime = ft.parse(startTime);
            diffMinute = e.getTime() - dateStartTime.getTime();
            diffMinute = diffMinute / 1000L / 60L;
        } catch (ParseException var7) {
            var7.printStackTrace();
        }

        return diffMinute;
    }

    public static long getDiffMinute(Date startTime, Date endTime) {
        long diffMinute = startTime.getTime() - endTime.getTime();
        return diffMinute / 1000L / 60L;
    }

    public static String getCostTimeStr(Date startTime, Date endTime) {
        long diffMinute = (startTime.getTime() - endTime.getTime()) / 1000;
        return secondToDate((int) diffMinute, "mm:ss");
    }

    public static long getDiffSecond(Date startTime, Date endTime) {
        long diff = startTime.getTime() - endTime.getTime();
        return diff / 1000L;
    }

    public static boolean beforeDate(String lastDate, String nowDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d = sdf.parse(lastDate);
        Date d2 = sdf.parse(nowDate);
        return d.before(d2);
    }

    public static int getMonthDays(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        return isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
                59, 59);
        return new Date(calendar.getTimeInMillis());
    }

    public static String secondToDate(Integer totalTime, String format) {
        Date date = new Date(totalTime * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return dateFormat.format(date);
    }

    // 获取两个时间相差天数
    public static int daysBetween(Date smdate, Date bdate) {
        return DateBetween(smdate, bdate, 1000 * 3600 * 24);
    }

    // 获取两个时间中相差天数之外的小时数
    public static int hourOfDayBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();

        long between = (time2 - time1) / (1000 * 3600 * 24);
        double sub = NumberUtil.sub(NumberUtil.div(time2 - time1, 1000 * 3600 * 24), between);
        return (int) NumberUtil.mul(sub, 24);
    }

    // 获取两个时间相差小时数
    public static int HourBetween(Date smdate, Date bdate) {
        return DateBetween(smdate, bdate, 1000 * 3600);
    }

    // 获取两个时间相差秒数
    public static int secondBetween(Date smdate, Date bdate) {
        return DateBetween(smdate, bdate, 1000);
    }

    // 计算到23:59:59分相差的时间
    public static long secondToZero() {
        long current = System.currentTimeMillis();// 当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();// 今天零点零分零秒的毫秒数
        long twelve = zero + 24 * 60 * 60 * 1000 - 1;// 今天23点59分59秒的毫秒数
        return (twelve - current) / 1000;
    }

    // 获取今天yyyy-MM-dd格式时间
    public static String todayFormatter() {
        return DateUtil.format(DateUtil.getCurrentDateTime(), "yyyy-MM-dd");
    }

    public static Date todayFormatterToDate() {
        return DateUtil.strToDate(DateUtil.format(DateUtil.getCurrentDateTime(), "yyyy-MM-dd 00:00:00"));
    }

    private static int DateBetween(Date smdate, Date bdate, long div) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between = (time2 - time1) / (div);
        return Integer.parseInt(String.valueOf(between));
    }

    public static boolean isValidDate(String str) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(str);
            return str.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }
}
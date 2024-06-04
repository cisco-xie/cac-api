package com.geekuniverse.cac.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author 谢诗宏
 * @description 时间工具类
 * @date 2022/11/24
 */
public class DateUtil {

    public static final String fmt = "yyyy-MM-dd HH:mm:ss",
            fmt_day = "yyyy-MM-dd",
            fmt_month = "yyyy-MM",
            fmt_recent = "MM-dd HH:mm",
            fmt_num = "yyMMdd",
            fmt_year = "yy",
            fmt_md = "MMdd",
            fmt_yyMMddHHmmss = fmt_num + "HHmmss",
            fmt_utc = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private DateUtil() {

    }

    public static String fmt(LocalDateTime date) {
        return fmt(date, fmt);
    }

    public static String fmt(LocalDateTime date, String fmt) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(fmt));
    }

    public static String fmtLocalDate(LocalDate date, String fmt) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(fmt));
    }

    public static LocalDateTime toDay(LocalDateTime date) {
        String str = date.format(DateTimeFormatter.ofPattern(fmt_day));
        return LocalDateTime.parse(str + " 00:00:00", DateTimeFormatter.ofPattern(fmt));
    }

    public static String fmtRecent(LocalDateTime date) {
        return fmt(date, fmt_recent);
    }

    public static LocalDateTime parse(String str) {
        if (str.length() == 10) str += " 00:00:00";//临时
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(fmt));
    }

    public static LocalDateTime parseDefault(String str) {
        if (str.length() == 10) str += " 00:00:00";//临时
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(fmt_utc));
    }

    public static LocalDate parseToLocalDate(String str) {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(fmt_day));
    }

    /**
     * 一周的第一天
     *
     * @param localDate 当地日期
     * @return {@link LocalDate}
     */
    public static LocalDate firstDayOfWeek(LocalDate localDate){
        return localDate.with(DayOfWeek.MONDAY);
    }

    /**
     * 一周的最后一天
     *
     * @param localDate 当地日期
     * @return {@link LocalDate}
     */
    public static LocalDate lastDayOfWeek(LocalDate localDate){
        return localDate.with(DayOfWeek.SUNDAY);
    }
    /**
     * 月的第一天
     *
     * @param localDate 当地日期
     * @return {@link LocalDate}
     */
    public static LocalDate firstDayOfMonth(LocalDate localDate){
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 月的最后一天
     *
     * @param localDate 当地日期
     * @return {@link LocalDate}
     */
    public static LocalDate lastDayOfMonth(LocalDate localDate){
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取yyMMdd格式的时间
     *
     * @param date
     * @return
     */
    public static String fmtNum(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(fmt_num));
    }
    public static String fmtDay(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(fmt_day));
    }

    /**
     * 获取yyMMddHHmmss格式的时间
     *
     * @param date
     * @return
     */
    public static String fmtYyMMddHHmmss(LocalDateTime date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(fmt_yyMMddHHmmss));
    }

    public static String fmtYear(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(fmt_year));
    }

    public static String fmtMd(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(fmt_md));
    }

    /**
     * 计算时间动态
     *
     * @param date
     * @param fmt
     * @return
     */
    public static String fmtFromNow(LocalDateTime date, String fmt) {
        if (date == null) return "";
        LocalDateTime now = LocalDateTime.now();
        //3天前
        if (now.isBefore(date)) return fmt(date, fmt);
        Duration duration = Duration.between(date, now);
        long days = duration.toDays();
        if (days >= 2) {
            return fmt(date, fmt);
        }
        //前1-3天
        if (days >= 1) {
            return days + "天前";
        }
        long hours = duration.toHours();
        if (hours > 0) {
            return hours + "小时前";
        }
        long minutes = duration.toMinutes();
        if (minutes > 10) {
            return minutes + "分钟前";
        }
        return "刚刚";
    }

    public static LocalDateTime now(String fmt) {
        return toDay(LocalDateTime.now());
    }

    public static LocalDateTime nowDay() {
        return now(fmt_day);
    }

    public static int daysBetween(LocalDateTime begin, LocalDateTime end) {
        LocalDate from = LocalDate.from(begin);
        LocalDate to = LocalDate.from(end);
        long days = to.toEpochDay() - from.toEpochDay();
        return (int) Math.abs(days);
    }

    public static int daysBetween(LocalDate begin, LocalDate end) {
        long days = end.toEpochDay() - begin.toEpochDay();
        return (int) Math.abs(days);
    }

    public static String getTime(long milis) {
        Date date = new Date(milis);
        return DateFormatUtils.format(date, "HH:mm");
    }

    /**
     * 判断时间是否在时间段内,支持跨域天的时段
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (end.before(begin)) {
            // 判断结束时间是否小于开始时间,小于则说明是跨天的时段,eg: 10:00-07:00
            // 处于开始时间之后，和结束时间之前的判断,因为会有跨天的时段,只要满足一个条件即可,所以用||
            if (date.after(begin) || date.before(end)) {
                return true;
            } else {
                return false;
            }
        } else if (begin.equals(endTime)) {
            // 开始时间等于结束时间则说明这个时段是一整天,eg: 07:00-07:00
            return true;
        } else {
            // 当天时段，eg: 07:00-19:00
            // 处于开始时间之后，和结束时间之前的判断
            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * 判断两个时间段是否有重合
     *
     * @param sourceBeginDate
     * @param sourceEndDate
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean match(LocalDate sourceBeginDate, LocalDate sourceEndDate, LocalDate beginDate, LocalDate endDate) {

        return !(beginDate.isAfter(sourceEndDate) || sourceBeginDate.isAfter(endDate));
    }


    /**
     * 判断时间是否在时间段内
     *
     * @param nowDateTime
     * @param beginDateTime
     * @param endDateTime
     * @return
     */
    public static boolean isDateTimeRange(LocalDateTime nowDateTime, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        if (nowDateTime.isAfter(beginDateTime) && nowDateTime.isBefore(endDateTime)) {
            return true;
        }
        return false;
    }


    public static boolean isDateRange(LocalDate sourceBeginDate, LocalDate sourceEndDate, LocalDate beginDate, LocalDate endDate) {
        boolean flag = false;
        if (sourceBeginDate.isAfter(beginDate.minusDays(1)) && sourceBeginDate.isBefore(endDate.plusDays(1))) {
            flag = true;
        }
        if (sourceEndDate.isAfter(beginDate.minusDays(1)) && sourceEndDate.isBefore(endDate.plusDays(1))) {
            flag = true;
        }
        /*if (isDateRange(sourceBeginDate, beginDate, endDate)) {
            return true;
        }
        if (isDateRange(sourceEndDate, beginDate, endDate)) {
            return true;
        }*/
        return flag;
    }

    /**
     * 判断时间是否在时间段内 列(闭区间)：[2021-02-04,2021-02-09]
     *
     * @param sourceDate 源时间
     * @param beginDate  开始时间段
     * @param endDate    结束时间段
     * @return
     */
    public static boolean isDateRange(LocalDate sourceDate, LocalDate beginDate, LocalDate endDate) {
        return sourceDate.isAfter(beginDate.minusDays(1)) && sourceDate.isBefore(endDate.plusDays(1));
    }

    /**
     * 通过生日获取当前年龄
     *
     * @param birthday
     * @return
     */
    public static Integer getAge(LocalDate birthday) {
        if (birthday == null) {
            return null;
        }
        return birthday.until(LocalDate.now()).getYears();
    }


    public static Date localTimeToDate(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = LocalDateTime.of(localDate, localTime).atZone(zone).toInstant();
        return Date.from(instant);
    }


    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String format(Date currentDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(currentDate);
    }

    public static LocalDate parse(String currentDate, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        try {
            return LocalDate.parse(currentDate, fmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LocalDate.now();
    }

    /**
     * 获取指定日期后的第N天
     *
     * @param localDate 日期
     * @param days      天数
     * @return 指定日期后的第N天
     */
    public static LocalDate getNextNDay(LocalDate localDate, long days) {
        return localDate.with(temporal -> temporal.plus(days, ChronoUnit.DAYS));
    }

    public static void main(String[] args) {
        /*boolean flag = isDateRange(LocalDate.parse("2020-03-26"), LocalDate.parse("2020-03-29"),
                LocalDate.parse("2020-03-17"), LocalDate.parse("2020-03-25"));
        System.out.println(flag);
        System.out.println(getNextNDay(parse("2020-03-30",fmt_day),2));*/
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusDays(2);
        System.out.println(localDateTime.getDayOfYear() - now.getDayOfYear());
        System.out.println(parse("19900101","yyyyMMdd"));
        System.out.println(parseToLocalDate("2021-08-18"));
        System.out.println(parseDefault("2021-08-18T12:23:00.000Z"));
    }

    /**
     * 转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return toLocalDateTime(localDate, null);
    }

    /**
     * 转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime toLocalDateTime(LocalDate localDate, LocalTime localTime) {
        if (localDate == null) return null;
        if (localTime == null)
            return localDate.atStartOfDay();
        else
            return localDate.atTime(localTime);
    }

    /**
     * 转LocalDateTime
     *
     * @return
     */
    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.toLocalDate();
    }

    /**
     * 转LocalTime
     *
     * @return
     */
    public static LocalTime toLocalTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.toLocalTime();
    }

    /**
     * 获取两个日期之间的所有日期
     * @param begin 开始日期
     * @param end 结束日期
     * @return 开始与结束之间的所以日期，包括起止
     */
    public static List<LocalDate> getMiddleDate(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        long length = end.toEpochDay() - begin.toEpochDay();
        for (long i = length; i >= 0; i--) {
            localDateList.add(end.minusDays(i));
        }
        return localDateList;
    }

}

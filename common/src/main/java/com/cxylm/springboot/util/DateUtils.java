package com.cxylm.springboot.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    /**
     * 获取指定时间单位开始时的毫秒时间<br/>
     * 例如：获取本月开始时的毫秒
     *
     * @param timeUnit 时间单位（请使用Calendar类的单位。可接受年、月、天。如：Calendar.MONTH）
     * @param interval 间隔（正数：向后推，负数，向前推）
     * @return 时间戳（毫秒）
     */
    public static Long getMillisOfVeryBeginning(Integer timeUnit, int interval) {
        if (timeUnit == null) {
            timeUnit = Calendar.DAY_OF_MONTH;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, interval);
        switch (timeUnit) {
            case Calendar.YEAR:
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case Calendar.MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case Calendar.DAY_OF_MONTH:
                break;
            default:
                throw new IllegalArgumentException("Illegal time unit: " + timeUnit);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间单位的开始时间戳
     *
     * @param timeUnit 时间单位
     * @return 开始时间戳
     */
    public static Long getMillisOfVeryBeginning(Integer timeUnit) {
        return getMillisOfVeryBeginning(timeUnit, 0);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔天数
     * @param timestamp1
     * @param timestamp2
     * @return
     */
    public static int differentDaysByMillisecond(long timestamp1, long timestamp2)
    {
        int days = (int) ((timestamp1 - timestamp2) / (1000*3600*24));
        return days;
    }

    /**
     * 根据当前时间获取本月第一天00:00:00时间戳
     * @param date
     * @return
     */
    public static long getFirstHMS(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND,0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        //获得当前月第一天
        long sdate = calendar.getTimeInMillis();
        return sdate;
    }

    /**
     *  根据当前时间获得本月时间最后一天 23:59:59时间戳
     * */
    public static long getLastHMS(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND,0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);

        //将当前月加1；
        calendar.add(Calendar.MONTH, 1);
        //在当前月的下一月基础上减去1毫秒
        calendar.add(Calendar.MILLISECOND, -1);
        //获得当前月最后一天
        long sdate = calendar.getTimeInMillis();
        return sdate;
    }

    /**
     * 根据当前时间获取上个月最后一天23:59:59的时间戳
     */
    public static long getAgoLastHMS(){
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MILLISECOND, -1);
        long sdate = c.getTimeInMillis();
        return sdate;
    }

    /**
     * 根据当前时间获取上个月第一天00:00:00时间戳
     */
    public static long getAgoFirstHMS(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND,0);

        long sdate = calendar.getTimeInMillis();
        return sdate;
    }

    /**
     * 返回当前时间
     * @return
     */
    public static String getCurrentDate() {
        return Date2Str(Calendar.getInstance().getTime(),"yyyy-MM-dd");
    }
    public static String Date2Str(Date date, String pattern) {
        if (date == null){
            return "";
        }
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.format(date);
    }

    /**
     * 将格式为pattern的字符串型转换为日期型返回
     * @param date
     * @param pattern
     * @return
     */
    public static Date Str2Date(String date, String pattern) {
        try {
            SimpleDateFormat ft = new SimpleDateFormat(pattern);
            Date d = ft.parse(date);
            return d;
        } catch (Exception ex) {
            return null;
        }
    }

    //算出N天后的日期
    public static String getGoDay(int i, String date) {
        // 加N天
        GregorianCalendar gc = new GregorianCalendar();
        Date daytime=Str2Date(date, "yyyy-MM-dd");
        if(daytime==null){
            daytime=new Date();
        }
        gc.setTime(daytime);
        gc.set(Calendar.DAY_OF_YEAR, gc.get(Calendar.DAY_OF_YEAR) + i);
        return Date2Str(gc.getTime(), "yyyy-MM-dd");
    }

    /**
     * 获取昨天最后的时间戳
     * @return
     */
    public static Long getLastTimeByYesterday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取昨天开始的时间戳
     * @return
     */
    public static Long getFristTimeByYesterday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        //把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        calendar.add(Calendar.DATE,-1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定时间、指定格式的时间戳
     * @param date
     * @param pattern
     * @return
     */
    public static Long getMillis(String date, String pattern){
        if(date==null||date.trim().isEmpty()){
            return null;
        }
        Calendar c=Calendar.getInstance();
        c.setTime(Str2Date(date,pattern));
        return c.getTimeInMillis();
    }

    /**
     * 获取指定时间、指定格式的后一天时间戳
     * @param date
     * @param pattern
     * @return
     */
    public static Long getMillis2(String date, String pattern){
        if(date==null||date.trim().isEmpty()){
            return null;
        }
        Calendar c=Calendar.getInstance();
        c.setTime(Str2Date(date,pattern));
        //把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        c.add(Calendar.DATE,1);
        c.add(Calendar.MILLISECOND, -1);
        return c.getTimeInMillis();
    }


    /**
     * 以下为南阳村镇银行dateutil
     */
    public static String getDateString14() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String sNowDate = sdf.format(Calendar.getInstance().getTime());
        return sNowDate;
    }

    public static String getFormatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sNowDate = sdf.format(Calendar.getInstance().getTime());
        return sNowDate;
    }

    public static String addMinutesTime(String nowTime, int minute) {
        Long nowTimeValue = Long.parseLong(nowTime);
        Long changeTimeValue = nowTimeValue + minute * 100;
        String result = changeTimeValue.toString();
        return result;
    }

    public static void main(String[] args) {
//        System.out.println(Date2Str(new Date(),"yyyy-MM"));
//        System.out.println(getGoDay(-1,getCurrentDate()));
//        System.out.println(System.currentTimeMillis());
//
//        Calendar c=Calendar.getInstance();
//        c.setTime(Str2Date("2019-03-07 10:56:00","yyyy-MM-dd HH:mm:ss"));
        Long millis = getMillis("2019-04-09", "yyyy-MM-dd");
        System.out.println(millis);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(1554739200000L);
        System.out.println(simpleDateFormat.format(date));
        Long millis2 = getMillis2("2019-04-09", "yyyy-MM-dd");
        System.out.println(millis2);

        date = new Date(millis2);
        System.out.println(simpleDateFormat.format(date));
    }
}

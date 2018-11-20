package com.adupdate.sed_report_demo.util;

import android.os.SystemClock;

import com.adupdate.sed_report_demo.helper.SharedPreferencesHelper;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_ZONE_ID = "GMT+08";

    public static final long MINUTE_TIME = 1000 * 60;
    public static final long HOUR_TIME = MINUTE_TIME * 60;
    public static final long DAY_TIME = HOUR_TIME * 24;


    /**
     * 返回默认的日期格式
     * @param time
     * @return
     */
    public static String simpleFormat(long time){
        return simpleFormat(time,DATE_TIME_PATTERN);
    }

    /**
     * 返回指定的日期格式
     * @param time
     * @param pattern
     * @return
     */
    public static String simpleFormat(long time,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
        return sdf.format(new Date(time));
    }

    /**
     * 获取开机时间
     * @return
     */
    public static long obtainBootTime(){
        return System.currentTimeMillis() - obtainElapsedRealtime();
    }
    /**
     * 获取开机时间
     * @return
     */
    public static long obtainBootTime(long currentTime){
        return currentTime - obtainElapsedRealtime();
    }
    /**
     * 获取最近一次开机到现在的运行时间
     */
    public static long obtainElapsedRealtime(){
        return SystemClock.elapsedRealtime();
    }
    /**
     * 获取当前时间
     * @return
     */
    public static long obtainCurrentTime(){
        return System.currentTimeMillis();
    }

    /**
     * 获取当日凌晨时间截
     * @return
     */
    public static long obtainBeforeDawnTime(){
        long nowTime = System.currentTimeMillis();
        return obtainBelongBeforeDawnTime(nowTime);
    }


    /**
     * 获取时间截time所在的凌晨时间截
     * @param time
     * @return
     */
    public static long obtainBelongBeforeDawnTime(long time){
        long beforeDawnTime = time - (time + 8 * HOUR_TIME) % DAY_TIME;
        return beforeDawnTime;
    }


}

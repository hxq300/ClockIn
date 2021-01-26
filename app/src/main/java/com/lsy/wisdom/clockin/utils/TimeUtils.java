package com.lsy.wisdom.clockin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by lsy on 2019/09/20
 * MODO :
 */
public class TimeUtils {

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        L.log("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getTimeYMD(Date date) {//可根据需要自行截取数据显示
        L.log("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String timeslashData(String time) {
        if (time.length() < 10) {
            return "";
        }
        if (time.length() > 10) {
            time = "" + Long.parseLong(time) / 1000;
        }
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//		int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc * 1000L));
        return times;

    }

    public static String timeData(String time) {
        if (time.length() < 10) {
            return "";
        }
        if (time.length() > 10) {
            time = "" + Long.parseLong(time) / 1000;
        }
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//		int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc * 1000L));
        return times;

    }

    public static String toDate(String time) {
        if (time.length() < 10) {
            return "";
        }
        if (time.length() > 10) {
            time = "" + Long.parseLong(time) / 1000;
        }
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//		int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc * 1000L));
        return times;

    }

}

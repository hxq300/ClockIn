package com.lsy.wisdom.clockin.utils;

import android.util.Log;

/**
 * Created by lsy on 2020/5/13
 * todo :
 */
public class L {
    public static boolean isDebug = false;

    public static void log(String title, String msg) {
        if (isDebug) {
            Log.e(title, msg);
        }
    }

    public static void log(String msg) {
        if (isDebug) {
            Log.e("打印数值：", msg);
        }

    }
}

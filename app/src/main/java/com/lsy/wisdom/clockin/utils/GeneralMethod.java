package com.lsy.wisdom.clockin.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Create by lsy on 2019/09/12
 * MODO :
 */
public class GeneralMethod {

    /**
     * 修改dialog大小
     */
    public static void XiuGaiDialog(Context context, Dialog dialog) {
        //指定操作的文件名称
        SharedPreferences share = context.getSharedPreferences("pm", MODE_PRIVATE);
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        if (width == 0) {
            width = 720;
        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width / 5 * 4;
        dialog.getWindow().setAttributes(params);
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    public static String beanToJson(Object bean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(bean);
//        System.out.println(jsonStr);
        return jsonStr;
    }
}

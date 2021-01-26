package com.lsy.wisdom.clockin.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lsy on 2020/5/13
 * todo : 单例Toast, 保证不重复显示，响应最新的toast
 */
public class ToastUtils {

    private static Toast mToast;

    /**
     * 弹出短时间的底部toast
     */
    public static void showBottomToast(Context context, String string) {
        if (mToast != null){
            mToast.cancel();
            mToast = mToast.makeText(context, string, Toast.LENGTH_SHORT);
        }else {
            mToast = mToast.makeText(context, string, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}


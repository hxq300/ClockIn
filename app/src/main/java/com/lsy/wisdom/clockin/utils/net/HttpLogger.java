package com.lsy.wisdom.clockin.utils.net;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hxq
 * on 2020/12/25
 * 网络请求 日志拦截器
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.d("json", message);
    }
}

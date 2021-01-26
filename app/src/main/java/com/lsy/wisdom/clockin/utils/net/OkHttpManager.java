package com.lsy.wisdom.clockin.utils.net;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hxq
 * on 2020/12/25
 * 处理 OkHttp网络请求
 * todo: 待完善
 */
public class OkHttpManager {
    private static volatile OkHttpManager mOkHttpManager;
    private OkHttpClient mOkHttpClient;

    private OkHttpManager() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20000, TimeUnit.SECONDS)
                .readTimeout(20000, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .build();
    }

    public static OkHttpManager getInstance() {
        if (mOkHttpManager == null) {
            synchronized (OkHttpManager.class) {
                if (mOkHttpManager == null)
                    mOkHttpManager = new OkHttpManager();
            }
        }
        return mOkHttpManager;
    }

    //
    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }


}

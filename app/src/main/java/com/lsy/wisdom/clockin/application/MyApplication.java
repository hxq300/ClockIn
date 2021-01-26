package com.lsy.wisdom.clockin.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by lsy on 2020/5/18
 * todo :
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //注意：在SDK各功能组件使用之前都需要调用。
        //SDKInitializer.initialize(getApplicationContext());，因此我们建议该方法放在Application的初始化方法中。

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }
}

package com.lsy.wisdom.clockin.request;

/**
 * Created by lsy on 2020/4/1
 * todo : 扬尘检测仪
 */
public class RJYunUrl {
    public static String RequestUrl = "http://www.0531yun.cn/wsjc";

    public static String testConnect = RequestUrl + "/app/TestConnect?";//测试接口
    public static String getDeviceData = RequestUrl + "/app/GetDeviceData?";//获取设备信息及实时数据接口

}

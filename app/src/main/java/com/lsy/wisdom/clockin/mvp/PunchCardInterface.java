package com.lsy.wisdom.clockin.mvp;

import java.sql.DataTruncation;
import java.util.List;

/**
 * Created by lsy on 2020/5/19
 * todo :
 */
public interface PunchCardInterface {

    interface Model {
        void getStatus(String id);//获取打卡状态

        //id(员工id),company_id(登录返回信息),in_address(打卡地址),remarkD(备注),longitude(经度),latitude(纬度)
        void signIn(int id, int conglomerate_id, int company_id, String in_address, String remarkD, String longitude, String latitude,String url);//签到

        //id(员工id),company_id(登录返回信息),out_address(打卡地址),remarkT(备注),longitude(经度),latitude(纬度),registration_id(打卡id,签到返回)
        void signOut(int id, int conglomerate_id, int company_id, String out_address, String remarkT, String longitude, String latitude, int registration_id,String url);//签退

    }

    interface View {
        //setData方法是为了 Activity实现view接口以后，重写这个方法就可以得到数据，为View赋值
        void setStatus(String clockstatus);//返回打卡状态

        void setInId(int registration_id);//签到返回ID

        void setSuccess();//
    }

    interface Presenter {
        void getStatus(String id);//获取打卡状态

        //id(员工id),company_id(登录返回信息),in_address(打卡地址),remarkD(备注),longitude(经度),latitude(纬度)
        void signIn(int id, int conglomerate_id, int company_id, String in_address, String remarkD, String longitude, String latitude,String url);//签到

        //id(员工id),company_id(登录返回信息),out_address(打卡地址),remarkT(备注),longitude(经度),latitude(纬度),registration_id(打卡id,签到返回)
        void signOut(int id, int conglomerate_id, int company_id, String out_address, String remarkT, String longitude, String latitude, int registration_id,String url);//签退

        //获取数据以后回调
        void responseStatus(String clockstatus);

        //
        void responseInId(int registration_id);

        void responseSuccess();

        void distory();
    }

}
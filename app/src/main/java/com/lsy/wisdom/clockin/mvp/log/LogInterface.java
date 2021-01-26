package com.lsy.wisdom.clockin.mvp.log;

import com.lsy.wisdom.clockin.bean.LogData;

import java.util.List;

/**
 * Created by lsy on 2020/6/14
 * todo :
 */
public interface LogInterface {
    interface Model {
        void getLog(int pageNo);//获取个人日志

        void getStaffLog(int pageNo);//提交给我的日志

    }

    interface View {
        void setLog(List<LogData> logDatas);

        void setStafflog(List<LogData> logDatas);

        void setSuccess();//
    }

    interface Presenter {
        void getLog(int pageNo);//获取个人日志

        void getStaffLog(int pageNo);//提交给我的日志

        //获取数据以后回调
        void responseLog(List<LogData> logDatas);

        void responseStaffLog(List<LogData> logDatas);

        void responseSuccess();

        void distory();
    }

}

package com.lsy.wisdom.clockin.mvp.record;

import com.lsy.wisdom.clockin.bean.RecordData;

import java.util.List;

/**
 * Created by lsy on 2020/5/26
 * todo :
 */
public interface RecordInterface {
    interface Model {
        //staff_id(登录返回) pageNo,pageSize   examine_type(审批,请假,加班,出差,报销)
        void getRecord(String state,int staff_id, int pageNo, int pageSize, String examine_type,int lei);//记录返回

        void getStaffRecord(String state,int staff_id, int pageNo, int pageSize, String examine_type,int lei);//提交给我的记录返回
    }

    interface View {
        //setData方法是为了 Activity实现view接口以后，重写这个方法就可以得到数据，为View赋值

        void setFailure(String message);//添加失败

        void setSuccess(List<RecordData> datas,int lei);//添加成功
    }

    interface Presenter {

        void getRecord(String state,int staff_id, int pageNo, int pageSize, String examine_type,int lei);//记录返回
        void getStaffRecord(String state,int staff_id, int pageNo, int pageSize, String examine_type,int lei);//提交给我的记录返回

        void responseFailure(String message);//添加失败

        void responseSuccess(List<RecordData> datas,int lei);

        void distory();
    }

}


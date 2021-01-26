package com.lsy.wisdom.clockin.mvp.task;

import com.lsy.wisdom.clockin.bean.TaskData;

import java.util.List;

/**
 * Created by lsy on 2020/7/29
 * describe :  TODO
 */
public interface TaskInterface {

    interface Model {

        //creator_id(创建人id),pageNo,pageSize,state
        void getMyCreate(String creator_id, int pageNo, int pageSize, String state);//获取我创建的任务

        //staff_id(员工id),pageNo,pageSize,state
        void getMyJoin(String staff_id, int pageNo, int pageSize, String state);//获取我参与的任务

        //传参:principal_id(负责人id),pageNo,pageSize,state
        void getMyResponsible(String principal_id, int pageNo, int pageSize, String state);//获取我负责的任务

    }

    interface View {
        //setData方法是为了 Activity实现view接口以后，重写这个方法就可以得到数据，为View赋值
        void setDatas(List<TaskData> taskDataList);//返回打卡状态

    }

    interface Presenter {
        //creator_id(创建人id),pageNo,pageSize,state
        void getMyCreate(String creator_id, int pageNo, int pageSize, String state);//获取我创建的任务

        //staff_id(员工id),pageNo,pageSize,state
        void getMyJoin(String staff_id, int pageNo, int pageSize, String state);//获取我参与的任务

        //传参:principal_id(负责人id),pageNo,pageSize,state
        void getMyResponsible(String principal_id, int pageNo, int pageSize, String state);//获取我负责的任务


        //获取数据以后回调
        void responseDatas(List<TaskData> taskDataList);

        void distory();
    }


}

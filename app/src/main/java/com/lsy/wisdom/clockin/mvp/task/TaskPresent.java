package com.lsy.wisdom.clockin.mvp.task;

import android.content.Context;

import com.lsy.wisdom.clockin.bean.TaskData;

import java.util.List;

/**
 * Created by lsy on 2020/7/29
 * describe :  TODO
 */
public class TaskPresent implements TaskInterface.Presenter {

    private TaskInterface.Model model;
    private TaskInterface.View view;
    private Context context;

    public TaskPresent(TaskInterface.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new TaskModel(this, context);
    }

    @Override
    public void getMyCreate(String creator_id, int pageNo, int pageSize, String state) {
        model.getMyCreate(creator_id, pageNo, pageSize, state);
    }

    @Override
    public void getMyJoin(String staff_id, int pageNo, int pageSize, String state) {
        model.getMyJoin(staff_id, pageNo, pageSize, state);
    }

    @Override
    public void getMyResponsible(String principal_id, int pageNo, int pageSize, String state) {
        model.getMyResponsible(principal_id, pageNo, pageSize, state);
    }

    @Override
    public void responseDatas(List<TaskData> taskDataList) {
        if (view != null) {
            view.setDatas(taskDataList);
        }

    }

    @Override
    public void distory() {
        view = null;
    }
}

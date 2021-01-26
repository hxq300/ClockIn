package com.lsy.wisdom.clockin.mvp.record;

import android.content.Context;

import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.mvp.append.AddInterface;
import com.lsy.wisdom.clockin.mvp.append.AddModel;

import java.util.List;

/**
 * Created by lsy on 2020/5/26
 * todo :
 */
public class RecordPresent implements RecordInterface.Presenter {

    private RecordInterface.View view;
    private RecordInterface.Model model;
    private Context context;

    public RecordPresent(RecordInterface.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new RecordModle(this, context);
    }

    @Override
    public void getRecord(String state, int staff_id, int pageNo, int pageSize, String examine_type, int lei) {
        model.getRecord(state, staff_id, pageNo, pageSize, examine_type, lei);
    }

    @Override
    public void getStaffRecord(String state, int staff_id, int pageNo, int pageSize, String examine_type, int lei) {
        model.getStaffRecord(state, staff_id, pageNo, pageSize, examine_type, lei);
    }

    @Override
    public void responseFailure(String message) {
        view.setFailure(message);
    }

    @Override
    public void responseSuccess(List<RecordData> datas, int lei) {
        view.setSuccess(datas, lei);
    }

    @Override
    public void distory() {
        view = null;
    }
}

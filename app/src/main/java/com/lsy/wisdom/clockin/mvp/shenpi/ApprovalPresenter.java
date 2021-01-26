package com.lsy.wisdom.clockin.mvp.shenpi;

import android.content.Context;

/**
 * Created by lsy on 2020/6/23
 * todo :
 */
public class ApprovalPresenter implements ApprovalInterface.Presenter {

    private ApprovalInterface.Model model;
    private ApprovalInterface.View view;
    private Context context;

    public ApprovalPresenter(ApprovalInterface.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new ApprovalModel(this, context);
    }

    @Override
    public void sendPass(String id) {
        model.sendPass(id);
    }

    @Override
    public void sendNoPass(String id) {
        model.sendNoPass(id);
    }

    @Override
    public void responseSuccess() {
        view.setSuccess();
    }

    @Override
    public void distory() {
        view = null;
    }
}

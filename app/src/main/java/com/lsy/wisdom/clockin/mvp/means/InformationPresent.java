package com.lsy.wisdom.clockin.mvp.means;

import android.content.Context;

import com.lsy.wisdom.clockin.bean.UserData;

/**
 * Created by lsy on 2020/5/20
 * todo :
 */
public class InformationPresent implements InformationInterface.Presenter {

    private InformationInterface.View view;
    private InformationInterface.Model model;
    private Context context;

    public InformationPresent(InformationInterface.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new InformationModel(this, context);
    }


    @Override
    public void getInformation(String id) {
        model.getInformation(id);
    }

    @Override
    public void updateInformation(int id, String picture, String signature) {
        model.updateInformation(id, picture, signature);
    }

    @Override
    public void responseInformation(UserData userData) {
        view.setInformation(userData);
    }


    @Override
    public void responseSuccess() {
        view.success();
    }

    @Override
    public void distory() {
        view = null;
    }
}

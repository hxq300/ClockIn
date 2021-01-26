package com.lsy.wisdom.clockin.mvp;

import android.content.Context;

/**
 * Created by lsy on 2020/5/19
 * todo :
 */
public class PunchCardPresent implements PunchCardInterface.Presenter {


    private PunchCardInterface.View view;
    private PunchCardInterface.Model model;
    private Context context;

    public PunchCardPresent(PunchCardInterface.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new PunchCardModel(this, context);
    }

    @Override
    public void getStatus(String id) {
        model.getStatus(id);
    }

    @Override
    public void signIn(int id,int conglomerate_id, int company_id, String in_address, String remarkD, String longitude, String latitude,String url) {
        model.signIn(id,conglomerate_id, company_id, in_address, remarkD, longitude, latitude,url);
    }

    @Override
    public void signOut(int id,int conglomerate_id, int company_id, String out_address, String remarkT, String longitude, String latitude, int registration_id,String url) {
        model.signOut(id,conglomerate_id, company_id, out_address, remarkT, longitude, latitude, registration_id,url);
    }

    @Override
    public void responseStatus(String clockstatus) {
        view.setStatus(clockstatus);
    }

    @Override
    public void responseInId(int registration_id) {
        view.setInId(registration_id);
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

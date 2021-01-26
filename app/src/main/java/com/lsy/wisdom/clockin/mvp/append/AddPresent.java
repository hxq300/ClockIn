package com.lsy.wisdom.clockin.mvp.append;

import android.content.Context;

/**
 * Created by lsy on 2020/5/25
 * todo :
 */
public class AddPresent implements AddInterface.Presenter {

    private AddInterface.View view;
    private AddInterface.Model model;
    private Context context;

    public AddPresent(AddInterface.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new AddModel(this, context);
    }

    @Override
    public void addCoutentSP(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content) {
        model.addCoutentSP(list, staff_id, conglomerate_id, company_id, examine_type, content);
    }

    @Override
    public void addCoutentBX(int itmes_id, int project_id, String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String expenses_type, double expenses_sum, String expenses_picture) {
        model.addCoutentBX(itmes_id, project_id, list, staff_id, conglomerate_id, company_id, examine_type, content, expenses_type, expenses_sum, expenses_picture);
    }

    @Override
    public void addCoutentCG(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String procurement_type, double procurement_sum, String procurement_img, int project_id) {
        model.addCoutentCG(list, staff_id, conglomerate_id, company_id, examine_type, content, procurement_type, procurement_sum, procurement_img, project_id);
    }

    @Override
    public void addCoutentCC(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String outaddress, long outtimeC, long intimeC) {
        model.addCoutentCC(list, staff_id, conglomerate_id, company_id, examine_type, content, outaddress, outtimeC, intimeC);
    }

    @Override
    public void addCoutentQJ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC) {
        model.addCoutentQJ(list, staff_id, conglomerate_id, company_id, examine_type, content, start_timeC, end_timeC);
    }

    @Override
    public void addCoutentJB(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content) {
        model.addCoutentJB(list, staff_id, conglomerate_id, company_id, examine_type, content);
    }

    @Override
    public void addCoutentZZ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC, String station) {
        model.addCoutentZZ(list, staff_id, conglomerate_id, company_id, examine_type, content, start_timeC, end_timeC, station);
    }

    @Override
    public void responseFailure(String message) {
        view.setFailure(message);
    }

    @Override
    public void responseSuccess() {
        view.setSuccess();
    }

    @Override
    public void distory() {

    }
}

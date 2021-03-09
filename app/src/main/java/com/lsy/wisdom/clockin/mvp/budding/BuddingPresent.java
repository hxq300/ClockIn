package com.lsy.wisdom.clockin.mvp.budding;

import android.content.Context;

import com.lsy.wisdom.clockin.bean.CompanyEntity;
import com.lsy.wisdom.clockin.bean.ProjectC;
import com.lsy.wisdom.clockin.bean.ProjectCus;
import com.lsy.wisdom.clockin.bean.SupplierEntity;

import java.util.List;

/**
 * Created by lsy on 2020/5/19
 * todo :
 */
public class BuddingPresent implements BuddingInterface.Presenter {


    private BuddingInterface.View view;
    private BuddingInterface.Model model;
    private Context context;

    public BuddingPresent(BuddingInterface.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new BuddingModel(this, context);
    }

    @Override
    public void getProject() {
        model.getProject();
    }

    @Override
    public void getCus() {
        model.getCus();
    }

    @Override
    public void getFindCompany() {
        model.getFindCompany();
    }

    @Override
    public void getSelectSupplier() {
        model.getSelectSupplier();
    }

    @Override
    public void responseP(List<ProjectC> pList) {
        view.setProject(pList);
    }

    @Override
    public void responseC(List<ProjectCus> cList) {
        view.setCustom(cList);
    }

    @Override
    public void responseCompany(List<CompanyEntity> companyEntities) {
        view.responseCompany(companyEntities);
    }

    @Override
    public void responseSupplier(List<SupplierEntity> supplierEntities) {
        view.responseSupplier(supplierEntities);
    }

    @Override
    public void distory() {
        view = null;
    }
}

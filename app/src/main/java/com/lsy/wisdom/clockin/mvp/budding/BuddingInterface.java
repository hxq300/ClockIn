package com.lsy.wisdom.clockin.mvp.budding;

import com.lsy.wisdom.clockin.bean.CompanyEntity;
import com.lsy.wisdom.clockin.bean.ProjectC;
import com.lsy.wisdom.clockin.bean.ProjectCus;
import com.lsy.wisdom.clockin.bean.SupplierEntity;

import java.util.List;

/**
 * Created by lsy on 2020/7/24
 * todo :
 */
public interface BuddingInterface {

    interface Model {
        void getProject();//

        void getCus();//

        void getFindCompany(); // 根据集团id查询公司信息

        void getSelectSupplier(); // 根据集团id查询供应商接口
    }

    interface View {

        void setProject(List<ProjectC> pList);//

        void setCustom(List<ProjectCus> cList);//

        void responseCompany(List<CompanyEntity> companyEntities);

        void responseSupplier(List<SupplierEntity> supplierEntities);

    }

    interface Presenter {
        void getProject();//

        void getCus();//

        void getFindCompany(); // 根据集团id查询公司信息

        void getSelectSupplier(); // 根据集团id查询供应商接口

        //获取数据以后回调
        void responseP(List<ProjectC> pList);

        void responseC(List<ProjectCus> cList);

        void responseCompany(List<CompanyEntity> companyEntities);

        void responseSupplier(List<SupplierEntity> supplierEntities);

        void distory();
    }

}
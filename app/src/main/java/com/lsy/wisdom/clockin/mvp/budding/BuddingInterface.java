package com.lsy.wisdom.clockin.mvp.budding;

import com.lsy.wisdom.clockin.bean.ProjectC;
import com.lsy.wisdom.clockin.bean.ProjectCus;

import java.util.List;

/**
 * Created by lsy on 2020/7/24
 * todo :
 */
public interface BuddingInterface {

    interface Model {
        void getProject();//

        void getCus();//
    }

    interface View {

        void setProject(List<ProjectC> pList);//

        void setCustom(List<ProjectCus> cList);//
    }

    interface Presenter {
        void getProject();//

        void getCus();//

        //获取数据以后回调
        void responseP(List<ProjectC> pList);

        void responseC(List<ProjectCus> cList);

        void distory();
    }

}
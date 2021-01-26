package com.lsy.wisdom.clockin.mvp.shenpi;

/**
 * Created by lsy on 2020/6/23
 * todo : 审批部分
 */
public interface ApprovalInterface {

    interface Model {

        void sendPass(String id);//通过

        void sendNoPass(String id);//不通过

    }

    interface View {
        void setSuccess();//
    }

    interface Presenter {
        void sendPass(String id);//通过

        void sendNoPass(String id);//不通过

        void responseSuccess();

        void distory();
    }

}

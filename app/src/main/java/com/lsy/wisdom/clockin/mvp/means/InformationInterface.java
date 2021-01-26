package com.lsy.wisdom.clockin.mvp.means;

import com.lsy.wisdom.clockin.bean.UserData;

/**
 * Created by lsy on 2020/5/20
 * todo :
 */
public interface InformationInterface {

    interface Model {
        void getInformation(String id);//获取员工信息

        void updateInformation(int id, String picture, String signature);//更改信息（图片、个性签名）

    }

    interface View {
        //setData方法是为了 Activity实现view接口以后，重写这个方法就可以得到数据，为View赋值
        void setInformation(UserData userData);//返回个人信息

        void success();//
    }

    interface Presenter {
        void getInformation(String id);//获取员工信息

        void updateInformation(int id, String picture, String signature);//更改信息（图片、个性签名）

        //获取数据以后回调
        void responseInformation(UserData userData);

        void responseSuccess();

        void distory();
    }

}

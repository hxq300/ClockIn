package com.lsy.wisdom.clockin.mvp.means;

import android.content.Context;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.activity.add.FeedbackActivity;
import com.lsy.wisdom.clockin.bean.UserData;
import com.lsy.wisdom.clockin.mvp.PunchCardInterface;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsy on 2020/5/20
 * todo :
 */
public class InformationModel implements InformationInterface.Model {

    private InformationInterface.Presenter presenter;
    private Context context;

    public InformationModel(InformationInterface.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }


    @Override
    public void getInformation(String id) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("id", id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.findStaffById, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("information", "" + dataString);

                Gson gson = new Gson();

                if (dataString != null) {
                    UserData userData = gson.fromJson(dataString, UserData.class);
                    presenter.responseInformation(userData);
                }

                return dataString;
            }
        });
    }

    @Override
    public void updateInformation(int id, String picture, String signature) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("id", id);

        if (signature != null) {
            listcanshu.put("signature", signature);
        }
        if (picture != null) {
            listcanshu.put("picture", picture);
        }


        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.updateStaffApp, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("information", "update==" + dataString);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(context, "更改成功");
                        presenter.responseSuccess();
                    } else {
                        ToastUtils.showBottomToast(context, "" + message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }
}

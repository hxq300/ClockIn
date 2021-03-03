package com.lsy.wisdom.clockin.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.bean.SignOutIdEntity;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.ToastUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsy on 2020/5/19
 * todo :
 */
public class PunchCardModel implements PunchCardInterface.Model {

    private PunchCardInterface.Presenter presenter;
    private Context context;

    public PunchCardModel(PunchCardInterface.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public void getStatus(String id) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("id", id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.findClockstatus, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("punchCard", "" + dataString);

                //{"id":0,"staff_age":0,"company_id":0,"department_id":0,"position_id":0,"clockstatus":"1"}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    String clockstatus = jsonObject.getString("clockstatus");
                    if (clockstatus != null) {
                        presenter.responseStatus(clockstatus);
                    } else {
                        presenter.responseStatus("10");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void getRegistrationId() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        int userId = OKHttpClass.getUserId(context);
        listcanshu.put("staff_id", userId);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.outId, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                Gson gson = new Gson();
                SignOutIdEntity signOutIdEntity = gson.fromJson(dataString, SignOutIdEntity.class);
                presenter.responseSuccessRegistrationId(signOutIdEntity.getRegistration_id());


                return dataString;
            }
        });
    }

    @Override
    public void signIn(int id, int conglomerate_id, int company_id, String in_address, String remarkD, String longitude, String latitude, String url) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("id", id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("in_address", in_address);
        listcanshu.put("remarkD", remarkD);
        listcanshu.put("longitude", longitude);
        listcanshu.put("latitude", latitude);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("url", "" + url);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.insertRegistration, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("punchCard", "" + dataString);

                JSONObject jsonObject = null;
                JSONObject jsonObject1 = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    jsonObject1 = new JSONObject(data);

                    if (code == 200) {
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

    @Override
    public void signOut(int id, int conglomerate_id, int company_id, String out_address, String remarkT, String longitude, String latitude, int registration_id, String url) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("id", id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("out_address", out_address);
        listcanshu.put("remarkT", remarkT);
        listcanshu.put("longitude", longitude);
        listcanshu.put("latitude", latitude);
        listcanshu.put("registration_id", registration_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("url", "" + url);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.updateRegistration, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("punchCard", "" + dataString);
                Gson gson = new Gson();

                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
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

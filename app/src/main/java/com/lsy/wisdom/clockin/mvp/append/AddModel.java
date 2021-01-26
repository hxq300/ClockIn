package com.lsy.wisdom.clockin.mvp.append;

import android.content.Context;

import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsy on 2020/5/25
 * todo :
 */
public class AddModel implements AddInterface.Model {

    private AddInterface.Presenter presenter;
    private Context context;

    public AddModel(AddInterface.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }


    @Override
    public void addCoutentSP(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("examine_type", examine_type);
        listcanshu.put("content", content);
        listcanshu.put("itmes_id", 0);
        listcanshu.put("list", "" + list);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.addCoutent, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("addContent", "" + dataString);

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
                        presenter.responseFailure(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void addCoutentBX(int itmes_id, int project_id, String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content,
                             String expenses_type, double expenses_sum, String expenses_picture) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("itmes_id", itmes_id);
        listcanshu.put("project_id", project_id);
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("examine_type", examine_type);
        listcanshu.put("content", content);
        listcanshu.put("expenses_type", expenses_type);
        listcanshu.put("expenses_sum", expenses_sum);
        listcanshu.put("expenses_picture", expenses_picture);
        listcanshu.put("list", "" + list);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.addCoutent, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("addContent", "" + dataString);

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
                        presenter.responseFailure(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void addCoutentCG(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String procurement_type, double procurement_sum, String procurement_img, int project_id) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("examine_type", examine_type);
        listcanshu.put("content", content);
        listcanshu.put("procurement_type", procurement_type);
        listcanshu.put("procurement_sum", procurement_sum);
        listcanshu.put("itmes_id", 0);
        listcanshu.put("list", "" + list);
        listcanshu.put("procurement_img", "" + procurement_img);
        listcanshu.put("project_id", project_id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.addCoutent, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("addContent", "" + dataString);

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
                        presenter.responseFailure(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }

    @Override
    public void addCoutentCC(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, String outaddress, long outtimeC, long intimeC) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("examine_type", examine_type);
        listcanshu.put("content", content);
        listcanshu.put("outaddress", outaddress);
        listcanshu.put("outtimeC", outtimeC);
        listcanshu.put("intimeC", intimeC);
        listcanshu.put("itmes_id", 0);
        listcanshu.put("list", "" + list);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.addCoutent, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("addContent", "" + dataString);

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
                        presenter.responseFailure(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void addCoutentQJ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("examine_type", examine_type);
        listcanshu.put("content", content);
        listcanshu.put("start_timeC", start_timeC);
        listcanshu.put("end_timeC", end_timeC);
        listcanshu.put("itmes_id", 0);
        listcanshu.put("list", "" + list);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.addCoutent, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("addContent", "" + dataString);

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
                        presenter.responseFailure(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void addCoutentJB(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("examine_type", examine_type);
        listcanshu.put("content", content);
        listcanshu.put("itmes_id", 0);
        listcanshu.put("list", "" + list);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.addCoutent, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("addContent", "" + dataString);

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
                        presenter.responseFailure(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void addCoutentZZ(String list, int staff_id, int conglomerate_id, int company_id, String examine_type, String content, long start_timeC, long end_timeC, String station) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();


//        entry_timeC 入职时间戳
//        promotion_timeC 转正时间戳
//        operating_post 转正职位
//        list  审批人数组
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("conglomerate_id", conglomerate_id);
        listcanshu.put("company_id", company_id);
        listcanshu.put("examine_type", examine_type);
        listcanshu.put("content", content);
        listcanshu.put("entry_timeC", start_timeC);
        listcanshu.put("promotion_timeC", end_timeC);
        listcanshu.put("operating_post", "" + station);
        listcanshu.put("itmes_id", 0);
        listcanshu.put("list", "" + list);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.addCoutent, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("addContent", "" + dataString);

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
                        presenter.responseFailure(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }
}

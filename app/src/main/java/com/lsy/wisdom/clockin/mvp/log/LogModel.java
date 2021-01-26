//package com.lsy.wisdom.clockin.mvp.log;
//
//import android.content.Context;
//
//import com.google.gson.Gson;
//import com.lsy.wisdom.clockin.activity.LogActivity;
//import com.lsy.wisdom.clockin.bean.LogData;
//import com.lsy.wisdom.clockin.request.OKHttpClass;
//import com.lsy.wisdom.clockin.request.RequestURL;
//import com.lsy.wisdom.clockin.utils.L;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by lsy on 2020/6/14
// * todo :
// */
//public class LogModel implements LogInterface.Model {
//
//    private LogInterface.Presenter presenter;
//    private Context context;
//
//    public LogModel(LogInterface.Presenter presenter, Context context) {
//        this.presenter = presenter;
//        this.context = context;
//    }
//
//    @Override
//    public void getLog(int pageNo) {
//        Map<String, Object> listcanshu = new HashMap<>();
//        OKHttpClass okHttpClass = new OKHttpClass();
//
//        //传参:staff_id(员工id,登录返回),pageNo  pageSize
//        listcanshu.put("staff_id", OKHttpClass.getUserId(context));
//        listcanshu.put("pageNo", pageNo);
//        listcanshu.put("pageSize", 10);
//        //设置请求类型、地址和参数
//        okHttpClass.setPostCanShu(context, RequestURL.getLog, listcanshu);
//        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
//            @Override
//            public String requestData(String dataString) {
//                //请求成功数据回调
//                L.log("log", "getLog==" + dataString);
//                Gson gson = new Gson();
//
//                addEmptyView();
////                ToastUtils.showBottomToast(LogActivity.this, "已是最新数据");
////
//////                //{"message":"打卡成功","data":null,"code":200}
//                JSONObject jsonObject = null;
//                JSONArray jsonArray = null;
//                try {
//                    jsonObject = new JSONObject(dataString);
//                    jsonArray = new JSONArray(jsonObject.getString("items"));
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        LogData record = gson.fromJson("" + jsonArray.get(i).toString(), LogData.class);
//                        logDataList.add(record);
//                    }
//
//                    commonAdapter.notifyDataSetChanged();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return dataString;
//            }
//        });
//
//    }
//
//    @Override
//    public void getStaffLog(int pageNo) {
//
//    }
//}

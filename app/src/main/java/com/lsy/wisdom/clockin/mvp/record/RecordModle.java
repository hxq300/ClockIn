package com.lsy.wisdom.clockin.mvp.record;

import android.content.Context;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lsy on 2020/5/26
 * todo :
 */
public class RecordModle implements RecordInterface.Model {


    private RecordInterface.Presenter presenter;
    private Context context;
    private List<RecordData> recordDataList;

    public RecordModle(RecordInterface.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public void getRecord(String state, int staff_id, int pageNo, int pageSize, String examine_type, int lei) {
        L.log("getRecord", "发起请求");

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);//状态:未审核,通过,未通过
        listcanshu.put("state", "" + state);
        listcanshu.put("pageNo", pageNo);
        listcanshu.put("pageSize", pageSize);
        listcanshu.put("examine_type", examine_type);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.getRecord, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

                recordDataList = new ArrayList<>();
                //请求成功数据回调
                L.log("getRecord", "" + dataString);

                Gson gson = new Gson();
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        RecordData recordData = gson.fromJson(jsonArray.get(i).toString(), RecordData.class);
                        recordDataList.add(recordData);
                    }

                    presenter.responseSuccess(recordDataList, lei);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void getStaffRecord(String state, int staff_id, int pageNo, int pageSize, String examine_type, int lei) {
        L.log("getRecord", "发起请求");

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("state", "" + state);
        listcanshu.put("pageNo", pageNo);//状态:未审核,通过,未通过
        listcanshu.put("pageSize", pageSize);
        listcanshu.put("examine_type", examine_type);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.getStaffRecord, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

                recordDataList = new ArrayList<>();
                //请求成功数据回调
                L.log("getRecord", "" + dataString);

                Gson gson = new Gson();
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        RecordData recordData = gson.fromJson(jsonArray.get(i).toString(), RecordData.class);
                        recordDataList.add(recordData);
                    }

                    presenter.responseSuccess(recordDataList, lei);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }
}

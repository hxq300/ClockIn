package com.lsy.wisdom.clockin.mvp.task;

import android.content.Context;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.bean.TaskData;
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
 * Created by lsy on 2020/7/29
 * describe :  TODO
 */
public class TaskModel implements TaskInterface.Model {

    private TaskInterface.Presenter presenter;
    private Context context;

    private List<TaskData> dataList=null;

    public TaskModel(TaskInterface.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public void getMyCreate(String creator_id, int pageNo, int pageSize, String state) {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("creator_id", creator_id);
        listcanshu.put("pageNo", pageNo);
        listcanshu.put("pageSize", pageSize);
        listcanshu.put("state", state);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.getMyCreate, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("taskGetMyCreate", "" + dataString);

                dataList=new ArrayList<>();

                Gson gson = new Gson();
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        TaskData taskData = gson.fromJson(jsonArray.get(i).toString(), TaskData.class);
                        dataList.add(taskData);
                    }

                    presenter.responseDatas(dataList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }

    @Override
    public void getMyJoin(String staff_id, int pageNo, int pageSize, String state) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", staff_id);
        listcanshu.put("pageNo", pageNo);
        listcanshu.put("pageSize", pageSize);
        listcanshu.put("state", state);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.getMyJoin, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("taskGetMyJoin", "" + dataString);

                dataList=new ArrayList<>();

                Gson gson = new Gson();
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        TaskData taskData = gson.fromJson(jsonArray.get(i).toString(), TaskData.class);
                        dataList.add(taskData);
                    }

                    presenter.responseDatas(dataList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void getMyResponsible(String principal_id, int pageNo, int pageSize, String state) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("principal_id", principal_id);
        listcanshu.put("pageNo", pageNo);
        listcanshu.put("pageSize", pageSize);
        listcanshu.put("state", state);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.getMyResponsible, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("taskGetMyResponsible", "" + dataString);

                dataList=new ArrayList<>();

                Gson gson = new Gson();
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        TaskData taskData = gson.fromJson(jsonArray.get(i).toString(), TaskData.class);
                        dataList.add(taskData);
                    }

                    presenter.responseDatas(dataList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }
}

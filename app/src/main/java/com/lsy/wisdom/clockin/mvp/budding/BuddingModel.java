package com.lsy.wisdom.clockin.mvp.budding;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lsy.wisdom.clockin.bean.CompanyEntity;
import com.lsy.wisdom.clockin.bean.ProjectC;
import com.lsy.wisdom.clockin.bean.ProjectCus;
import com.lsy.wisdom.clockin.bean.SupplierEntity;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lsy on 2020/5/19
 * todo :
 */
public class BuddingModel implements BuddingInterface.Model {

    private BuddingInterface.Presenter presenter;
    private Context context;

    public BuddingModel(BuddingInterface.Presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public void getProject() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(context));
        listcanshu.put("staff_id", OKHttpClass.getUserId(context));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.bindingProject, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("buddingProject", "" + dataString);
                Gson gson = new Gson();

                List<ProjectC> projectCList = new ArrayList<>();

                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");

                    jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProjectC projectC = gson.fromJson(jsonArray.get(i).toString(), ProjectC.class);
                        projectCList.add(projectC);
                    }

                    presenter.responseP(projectCList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void getCus() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(context));
        listcanshu.put("staff_id", OKHttpClass.getUserId(context));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.bindingCus, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("buddingCus", "" + dataString);
                Gson gson = new Gson();

                List<ProjectCus> projectCList = new ArrayList<>();

//                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");

                    jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProjectCus projectC = gson.fromJson(jsonArray.get(i).toString(), ProjectCus.class);
                        projectCList.add(projectC);
                    }

                    presenter.responseC(projectCList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    @Override
    public void getFindCompany() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(context));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.FindCompany, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

                Gson gson = new Gson();
                List<CompanyEntity> companyEntities = gson.fromJson(dataString,new TypeToken<List<CompanyEntity>>(){}.getType());
                presenter.responseCompany(companyEntities);
                return dataString;
            }
        });
    }

    @Override
    public void getSelectSupplier() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(context));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(context, RequestURL.SelectSupplier, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {


                Gson gson = new Gson();
                List<SupplierEntity> supplierEntities = gson.fromJson(dataString,new TypeToken<List<SupplierEntity>>(){}.getType());
                presenter.responseSupplier(supplierEntities);

                return dataString;
            }
        });
    }
}

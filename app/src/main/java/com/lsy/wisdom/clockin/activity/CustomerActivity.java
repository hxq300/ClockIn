package com.lsy.wisdom.clockin.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.AddCustomerlActivity;
import com.lsy.wisdom.clockin.activity.desc.CustomerDescActivity;
import com.lsy.wisdom.clockin.activity.desc.ShareActivity;
import com.lsy.wisdom.clockin.bean.Company;
import com.lsy.wisdom.clockin.bean.ProcessData;
import com.lsy.wisdom.clockin.permission.QuanXian;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.GroupButtonView;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/12
 * todo : 客户列表 合作公司
 */
public class CustomerActivity extends AppCompatActivity implements QuanXian.OnPermission {

    @BindView(R.id.customer_toolbar)
    IToolbar customerToolbar;
    @BindView(R.id.id_customer_recycler)
    RecyclerView idCustomerRecycler;
    @BindView(R.id.gbv_customer)
    GroupButtonView gbvCustomer;
    @BindView(R.id.process_recycler)
    RecyclerView processRecycler;



    private CommonAdapter commonAdapter;
    private CommonAdapter processAdapter;
    private int open = 0;

    private List<Company> companies = null;
    private List<ProcessData> dataList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        setSupportActionBar(customerToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        //判断权限是否全部打开
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }

        initBar();


        initView();

        getCustomer();
    }

    private void initView() {

        //===========
        idCustomerRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerActivity.this);
        idCustomerRecycler.setLayoutManager(linearLayoutManager);
        idCustomerRecycler.setNestedScrollingEnabled(false);


        //===========
        processRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearManager = new LinearLayoutManager(CustomerActivity.this);
        processRecycler.setLayoutManager(linearManager);
        processRecycler.setNestedScrollingEnabled(false);


        companies = new ArrayList<>();

        commonAdapter = new CommonAdapter<Company>(this, R.layout.item_customer_view, companies) {
            @Override
            protected void convert(ViewHolder holder, Company company, int position) {
                if (company != null) {
                    holder.setText(R.id.customer_name, "" + company.getItems_name());
                    holder.setText(R.id.user_name, "" + company.getStaff_name());
                    holder.setText(R.id.up_time, "" + company.getUptime());
                }

                //分享
                holder.getView(R.id.customer_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ToastUtils.showBottomToast(CustomerActivity.this, "正在分享给好友");
                        Intent intent = new Intent();
                        intent.putExtra("id", company.getId());
                        intent.setClass(CustomerActivity.this, ShareActivity.class);
                        startActivity(intent);

                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent desc = new Intent();
                        desc.putExtra("company", "" + company.toString());
                        desc.setClass(CustomerActivity.this, CustomerDescActivity.class);

                        startActivity(desc);
                    }
                });
            }

        };

        idCustomerRecycler.setAdapter(commonAdapter);

        dataList = new ArrayList<>();
        processAdapter = new CommonAdapter<ProcessData>(this, R.layout.item_process_view, dataList) {
            @Override
            protected void convert(ViewHolder holder, ProcessData data, int position) {

                holder.setText(R.id.cp_name, "" + data.getItems_name());
                holder.setText(R.id.cp_principal, "负责人：" + data.getPrincipal());
                holder.setText(R.id.cp_staff_name, "员工姓名：" + data.getStaff_name());
                holder.setText(R.id.cp_content, "跟进内容：" + data.getContent());
                holder.setText(R.id.cp_state, "" + data.getSchedule_type());
                holder.setText(R.id.cp_time, "" + data.getUptime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (GeneralMethod.isFastClick()) {
                            //todo:进入跟进详情列表页
                        }
                    }
                });
            }

        };

        processRecycler.setAdapter(processAdapter);

        processRecycler.setVisibility(View.GONE);


        gbvCustomer.setOnGroupBtnClickListener(new GroupButtonView.OnGroupBtnClickListener() {
            @Override
            public void groupBtnClick(String code) {
                open = Integer.parseInt(code);
                switch (open) {
                    case 0:
                        dataList.clear();
                        companies.clear();
                        idCustomerRecycler.setVisibility(View.VISIBLE);
                        processRecycler.setVisibility(View.GONE);

                        getCustomer();
                        break;
                    case 1:
                        dataList.clear();
                        companies.clear();
                        idCustomerRecycler.setVisibility(View.GONE);
                        processRecycler.setVisibility(View.VISIBLE);
                        getStaffProcess();
                        break;
                    default:
                        break;
                }

//                Toast.makeText(GoodsDetailsActivity.this, code, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (open == 0) {
            dataList.clear();
            companies.clear();

            idCustomerRecycler.setVisibility(View.VISIBLE);
            processRecycler.setVisibility(View.GONE);
            getCustomer();
        } else {
            dataList.clear();
            companies.clear();

            idCustomerRecycler.setVisibility(View.GONE);
            processRecycler.setVisibility(View.VISIBLE);
            getStaffProcess();
        }

    }

    public void getCustomer() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        //传参:staff_id(登录返回),conglomerate_id(登录返回)
        listcanshu.put("staff_id", OKHttpClass.getUserId(this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(CustomerActivity.this, RequestURL.customerQuery, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("customerQuery", "" + dataString);

                Gson gson = new Gson();

                try {
                    JSONObject jsonObject = new JSONObject(dataString);

                    String data = jsonObject.getString("data");

                    JSONArray jsonArray = new JSONArray("" + data);

                    companies.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (jsonArray.get(i) != null) {
                            Company company = gson.fromJson(jsonArray.get(i).toString(), Company.class);
                            companies.add(company);
                        }

                    }

                    commonAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return dataString;
            }
        });
    }


    public void getStaffProcess() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        //传参:staff_id(登录返回),conglomerate_id(登录返回)
        listcanshu.put("staff_id", OKHttpClass.getUserId(this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(CustomerActivity.this, RequestURL.getStaffProcess, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("getStaffProcess", "" + dataString);

                Gson gson = new Gson();

                try {
                    JSONObject jsonObject = new JSONObject(dataString);

                    String data = jsonObject.getString("data");

                    JSONArray jsonArray = new JSONArray("" + data);

                    companies.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (jsonArray.get(i) != null) {
                            ProcessData data1 = gson.fromJson(jsonArray.get(i).toString(), ProcessData.class);
                            dataList.add(data1);
                        }

                    }

                    processAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return dataString;
            }
        });
    }


    private void initBar() {

        Menu menu = customerToolbar.getMenu();
        menu.clear();

        customerToolbar.inflateMenu(R.menu.add_menu);
        customerToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://添加客户
                        Intent addCustomer = new Intent(CustomerActivity.this, AddCustomerlActivity.class);
                        startActivity(addCustomer);

                        break;

                    default:
                        break;
                }
            }
        });
    }

    // 用户已经同意该权限
    @Override
    public void one_permission_isok(String permission_name) {

    }

    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
    @Override
    public void one_permission_is_refuse(String permission_name) {
        requestPermissions();
    }

    private boolean noPermission = false;

    // 用户拒绝了该权限，并且选中『不再询问』
    @Override
    public void one_permission_is_refuse_no_prompt(String permission_name) {
        noPermission = true;
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(CustomerActivity.this, CustomerActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CALL_PHONE
        );
    }



}

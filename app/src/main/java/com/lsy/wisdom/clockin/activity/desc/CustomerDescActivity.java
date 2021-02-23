package com.lsy.wisdom.clockin.activity.desc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.CustomerActivity;
import com.lsy.wisdom.clockin.activity.add.AddClientActivity;
import com.lsy.wisdom.clockin.activity.add.AddCustomerlActivity;
import com.lsy.wisdom.clockin.activity.add.AddProcessActivity;
import com.lsy.wisdom.clockin.activity.add.AddSignedActivity;
import com.lsy.wisdom.clockin.bean.ClientData;
import com.lsy.wisdom.clockin.bean.Company;
import com.lsy.wisdom.clockin.permission.QuanXian;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
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
 * todo : 客户详情 (分享客户)
 */
public class CustomerDescActivity extends AppCompatActivity implements QuanXian.OnPermission {

    @BindView(R.id.cdesc_toolbar)
    IToolbar cdescToolbar;
    @BindView(R.id.cdesc_time)
    TextView cdescTime;
    @BindView(R.id.cdesc_type)
    EditText cdescType;
    @BindView(R.id.cdesc_name)
    EditText cdescName;
    @BindView(R.id.cdesc_recycler)
    RecyclerView cdescRecycler;
    @BindView(R.id.cdesc_util)
    EditText cdescUtil;
    @BindView(R.id.cdesc_yuangong)
    TextView cdescYuangong;
    @BindView(R.id.btn_del)
    Button btnDel;

    private Company company;

    private CommonAdapter commonAdapter;

    private List<ClientData> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_desc);
        setSupportActionBar(cdescToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        //判断权限是否全部打开
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }

        initRecycle();


    }

    private void initRecycle() {

        Intent intent = getIntent();

        Gson gson = new Gson();

        String data = intent.getStringExtra("company");

        company = gson.fromJson(data, Company.class);

        if (company.getUptime() != null) {
            cdescTime.setText("" + company.getUptime());
        }

        if (company.getType() != null) {
            cdescType.setText("" + company.getType());
        }

        if (company.getItems_name() != null) {
            cdescName.setText("" + company.getItems_name());
        }

        if (company.getBloc_name() != null) {
            cdescUtil.setText("" + company.getBloc_name());
        }

        if (company.getStaff_name() != null) {
            cdescYuangong.setText("" + company.getStaff_name());
        }

        //===========
        cdescRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomerDescActivity.this);
        cdescRecycler.setLayoutManager(linearLayoutManager);
        cdescRecycler.setNestedScrollingEnabled(false);

        dataList = new ArrayList<>();


        commonAdapter = new CommonAdapter<ClientData>(this, R.layout.item_client_view, dataList) {
            @Override
            protected void convert(ViewHolder holder, ClientData data, int position) {
                holder.setText(R.id.client_name, "" + data.getClient_name());
                holder.setText(R.id.client_sex, "" + data.getClient_sex());
                holder.setText(R.id.client_jod, "" + data.getClient_department());
                holder.setText(R.id.client_phone, "" + data.getClient_phone());


                //打电话
                holder.getView(R.id.client_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (noPermission) {
                            Toast.makeText(CustomerDescActivity.this, "您拒绝了拨打电话权限,无法联系物业", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "19893330840"));
                            if (ActivityCompat.checkSelfPermission(CustomerDescActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                            }
                            startActivity(intent);
                        }
                    }
                });
            }

        };

        cdescRecycler.setAdapter(commonAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCustomer();
    }

    private void getCustomer() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("items_id", company.getId());

        dataList.clear();

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(CustomerDescActivity.this, RequestURL.getClient, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("getClient", "" + dataString);

                Gson gson = new Gson();
                JSONArray jsonArray = null;
                try {

                    jsonArray = new JSONArray(dataString);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ClientData data = gson.fromJson("" + jsonArray.get(i).toString(), ClientData.class);
                        dataList.add(data);
                    }

                    commonAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }

    private void initBar() {
        Menu menu = cdescToolbar.getMenu();
        menu.clear();

        cdescToolbar.inflateMenu(R.menu.add_more_menu);
        cdescToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://添加联系人
                        Intent addCustomer = new Intent();
                        addCustomer.putExtra("items_id", company.getId());
                        addCustomer.setClass(CustomerDescActivity.this, AddClientActivity.class);
                        startActivity(addCustomer);
                        break;

                    case 2://新建跟进
                        Intent addProcess = new Intent();
                        addProcess.putExtra("items_id", company.getId());
                        addProcess.putExtra("items_name", company.getItems_name());
                        addProcess.setClass(CustomerDescActivity.this, AddProcessActivity.class);
                        startActivity(addProcess);
                        break;

                    case 3://客户签约
                        Intent qianyue = new Intent();
                        qianyue.putExtra("items_id", company.getId());
                        qianyue.putExtra("items_name", company.getItems_name());
                        qianyue.setClass(CustomerDescActivity.this, AddSignedActivity.class);
                        startActivity(qianyue);
                        break;

                    default:
                        L.log("munu", "新建跟进" + pos);
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
        QuanXian quanXian = new QuanXian(CustomerDescActivity.this, CustomerDescActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CALL_PHONE
        );
    }

    // 提交编辑
    @OnClick(R.id.btn_del)
    public void onViewClicked() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        //传参:staff_id(登录返回),conglomerate_id(登录返回)
        String s = cdescUtil.getText().toString();
        listcanshu.put("id", company.getId());
        listcanshu.put("items_name",cdescUtil.getText().toString() +"");
        listcanshu.put("bloc_name", cdescName.getText().toString()+"");
        listcanshu.put("type", cdescType.getText().toString()+"");

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(CustomerDescActivity.this, RequestURL.updateMessage, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("customerQuery", "" + dataString);

//                Gson gson = new Gson();

                //{"message":"客户添加成功,暂无联系人!","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(CustomerDescActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(CustomerDescActivity.this, "" + message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }
}

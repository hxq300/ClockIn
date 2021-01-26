package com.lsy.wisdom.clockin.activity.add;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.TimeUtils;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/12
 * todo : 添加客户
 */
public class AddCustomerlActivity extends AppCompatActivity {


    @BindView(R.id.add_log_toolbar)
    IToolbar addLogToolbar;
    @BindView(R.id.cusm_time)
    TextView cusmTime;
    @BindView(R.id.cusm_type)
    EditText cusmType;
    @BindView(R.id.cusm_bloc_name)
    EditText cusmBlocName;
    @BindView(R.id.client_name)
    EditText clientName;
    @BindView(R.id.client_sex)
    EditText clientSex;
    @BindView(R.id.client_job_title)
    EditText clientJobTitle;
    @BindView(R.id.client_phone)
    EditText clientPhone;
    @BindView(R.id.cusm_jitian_name)
    EditText cusmJitianName;
    @BindView(R.id.client_department)
    EditText clientDepartment;

    private TimePickerView pickerView;
    private long timeStart = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        setSupportActionBar(addLogToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();
    }


    private void initBar() {
        addLogToolbar.inflateMenu(R.menu.toolbar_menu);
        addLogToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.cusm_time_line, R.id.cusm_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cusm_time_line:

                pickerView = new TimePickerBuilder(AddCustomerlActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        timeStart = date.getTime();
                        cusmTime.setText("" + TimeUtils.getTimeYMD(date));
                    }
                }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        L.log("pvTime", "onTimeSelectChanged");
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})
//                            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                        .build();
                pickerView.show();
                break;
            case R.id.cusm_commit:

                if (is_input()) {

                    addCustomer();
                }

                break;
        }
    }


    public void addCustomer() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
//        nt类型  conglomerate_id(登录返回),staff_id(登录返回用户id)
//        String类型  items_name(项目名称),bloc_name(所属集团),type(客户类型)
//        client_name(联系人姓名),client_sex(性别),client_position(职务),client_department(所在部门),client_phone(联系电话)
//        long类型   uptimeC(上传时间戳)
        listcanshu.put("staff_id", OKHttpClass.getUserId(this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("items_name", "" + cusmBlocName.getText().toString());
        listcanshu.put("bloc_name", "" + cusmJitianName.getText().toString());
        listcanshu.put("type", "" + cusmType.getText().toString());
        listcanshu.put("client_name", "" + clientName.getText().toString());
        listcanshu.put("client_sex", "" + clientSex.getText().toString());
        listcanshu.put("client_position", "" + clientJobTitle.getText().toString());
        listcanshu.put("client_department", "" + clientDepartment.getText().toString());
        listcanshu.put("client_phone", "" + clientPhone.getText().toString());
        listcanshu.put("uptimeC", timeStart);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddCustomerlActivity.this, RequestURL.addCustomer, listcanshu);
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
                        ToastUtils.showBottomToast(AddCustomerlActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(AddCustomerlActivity.this, "" + message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return dataString;
            }
        });
    }


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (timeStart <= 0) {
            Toast.makeText(AddCustomerlActivity.this, "未选择开始日期", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cusmType.getText().toString().trim().length() < 1) {
            Toast.makeText(AddCustomerlActivity.this, "请输入客户类型", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cusmJitianName.getText().toString().trim().length() < 1) {
            Toast.makeText(AddCustomerlActivity.this, "请输入所属集团", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cusmBlocName.getText().toString().trim().length() < 1) {
            Toast.makeText(AddCustomerlActivity.this, "请输入客户名称", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}

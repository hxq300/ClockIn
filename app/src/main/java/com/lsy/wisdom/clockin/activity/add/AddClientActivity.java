package com.lsy.wisdom.clockin.activity.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
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
public class AddClientActivity extends AppCompatActivity {

    @BindView(R.id.aclient_toolbar)
    IToolbar aclientToolbar;
    @BindView(R.id.aclient_name)
    EditText aclientName;
    @BindView(R.id.aclient_sex)
    EditText aclientSex;
    @BindView(R.id.client_job_title)
    EditText clientJobTitle;
    @BindView(R.id.aclient_department)
    EditText aclientDepartment;
    @BindView(R.id.aclient_phone)
    EditText aclientPhone;
    @BindView(R.id.cusm_commit)
    Button cusmCommit;

    private int items_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        setSupportActionBar(aclientToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        Intent intent = getIntent();
        items_id = intent.getIntExtra("items_id", 0);

        initBar();
    }


    private void initBar() {
        aclientToolbar.inflateMenu(R.menu.toolbar_menu);
        aclientToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

    @OnClick({R.id.cusm_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.cusm_commit:

                if (is_input()) {

                    addCLient();
                }

                break;
        }
    }


    public void addCLient() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:client_name(联系人姓名),client_sex(性别),client_position(职务),client_department(所在部门),client_phone(联系电话),staff_id,items_id(客户id)

        listcanshu.put("client_name", "" + aclientName.getText().toString());
        listcanshu.put("client_sex", "" + aclientSex.getText().toString());
        listcanshu.put("client_position", "" + clientJobTitle.getText().toString());
        listcanshu.put("client_department", "" + aclientDepartment.getText().toString());
        listcanshu.put("client_phone", "" + aclientPhone.getText().toString());
        listcanshu.put("staff_id", OKHttpClass.getUserId(this));
        listcanshu.put("items_id", items_id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddClientActivity.this, RequestURL.addClient, listcanshu);
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
                        ToastUtils.showBottomToast(AddClientActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(AddClientActivity.this, "" + message);
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

        if (aclientName.getText().toString().trim().length() < 1) {
            Toast.makeText(AddClientActivity.this, "请输入联系人姓名", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (aclientSex.getText().toString().trim().length() < 1) {
            Toast.makeText(AddClientActivity.this, "请输入性别", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (clientJobTitle.getText().toString().trim().length() < 1) {
            Toast.makeText(AddClientActivity.this, "请输入职位", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (aclientPhone.getText().toString().trim().length() < 11) {
            Toast.makeText(AddClientActivity.this, "手机号不正确", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}

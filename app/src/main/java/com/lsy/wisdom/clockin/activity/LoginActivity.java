package com.lsy.wisdom.clockin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.UserData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.AutoSeparateTextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/15
 * todo : 登录
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_et_phone)
    EditText loginEtPhone;
    @BindView(R.id.login_et_pass)
    EditText loginEtPass;
    @BindView(R.id.logon)
    Button logon;

    private AutoSeparateTextWatcher textWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xFFA400);
        }

        initView();

    }

    private void initView() {
        textWatcher = new AutoSeparateTextWatcher(loginEtPhone);
        textWatcher.setRULES(new int[]{3, 4, 4});
        textWatcher.setSeparator(' ');
        loginEtPhone.addTextChangedListener(textWatcher);
    }

    @OnClick(R.id.logon)
    public void onViewClicked() {
        if (is_input() && GeneralMethod.isFastClick()) {
            L.log("login", "login");
            toLogin("" + textWatcher.removeSpecialSeparator(loginEtPhone, ' ').trim(), "" + loginEtPass.getText().toString().trim());
        }
    }


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (textWatcher.removeSpecialSeparator(loginEtPhone, ' ').length() < 11) {
            Toast.makeText(LoginActivity.this, "请检查您的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (loginEtPass.getText().toString().trim().length() < 1) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * 保存用户id
     */
    private void saveUserMsg(UserData managerData) {
        SharedUtils sharedUtils = new SharedUtils(LoginActivity.this, SharedUtils.CLOCK);
        sharedUtils.setData(sharedUtils.USERID, managerData.getId());
        sharedUtils.setData(sharedUtils.COMPANYID, managerData.getCompany_id());
        sharedUtils.setData(sharedUtils.JITUANID, managerData.getConglomerate_id());
    }


    public void toLogin(String staff_phone, String password) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //staff_phone(员工手机号),password(密码)
        listcanshu.put("staff_phone", "" + staff_phone);
        listcanshu.put("password", "" + password);

        L.log("login", "phone=" + staff_phone.trim() + ",pass" + password);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(LoginActivity.this, RequestURL.login, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("login", "" + dataString);
                Gson gson = new Gson();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);
                    int code = jsonObject.getInt("code");
                    String data = jsonObject.getString("data");
                    String message = jsonObject.getString("message");

                    if (code == 200 && data != null) {
                        UserData userData = gson.fromJson(data, UserData.class);

                        saveUserMsg(userData);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        ToastUtils.showBottomToast(LoginActivity.this, "" + message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ToastUtils.showBottomToast(this, "需要先登录");
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

}

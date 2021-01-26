package com.lsy.wisdom.clockin.activity.desc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/28
 * todo : 审批详情
 */
public class ShenPiDescActivity extends AppCompatActivity {


    @BindView(R.id.sp_toolbar)
    IToolbar spToolbar;
    @BindView(R.id.sp_reasons)
    TextView spReasons;

    private RecordData recordData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenpi_desc);
        setSupportActionBar(spToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("RecordData");
        Gson gson = new Gson();
        recordData = gson.fromJson(data, RecordData.class);

        if (recordData.getContent() != null) {
            spReasons.setText("" + recordData.getContent());
        }


    }


    private void initBar() {
        spToolbar.inflateMenu(R.menu.toolbar_menu);
        spToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        break;

                    default:
                        break;
                }
            }
        });
    }


    //删除审批
    @OnClick(R.id.btn_del)
    public void onViewClicked() {

        if (GeneralMethod.isFastClick()) {
            delete("" + recordData.getId());
        }
    }

    public void delete(String id) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        listcanshu.put("id", id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(ShenPiDescActivity.this, RequestURL.spDel, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("deleted", "" + dataString);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(ShenPiDescActivity.this, "删除成功");
                        finish();
                    } else {
                        ToastUtils.showBottomToast(ShenPiDescActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }
}

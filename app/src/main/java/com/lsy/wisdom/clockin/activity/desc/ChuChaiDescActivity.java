package com.lsy.wisdom.clockin.activity.desc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.LogData;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/28
 * todo : 出差详情
 */
public class ChuChaiDescActivity extends AppCompatActivity {

    @BindView(R.id.cc_toolbar)
    IToolbar ccToolbar;
    @BindView(R.id.cc_start_time)
    TextView ccStartTime;
    @BindView(R.id.cc_end_time)
    TextView ccEndTime;
    @BindView(R.id.cc_address)
    TextView ccAddress;
    @BindView(R.id.cc_reasons)
    TextView ccReasons;
    private RecordData recordData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuchai_desc);
        setSupportActionBar(ccToolbar);
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

        if (recordData.getOuttime() != null) {
            ccStartTime.setText("" + recordData.getOuttime());
        }

        if (recordData.getIntime() != null) {
            ccEndTime.setText("" + recordData.getIntime());
        }

        if (recordData.getOutaddress() != null) {
            ccAddress.setText("" + recordData.getOutaddress());
        }

        L.log("" + recordData.getContent());
        if (recordData.getContent() != null) {
            ccReasons.setText("" + recordData.getContent());
        }


    }


    private void initBar() {
        ccToolbar.inflateMenu(R.menu.toolbar_menu);
        ccToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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
}

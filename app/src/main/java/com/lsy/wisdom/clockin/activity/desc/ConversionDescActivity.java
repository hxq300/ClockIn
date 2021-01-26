package com.lsy.wisdom.clockin.activity.desc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/28
 * todo : 转正详情
 */
public class ConversionDescActivity extends AppCompatActivity {

    @BindView(R.id.con_toolbar)
    IToolbar conToolbar;
    @BindView(R.id.con_start_time)
    TextView conStartTime;
    @BindView(R.id.con_end_time)
    TextView conEndTime;
    @BindView(R.id.con_station)
    TextView conStation;
    @BindView(R.id.con_reasons)
    TextView conReasons;
    private RecordData recordData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_desc);
        setSupportActionBar(conToolbar);
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

        if (recordData.getEntry_time() != null) {
            conStartTime.setText("" + recordData.getEntry_time());
        }
        if (recordData.getPromotion_time() != null) {
            conEndTime.setText("" + recordData.getPromotion_time());
        }

        if (recordData.getOperating_post() != null) {
            conStation.setText("" + recordData.getOperating_post());
        }
        if (recordData.getContent() != null) {
            conReasons.setText("" + recordData.getContent());
        }

    }


    private void initBar() {
        conToolbar.inflateMenu(R.menu.toolbar_menu);
        conToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

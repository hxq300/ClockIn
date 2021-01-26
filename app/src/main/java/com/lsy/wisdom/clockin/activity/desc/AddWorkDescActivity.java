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
 * todo : 加班详情
 */
public class AddWorkDescActivity extends AppCompatActivity {

    @BindView(R.id.sp_toolbar)
    IToolbar spToolbar;
    @BindView(R.id.sp_reasons)
    TextView spReasons;

    private RecordData recordData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exwork_desc);
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


}

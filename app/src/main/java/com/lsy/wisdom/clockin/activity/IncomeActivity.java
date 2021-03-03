package com.lsy.wisdom.clockin.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.AddApprovalActivity;
import com.lsy.wisdom.clockin.activity.add.AddConversionActivity;
import com.lsy.wisdom.clockin.activity.add.AddLeaveActivity;
import com.lsy.wisdom.clockin.activity.add.AddPurchaseActivity;
import com.lsy.wisdom.clockin.activity.add.AddReimburseActivity;
import com.lsy.wisdom.clockin.activity.add.EvectionActivity;
import com.lsy.wisdom.clockin.activity.add.ExtraWorkActivity;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.GroupButtonView;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by hxq on 2021/3/3
 * describe :  TODO
 */
public class IncomeActivity extends AppCompatActivity {
    @BindView(R.id.gbv_record)
    GroupButtonView gbvRecord;
    @BindView(R.id.record_toolbar)
    IToolbar recordToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        init();
    }

    private void init() {


        recordToolbar.inflateMenu(R.menu.add_menu);
        recordToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        break;
                    case 1://添加

                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

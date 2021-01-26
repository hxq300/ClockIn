package com.lsy.wisdom.clockin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.setting.ClearCacheActivity;
import com.lsy.wisdom.clockin.utils.APKVersionCodeUtils;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/12
 * todo : 系统设置
 */
public class SysSettingActivity extends AppCompatActivity {


    @BindView(R.id.setting_toolbar)
    IToolbar settingToolbar;
    @BindView(R.id.set_version)
    TextView setVersion;
    private SharedUtils sharedUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_setting);
        setSupportActionBar(settingToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        sharedUtils = new SharedUtils(this, SharedUtils.CLOCK);
        setVersion.setText("V" + APKVersionCodeUtils.getVerName(SysSettingActivity.this));
    }


    private void initBar() {
        settingToolbar.inflateMenu(R.menu.toolbar_menu);
        settingToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    @OnClick({R.id.set_clean, R.id.set_xieyi, R.id.set_exist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_clean:
                Intent clean = new Intent(SysSettingActivity.this, ClearCacheActivity.class);
                startActivity(clean);
                break;
            case R.id.set_xieyi:
                ToastUtils.showBottomToast(SysSettingActivity.this, "暂无相关内容");
                break;
            case R.id.set_exist:
                sharedUtils.remove_data();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();

                break;

            default:
                break;
        }
    }
}

package com.lsy.wisdom.clockin.activity.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.tools.DataCleanManager;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/14
 * todo :
 */
public class ClearCacheActivity extends AppCompatActivity {

    @BindView(R.id.cache_toolbar)
    IToolbar cacheToolbar;
    @BindView(R.id.cache_data)
    TextView cacheData;
    @BindView(R.id.clean_cache)
    Button cleanCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        setSupportActionBar(cacheToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();
        bindData();
    }


    private void initBar() {
        cacheToolbar.inflateMenu(R.menu.toolbar_menu);
        cacheToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

    private void bindData() {

        try {
            cacheData.setText("" + DataCleanManager.getTotalCacheSize(ClearCacheActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        cleanCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCleanManager.clearAllCache(ClearCacheActivity.this);
                ToastUtils.showBottomToast(ClearCacheActivity.this, "清除成功");
                try {
                    cacheData.setText("" + DataCleanManager.getTotalCacheSize(ClearCacheActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

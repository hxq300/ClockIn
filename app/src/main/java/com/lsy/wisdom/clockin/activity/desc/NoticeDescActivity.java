package com.lsy.wisdom.clockin.activity.desc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.NoticeData;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/12
 * todo : 公告详情
 */
public class NoticeDescActivity extends AppCompatActivity {


    @BindView(R.id.ndesc_toolbar)
    IToolbar ndescToolbar;
    @BindView(R.id.ndesc_title)
    TextView ndescTitle;
    @BindView(R.id.ndesc_time)
    TextView ndescTime;
    @BindView(R.id.ndesc_content)
    TextView ndescContent;

    private NoticeData noticeData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_desc);
        setSupportActionBar(ndescToolbar);
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
        String data = intent.getStringExtra("noticeData");
        Gson gson = new Gson();
        noticeData = gson.fromJson(data, NoticeData.class);

        if (noticeData.getTitle() != null) {
            ndescTitle.setText("" + noticeData.getTitle());
        }
        if (noticeData.getUptime() != null) {
            ndescTime.setText("" + noticeData.getUptime());
        }
        if (noticeData.getContent() != null) {
            ndescContent.setText("" + noticeData.getContent());
        }

    }


    private void initBar() {
        ndescToolbar.inflateMenu(R.menu.toolbar_menu);
        ndescToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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
}

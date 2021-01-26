package com.lsy.wisdom.clockin.activity.desc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.DetailsLog;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/28
 * todo : 项目日志详情
 */
public class DLogActivity extends AppCompatActivity {


    @BindView(R.id.baoxiao_toolbar)
    IToolbar baoxiaoToolbar;
    @BindView(R.id.blog_time)
    TextView blogTime;
    @BindView(R.id.dlog_reasons)
    TextView dlogReasons;
    @BindView(R.id.baoxiao_recycler)
    RecyclerView baoxiaoRecycler;
    private CommonAdapter listAdapter;

    private DetailsLog recordData;

    private List<String> images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlog_desc);
        setSupportActionBar(baoxiaoToolbar);
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
        String data = intent.getStringExtra("DetailsLog");

        //===添加图片部分
        baoxiaoRecycler.setItemViewCacheSize(100);
        baoxiaoRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        baoxiaoRecycler.setNestedScrollingEnabled(false);


        Gson gson = new Gson();
        recordData = gson.fromJson(data, DetailsLog.class);

        if (recordData != null) {
            if (recordData.getUptime() != null) {
                blogTime.setText("" + recordData.getUptime());
            }

            if (recordData.getContent() != null) {
                dlogReasons.setText("" + recordData.getContent());
            }
        }

        images = new ArrayList<>();

        listAdapter = new CommonAdapter<String>(getApplicationContext(), R.layout.view_reimburse_image, images) {
            @Override
            protected void convert(ViewHolder holder, String iamge, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                Glide.with(DLogActivity.this).load(RequestURL.OssUrl + iamge).into(image);

            }
        };

        baoxiaoRecycler.setAdapter(listAdapter);

        images.clear();
        if (recordData.getUrl() != null) {

            String jsons = recordData.getUrl().substring(1, recordData.getUrl().length() - 1);
            String str[] = jsons.split("[,]");
            for (int i = 0; i < str.length; i++) {
                L.log(str[i]);
                images.add("" + str[i].trim());
            }

            listAdapter.notifyDataSetChanged();
        }
    }

    private void initBar() {
        baoxiaoToolbar.inflateMenu(R.menu.toolbar_menu);
        baoxiaoToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

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
 * Created by lsy on 2020/6/17
 * todo : 采购详情
 */
public class DetailsDescActivity extends AppCompatActivity {

    @BindView(R.id.baoxiao_toolbar)
    IToolbar baoxiaoToolbar;
    @BindView(R.id.baoxiao_type)
    TextView baoxiaoType;
    @BindView(R.id.baoxiao_amount)
    TextView baoxiaoAmount;
    @BindView(R.id.baoxiao_reasons)
    TextView baoxiaoReasons;
    @BindView(R.id.baoxiao_recycler)
    RecyclerView baoxiaoRecycler;
    @BindView(R.id.baoxiao_until)
    TextView baoxiaoUntil;

    private RecordData recordData;

    private List<String> images;
    private CommonAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_desc);
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
        String data = intent.getStringExtra("RecordData");

        //===添加图片部分
        baoxiaoRecycler.setItemViewCacheSize(100);
        baoxiaoRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        baoxiaoRecycler.setNestedScrollingEnabled(false);


        Gson gson = new Gson();
        recordData = gson.fromJson(data, RecordData.class);

        if (recordData.getExpenses_type() != null) {
            baoxiaoType.setText("" + recordData.getProcurement_type());
        }
        if (recordData.getProject_name() != null) {
            baoxiaoUntil.setText("" + recordData.getProject_name());
        }

        baoxiaoAmount.setText("" + recordData.getProcurement_sum());

        if (recordData.getContent() != null) {
            baoxiaoReasons.setText("" + recordData.getContent());
        }

        images = new ArrayList<>();

        listAdapter = new CommonAdapter<String>(getApplicationContext(), R.layout.view_reimburse_image, images) {
            @Override
            protected void convert(ViewHolder holder, String iamge, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                Glide.with(DetailsDescActivity.this).load(RequestURL.OssUrl + iamge).into(image);

            }
        };

        baoxiaoRecycler.setAdapter(listAdapter);

        if (recordData.getProcurement_img() != null) {

            String jsons = recordData.getProcurement_img().substring(1, recordData.getProcurement_img().length() - 1);
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

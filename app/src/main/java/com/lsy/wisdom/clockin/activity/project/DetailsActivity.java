package com.lsy.wisdom.clockin.activity.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.UpdateSignedActivity;
import com.lsy.wisdom.clockin.bean.DetailsLog;
import com.lsy.wisdom.clockin.bean.ProjectData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/7/7
 * describe :  项目详情
 */
public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.details_toolbar)
    IToolbar detailsToolbar;
    @BindView(R.id.details_money)
    TextView detailsMoney;
    @BindView(R.id.details_name)
    TextView detailsName;
    @BindView(R.id.recycler_plog)
    RecyclerView recyclerPlog;
    @BindView(R.id.details_cusname)
    TextView detailsCusname;
    @BindView(R.id.details_utilname)
    TextView detailsUtilname;
    @BindView(R.id.details_startt)
    TextView detailsStartt;
    @BindView(R.id.details_end)
    TextView detailsEnd;
    @BindView(R.id.details_join_people)
    TextView detailsJoinPeople;

    private ProjectData projectData;

    private CommonAdapter listAdapter;
    private CommonAdapter imageAdapter;

    private List<DetailsLog> detailsLogs;

    private List<String> images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_desc);
        setSupportActionBar(detailsToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        images = new ArrayList<>();

        initData();
    }

    private void initData() {

        //===========
        recyclerPlog.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailsActivity.this);
        recyclerPlog.setLayoutManager(linearLayoutManager);
        recyclerPlog.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        String data = intent.getStringExtra("projectData");
        Gson gson = new Gson();
        projectData = gson.fromJson(data, ProjectData.class);

        if (projectData.getItems_name() != null) {
            detailsCusname.setText("" + projectData.getItems_name());
        }

        if (projectData.getProject_name() != null) {
            detailsUtilname.setText("" + projectData.getProject_name());
        }

        if (projectData.getStart_time() != null) {
            detailsStartt.setText("" + projectData.getStart_time());
        }

        if (projectData.getEnd_time() != null) {
            detailsEnd.setText("" + projectData.getEnd_time());
        }

        detailsMoney.setText("￥" + projectData.getAmount());

        if (projectData.getStaff_name() != null) {
            detailsName.setText("" + projectData.getStaff_name());
        }
        if (projectData.getParticipant_name() != null) {
            detailsJoinPeople.setText("" + projectData.getParticipant_name());
        }

        detailsLogs = new ArrayList<>();
        getProjectLog();

        listAdapter = new CommonAdapter<DetailsLog>(this, R.layout.item_daka_log, detailsLogs) {
            @Override
            protected void convert(ViewHolder holder, DetailsLog data, int position) {

                ImageView cardView = holder.getView(R.id.image_card);
                TextView remark = holder.getView(R.id.record_remark);
                RecyclerView baoxiaoRecycler = holder.getView(R.id.baoxiao_recycler);

                //===添加图片部分
                baoxiaoRecycler.setItemViewCacheSize(100);
                baoxiaoRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                baoxiaoRecycler.setNestedScrollingEnabled(false);

                holder.setText(R.id.dlog_time, "" + data.getUptime());
                holder.setText(R.id.dlog_content, "" + data.getContent());

                String jsons = data.getUrl().substring(1, data.getUrl().length() - 1);
                String str[] = jsons.split("[,]");
                images.clear();
                for (int i = 0; i < str.length; i++) {
                    L.log(str[i]);
                    images.add("" + str[i].trim());
                }

                baoxiaoRecycler.setAdapter(imageAdapter);

//                imageAdapter.notifyDataSetChanged();

//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent dlog = new Intent();
//                        dlog.putExtra("DetailsLog", data.toString());
//                        dlog.setClass(DetailsActivity.this, DLogActivity.class);
//                        startActivity(dlog);
//
//                    }
//                });
            }

        };

        recyclerPlog.setAdapter(listAdapter);


        imageAdapter = new CommonAdapter<String>(getApplicationContext(), R.layout.view_reimburse_image, images) {
            @Override
            protected void convert(ViewHolder holder, String iamgeurl, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                Glide.with(DetailsActivity.this).load(RequestURL.OssUrl + iamgeurl).into(image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        L.log("imageurl", "" + iamgeurl);
                    }
                });
            }
        };


    }

    private void initBar() {

        Menu menu = detailsToolbar.getMenu();
        menu.clear();

        detailsToolbar.inflateMenu(R.menu.add_update_menu);
        detailsToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;

                    case 1://项目修改

                        Intent update = new Intent();
                        update.putExtra("projectData", projectData.toString());
                        update.setClass(DetailsActivity.this, UpdateSignedActivity.class);
                        startActivity(update);

                        break;
                    case 2://添加项目日志

                        Intent intent = new Intent();
                        intent.putExtra("project_id", projectData.getId());
                        intent.setClass(DetailsActivity.this, AddProjectLogActivity.class);
                        startActivity(intent);

                        break;

                    default:
                        break;
                }
            }
        });
    }


    private void getProjectLog() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        listcanshu.put("project_id", projectData.getId());

        okHttpClass.setPostCanShu(DetailsActivity.this, RequestURL.getProjectLog, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

                //请求成功数据回调
                L.log("getProjectLog", "" + dataString);
                Gson gson = new Gson();

//                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);
                    jsonArray = new JSONArray(jsonObject.getString("data"));

                    detailsLogs.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        DetailsLog log = gson.fromJson("" + jsonArray.get(i).toString(), DetailsLog.class);
                        detailsLogs.add(log);
                    }

                    listAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getProjectLog();
    }
}

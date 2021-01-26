package com.lsy.wisdom.clockin.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.TaskData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/7/30
 * describe :  TODO
 */
public class TaskDescActivity extends AppCompatActivity {

    @BindView(R.id.tdesc_toolbar)
    IToolbar tdescToolbar;
    @BindView(R.id.task_desc_recycler)
    RecyclerView taskDescRecycler;
    @BindView(R.id.tdesc_title)
    TextView tdescTitle;
    @BindView(R.id.tdesc_content)
    TextView tdescContent;
    @BindView(R.id.tdesc_level)
    TextView tdescLevel;
    @BindView(R.id.tdesc_time)
    TextView tdescTime;
    @BindView(R.id.tdesc_chuanjianren)
    TextView tdescChuanjianren;
    @BindView(R.id.tdesc_fuzeren)
    TextView tdescFuzeren;
    @BindView(R.id.tdesc_canyuren)
    TextView tdescCanyuren;
    @BindView(R.id.btn_line)
    LinearLayout btnLine;
    @BindView(R.id.task_summary)
    TextView taskSummary;
    @BindView(R.id.task_note)
    TextView taskNote;
    @BindView(R.id.tdesc_startd)
    TextView tdescStartd;
    @BindView(R.id.tdesc_days)
    TextView tdescDays;

    private int type = 10;
    private String state = "";

    private TaskData taskData = null;
    private List<String> images;
    private CommonAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_desc);
        setSupportActionBar(tdescToolbar);
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

        if (type == 2) {
            if (state.equals("审核中")) {
                btnLine.setVisibility(View.VISIBLE);
            } else {
                btnLine.setVisibility(View.GONE);
            }
        } else {
            btnLine.setVisibility(View.GONE);
        }
        Intent intent = getIntent();
        String data = intent.getStringExtra("taskData");

        //===添加图片部分
        taskDescRecycler.setItemViewCacheSize(100);
        taskDescRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        taskDescRecycler.setNestedScrollingEnabled(false);


        Gson gson = new Gson();
        taskData = gson.fromJson(data, TaskData.class);

        if (taskData.getTask_title() != null) {
            tdescTitle.setText("标题： " + taskData.getTask_title());
        }

        if (taskData.getTask_describe() != null) {
            tdescContent.setText(taskData.getTask_describe());
        }
        if (taskData.getUptime() != null) {
            tdescStartd.setText("开始时间：" + taskData.getUptime());
        }

//        if (taskData.getTaskday() != null) {
        tdescDays.setText("所需天数：" + taskData.getTaskday());
//        }

        if (taskData.getDegree() != null) {
            tdescLevel.setText(taskData.getDegree());
        }

        if (taskData.getEnd_time() != null) {
            tdescTime.setText(taskData.getEnd_time());
        }

        if (taskData.getCreator_name() != null) {
            tdescChuanjianren.setText(taskData.getCreator_name());
        }
        if (taskData.getPrincipal_name() != null) {
            tdescFuzeren.setText(taskData.getPrincipal_name());
        }

        if (taskData.getParticipant_name() != null) {
            tdescCanyuren.setText("" + taskData.getParticipant_name());
        }

        if (taskData.getTask_summarize() != null) {
            taskSummary.setVisibility(View.VISIBLE);
            taskSummary.setText("任务总结：" + taskData.getTask_summarize());
        } else {
            taskSummary.setVisibility(View.GONE);
        }

        if (taskData.getTask_reason() != null) {
            taskNote.setVisibility(View.VISIBLE);
            taskNote.setText("任务备注：" + taskData.getTask_reason());
        } else {
            taskNote.setVisibility(View.GONE);
        }

        images = new ArrayList<>();

        listAdapter = new CommonAdapter<String>(getApplicationContext(), R.layout.view_reimburse_image, images) {
            @Override
            protected void convert(ViewHolder holder, String iamge, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                Glide.with(TaskDescActivity.this).load(RequestURL.OssUrl + iamge).into(image);

            }
        };

        taskDescRecycler.setAdapter(listAdapter);

        if (taskData.getStart_img() != null && taskData.getStart_img().length() > 10) {

            String jsons = taskData.getStart_img().substring(1, taskData.getStart_img().length() - 1);
            String str[] = jsons.split("[,]");
            for (int i = 0; i < str.length; i++) {
                L.log(str[i]);
                images.add("" + str[i].trim());
            }

            listAdapter.notifyDataSetChanged();
        }
    }


    private void initBar() {
        Menu menu = tdescToolbar.getMenu();
        menu.clear();

        Intent intent = getIntent();
        //0、我的任务  1、我参加的任务   2、我创建的任务
        type = intent.getIntExtra("type", 10);
        state = intent.getStringExtra("state");

        L.log("taskdesc", "type=" + type + "---state=" + state);

        if (type == 0) {//我的任务
            if (state.equals("进行中")) {
                tdescToolbar.inflateMenu(R.menu.add_task_menu);
            } else if (state.equals("审核中")) {
                tdescToolbar.inflateMenu(R.menu.add_task_menu1);
            }
        } else if (type == 2) {
            if (state.equals("进行中") || state.equals("审核中")) {
                tdescToolbar.inflateMenu(R.menu.add_task_menu1);
            }
        } else {
            tdescToolbar.inflateMenu(R.menu.toolbar_menu);
        }


        tdescToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://撤销
                        cancelT();
                        break;

                    case 2://完成任务
                        Intent addProcess = new Intent();
                        addProcess.putExtra("task_id", taskData.getId());
                        addProcess.setClass(TaskDescActivity.this, TaskSucessActivity.class);
                        startActivity(addProcess);
                        break;

                    case 3://客户签约
//                        Intent qianyue = new Intent();
//                        qianyue.putExtra("items_id", company.getId());
//                        qianyue.putExtra("items_name", company.getItems_name());
//                        qianyue.setClass(CustomerDescActivity.this, AddSignedActivity.class);
//                        startActivity(qianyue);
                        break;

                    default:
                        L.log("munu", "新建跟进" + pos);
                        break;
                }
            }
        });
    }

    private void cancelT() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("id", taskData.getId());

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(this, RequestURL.cancelTask, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("punchCard", "" + dataString);

                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(dataString);

                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(TaskDescActivity.this, "撤销成功");
                        finish();
                    } else {
                        ToastUtils.showBottomToast(TaskDescActivity.this, "" + message);
                    }


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

    }

    @OnClick({R.id.btn_no_green, R.id.btn_green})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_no_green:
                Intent ngreen = new Intent();
                ngreen.putExtra("task_id", taskData.getId());
                ngreen.putExtra("type", 2);
                ngreen.setClass(this, TaskPassActivity.class);
                startActivity(ngreen);

                break;
            case R.id.btn_green:
                Intent green = new Intent();
                green.putExtra("task_id", taskData.getId());
                green.putExtra("type", 0);
                green.setClass(this, TaskPassActivity.class);
                startActivity(green);
                break;
        }
    }
}

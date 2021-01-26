package com.lsy.wisdom.clockin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.refresh.FooterView;
import com.donkingliang.refresh.HeaderView;
import com.donkingliang.refresh.RefreshLayout;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.AddLogActivity;
import com.lsy.wisdom.clockin.activity.desc.LogDescActivity;
import com.lsy.wisdom.clockin.adapter.LogDataAdapter;
import com.lsy.wisdom.clockin.bean.LogData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.GroupButtonView;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
 * Created by lsy on 2020/5/8
 * todo : 日志记录
 */
public class LogActivity extends AppCompatActivity {


    @BindView(R.id.record_toolbar)
    IToolbar recordToolbar;
    @BindView(R.id.id_record_recycler)
    RecyclerView idRecordRecycler;
    @BindView(R.id.gbv_log)
    GroupButtonView gbvLog;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private int pageNum = 1;
    private int groupNum = 0;
    private List<LogData> logDataList;
    private LogDataAdapter mLogDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        setSupportActionBar(recordToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initView();

//        getData(pageNum);
    }

    private void getData(int pNum) {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:staff_id(员工id,登录返回),pageNo  pageSize
        listcanshu.put("staff_id", OKHttpClass.getUserId(LogActivity.this));
        listcanshu.put("pageNo", pNum);
        listcanshu.put("pageSize", 10);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(LogActivity.this, RequestURL.getLog, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
                //请求成功数据回调
                L.log("log", "getLog==" + dataString);
                Gson gson = new Gson();

                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);
                    jsonArray = new JSONArray(jsonObject.getString("items"));
                    if (pageNum > 1 && jsonArray.length() == 0) {
                        //加载到头 没有数据

                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LogData record = gson.fromJson("" + jsonArray.get(i).toString(), LogData.class);
                            logDataList.add(record);
                        }
                        //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
                        mLogDataAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });

    }


    private void getStaffData(int pNum) {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:staff_id(员工id,登录返回),pageNo  pageSize
        listcanshu.put("staff_id", OKHttpClass.getUserId(LogActivity.this));
        listcanshu.put("pageNo", pNum);
        listcanshu.put("pageSize", 20);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(LogActivity.this, RequestURL.getStaffLog, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                Gson gson = new Gson();

//                ToastUtils.showBottomToast(LogActivity.this, "已是最新数据");
//
////                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);
                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    if (pageNum > 1 && jsonArray.length() == 0) {
                        //加载到头 没有数据
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LogData record = gson.fromJson("" + jsonArray.get(i).toString(), LogData.class);
                            logDataList.add(record);
                        }
                        //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
                        mLogDataAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });

    }

    private void initView() {

        logDataList = new ArrayList<>();

        //===========
        idRecordRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LogActivity.this);
        idRecordRecycler.setLayoutManager(linearLayoutManager);
        idRecordRecycler.setNestedScrollingEnabled(false);
        idRecordRecycler.setFocusableInTouchMode(true);

        // todo: 修改
        mLogDataAdapter = new LogDataAdapter(logDataList);
        idRecordRecycler.setAdapter(mLogDataAdapter);

        mLogDataAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent desc = new Intent();
                desc.putExtra("logData", logDataList.get(position).toString());
                desc.setClass(LogActivity.this, LogDescActivity.class);
                startActivity(desc);
            }
        });

        // 下拉刷新
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                logDataList.clear();
                pageNum = 1;
                if (groupNum == 0) {
                    getData(pageNum);
                } else {
                    getStaffData(pageNum);
                }
            }
        });


        // 上拉加载更多
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                // 加载回调
                pageNum++;
                if (groupNum == 0) {
                    getData(pageNum);
                } else {
                    getStaffData(pageNum);
                }
            }
        });

        idRecordRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (groupNum == 0) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
                        if (!idRecordRecycler.canScrollVertically(1)) { // 到达底部
                            pageNum++;
//                        Toast.makeText(LogActivity.this, "到达底部", Toast.LENGTH_SHORT).show();
                            getData(pageNum);
                        } else if (!idRecordRecycler.canScrollVertically(-1)) { // 到达顶部
                            logDataList.clear();
                            pageNum = 1;
//                        Toast.makeText(LogActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
                            getData(pageNum);
                        }
                    } else {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
                            if (!idRecordRecycler.canScrollVertically(1)) { // 到达底部
                                pageNum++;
//                        Toast.makeText(LogActivity.this, "到达底部", Toast.LENGTH_SHORT).show();
                                getStaffData(pageNum);
                            } else if (!idRecordRecycler.canScrollVertically(-1)) { // 到达顶部
                                logDataList.clear();
                                pageNum = 1;
//                        Toast.makeText(LogActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
                                getStaffData(pageNum);
                            }
                        }
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });




        gbvLog.setOnGroupBtnClickListener(new GroupButtonView.OnGroupBtnClickListener() {
            @Override
            public void groupBtnClick(String code) {

                int open = Integer.parseInt(code);

                switch (open) {
                    case 0:
                        groupNum = 0;
                        logDataList.clear();
                        pageNum = 1;
                        getData(pageNum);
                        break;
                    case 1:
                        groupNum = 1;
                        logDataList.clear();
                        pageNum = 1;
                        getStaffData(pageNum);
                        break;
                    default:
                        break;
                }

//                Toast.makeText(GoodsDetailsActivity.this, code, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void initBar() {

        Menu menu = recordToolbar.getMenu();
        menu.clear();

//        recordToolbar.setTitle("日志记录");
        recordToolbar.removeTitleText();
        recordToolbar.inflateMenu(R.menu.add_menu);
        recordToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://新建日志

                        Intent addLog = new Intent(LogActivity.this, AddLogActivity.class);
                        startActivity(addLog);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        logDataList.clear();
        pageNum = 1;

        if (groupNum == 0) {
            getData(pageNum);
        } else {
            getStaffData(pageNum);
        }

    }
}

package com.lsy.wisdom.clockin.activity.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.refresh.FooterView;
import com.donkingliang.refresh.HeaderView;
import com.donkingliang.refresh.RefreshLayout;
import com.google.android.material.tabs.TabLayout;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.TaskData;
import com.lsy.wisdom.clockin.mvp.task.TaskInterface;
import com.lsy.wisdom.clockin.mvp.task.TaskPresent;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/7/29
 * describe :  TODO
 */
public class TaskActivity extends AppCompatActivity implements TaskInterface.View {

    @BindView(R.id.task_toolbar)
    IToolbar taskToolbar;
    @BindView(R.id.task_tablayout)
    TabLayout taskTablayout;
    @BindView(R.id.task_recycler)
    RecyclerView taskRecycler;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    private List<String> titleData = new ArrayList<>();

    private int pageNum = 1;
    private String getState = "";
    private int type = 0;//0、我的任务  1、我参加的任务   2、我创建的任务

    private TaskInterface.Presenter presenter;

    private CommonAdapter commonAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;

    private EmptyWrapper mEmptyWrapper;

    private List<TaskData> dataList = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setSupportActionBar(taskToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        presenter = new TaskPresent(this, TaskActivity.this);

        initBar();

        initView();

        addTabToTabLayout();

        //进行中,审核中,已完成,已撤销
        loadData(getState, pageNum);
    }

    private void initView() {

        dataList = new ArrayList<>();
        //===========
        taskRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskActivity.this);
        taskRecycler.setLayoutManager(linearLayoutManager);
        taskRecycler.setNestedScrollingEnabled(false);
        taskRecycler.setFocusableInTouchMode(true);

        //设置头部(刷新)
        refreshLayout.setHeaderView(new HeaderView(this));

        //设置尾部(加载更多)
        refreshLayout.setFooterView(new FooterView(this));

        //设置刷新监听，触发刷新时回调
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新回调
                dataList.clear();
                pageNum = 1;
                loadData(getState, pageNum);
                //通知刷新完成，isSuccess是否刷新成功
                refreshLayout.finishRefresh(true);
            }
        });

        //设置上拉加载更多的监听，触发加载时回调。
        //RefreshLayout默认没有启用上拉加载更多的功能，如果设置了OnLoadMoreListener，则自动启用。
        refreshLayout.setOnLoadMoreListener(new RefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 加载回调
                pageNum++;
                loadData(getState, pageNum);

            }
        });

        // 启用下拉刷新功能。默认启用
        refreshLayout.setRefreshEnable(true);

        // 启用上拉加载更多功能。默认不启用，如果设置了OnLoadMoreListener，则自动启用。
        refreshLayout.setLoadMoreEnable(true);

        // 是否还有更多数据，只有为true是才能上拉加载更多，它会回调FooterView的onHasMore()方法。默认为true。
        refreshLayout.hasMore(true);

        //自动触发下拉刷新。只有启用了下拉刷新功能时起作用。
        refreshLayout.autoRefresh();

        //自动触发上拉加载更多，在滑动到底部的时候，自动加载更多。只有在启用了上拉加载更多功能并且有更多数据时起作用。
        refreshLayout.autoLoadMore();

        ////通知刷新完成，isSuccess是否刷新成功
        //        refreshLayout.finishRefresh( boolean isSuccess);
        //
        ////通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
        //        refreshLayout.finishLoadMore( boolean isSuccess, boolean hasMore);

        // 是否自动触发加载更多。只有在启用了上拉加载更多功能时起作用。
        refreshLayout.setAutoLoadMore(true);

//        taskRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
//                    if (!taskRecycler.canScrollVertically(1)) { // 到达底部
//                        pageNum++;
//                        loadMore();
////                        Toast.makeText(LogActivity.this, "到达底部", Toast.LENGTH_SHORT).show();
//                        loadData(getState, pageNum);
//                    } else if (!taskRecycler.canScrollVertically(-1)) { // 到达顶部
////                            logDataList.clear();
//                        pageNum = 1;
////                        Toast.makeText(LogActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
//                        addHead();
//                        loadData(getState, pageNum);
//                    }
//                } else {
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
//                        if (!taskRecycler.canScrollVertically(1)) { // 到达底部
//                            pageNum++;
//                            loadMore();
////                        Toast.makeText(LogActivity.this, "到达底部", Toast.LENGTH_SHORT).show();
//                            loadData(getState, pageNum);
//                        } else if (!taskRecycler.canScrollVertically(-1)) { // 到达顶部
////                                logDataList.clear();
//                            pageNum = 1;
////                        Toast.makeText(LogActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
//                            addHead();
//                            loadData(getState, pageNum);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

        commonAdapter = new CommonAdapter<TaskData>(this, R.layout.item_task_view, dataList) {
            @Override
            protected void convert(ViewHolder holder, TaskData data, int position) {

                TextView tvState = holder.getView(R.id.task_type);

                holder.setText(R.id.task_name, "" + data.getTask_title());

                if (data.getState() != null) {
                    if (data.getState().equals("已撤销")) {
                        tvState.setTextColor(Color.parseColor("#999999"));
                    }
                    tvState.setText("" + data.getState());
                }

                holder.setText(R.id.task_principal, "负责人：" + data.getPrincipal_name());
                holder.setText(R.id.task_content, "任务描述：" + data.getTask_describe());
                holder.setText(R.id.task_degree_level, "任务级别：" + data.getDegree());
                holder.setText(R.id.task_time, "开始时间：" + data.getUptime());
                holder.setText(R.id.task_days, "所需天数：" + data.getTaskday());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent desc = new Intent();
                        desc.putExtra("type", type);
                        desc.putExtra("state", "" + data.getState());
                        desc.putExtra("taskData", data.toString());
                        desc.setClass(TaskActivity.this, TaskDescActivity.class);
                        startActivity(desc);

                    }
                });
            }

        };

    }

    private void loadData(String state, int pageNum) {

        L.log("task", "state=" + state + "-----pageNum=" + pageNum + "----type=" + type);
        //0、我的任务  1、我参加的任务   2、我创建的任务
        if (type == 0) {
            presenter.getMyResponsible("" + OKHttpClass.getUserId(this), pageNum, 30, "" + state);
        } else if (type == 1) {
            presenter.getMyJoin("" + OKHttpClass.getUserId(this), pageNum, 30, "" + state);
        } else {
            presenter.getMyCreate("" + OKHttpClass.getUserId(this), pageNum, 30, "" + state);
        }
    }


    /**
     * Description：给TabLayout添加tab
     */
    private void addTabToTabLayout() {

        titleData.add("全部");
        titleData.add("进行中");
        titleData.add("审核中");
        titleData.add("已完成");
        titleData.add("已撤销");

        for (int i = 0; i < titleData.size(); i++) {
            L.log("title", "" + titleData.get(i));
            taskTablayout.addTab(taskTablayout.newTab().setText(titleData.get(i)));
        }

        taskTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                recordDatas.clear();
                if (tab.getText().equals("全部")) {
                    pageNum = 1;
                    getState = "";
                } else {
                    pageNum = 1;
                    getState = "" + tab.getText().toString();
                }

                loadData(getState, 1);
                L.log("state", "titleState=" + getState);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                L.log("onTabUnselected", "未选中的" + tab.getText());
//                Toast.makeText(mContext, "未选中的"+tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                L.log("onTabReselected", "复选的" + tab.getText());
//                Toast.makeText(mContext, "复选的"+tab.getText(), Toast.LENGTH_SHORT).show();

            }
        });
        taskTablayout.bringToFront();
    }


    private void initBar() {

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        String title = intent.getStringExtra("title");

        taskToolbar.inflateMenu(R.menu.toolbar_menu);
        taskToolbar.setTitle("" + title);
        taskToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    private void addEmptyView() {

        mEmptyWrapper = new EmptyWrapper(commonAdapter);
        mEmptyWrapper.setEmptyView(R.layout.empty_view);

        taskRecycler.setAdapter(mEmptyWrapper);
    }

    private void addHead() {
        //添加头部
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);

        TextView t1 = new TextView(this);
        t1.setText("正在刷新数据");
//        TextView t2 = new TextView(this);
//        t2.setText("加载更多，请稍等");
        mHeaderAndFooterWrapper.addHeaderView(t1);
//        mHeaderAndFooterWrapper.addFootView(t2);

        taskRecycler.setAdapter(mHeaderAndFooterWrapper);
    }

    private void loadMore() {
        //加载更多
        mLoadMoreWrapper = new LoadMoreWrapper(commonAdapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
            }
        });

        taskRecycler.setAdapter(mLoadMoreWrapper);
        mLoadMoreWrapper.setLoadMoreView(null);
    }


    @Override
    public void setDatas(List<TaskData> taskDataList) {

        if (pageNum == 1) {
            dataList.clear();
            for (int i = 0; i < taskDataList.size(); i++) {
                dataList.add(taskDataList.get(i));
            }
        } else {
            for (int i = 0; i < taskDataList.size(); i++) {
                dataList.add(taskDataList.get(i));
            }
        }

//        commonAdapter.notifyDataSetChanged();
        if (commonAdapter != null) {
            addEmptyView();
            L.log("setSuccess", "头部置空");
        }

        if (pageNum > 1 && taskDataList.size() == 0) {
            //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
            refreshLayout.finishLoadMore(true, false);
//                        refreshLayout.showEmpty();
        } else {
            //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
            refreshLayout.finishLoadMore(true, true);
            commonAdapter.notifyDataSetChanged();
//                        refreshLayout.hideEmpty();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNum = 1;
        loadData(getState, pageNum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.distory();
        }
    }
}

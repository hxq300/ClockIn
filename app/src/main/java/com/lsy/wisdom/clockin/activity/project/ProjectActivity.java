package com.lsy.wisdom.clockin.activity.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.refresh.FooterView;
import com.donkingliang.refresh.HeaderView;
import com.donkingliang.refresh.RefreshLayout;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.ProjectData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

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
 * describe :  采购添加
 */
public class ProjectActivity extends AppCompatActivity {

    @BindView(R.id.add_leave_toolbar)
    IToolbar addLeaveToolbar;
    @BindView(R.id.recycler_project)
    RecyclerView recyclerProject;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    private int pageNum = 1;

    private List<ProjectData> projectDataList;

    private CommonAdapter commonAdapter;

    private EmptyWrapper mEmptyWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setSupportActionBar(addLeaveToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initView();

    }

    private void initView() {

        projectDataList = new ArrayList<>();

        //===========
        recyclerProject.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProjectActivity.this);
        recyclerProject.setLayoutManager(linearLayoutManager);
        recyclerProject.setNestedScrollingEnabled(false);


        //设置头部(刷新)
        refreshLayout.setHeaderView(new HeaderView(this));

        //设置尾部(加载更多)
        refreshLayout.setFooterView(new FooterView(this));

        //设置刷新监听，触发刷新时回调
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新回调
                projectDataList.clear();
                pageNum = 1;
                getData(pageNum);
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
//                loadMore();
                getData(pageNum);

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

        // 隐藏内容布局，显示空布局。
        //refreshLayout.showEmpty();

        // 隐藏空布局，显示内容布局。
        //refreshLayout.hideEmpty();

//        recyclerProject.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
//                    if (!recyclerProject.canScrollVertically(1)) { // 到达底部
//                        pageNum++;
//                        loadMore();
////                        Toast.makeText(LogActivity.this, "到达底部", Toast.LENGTH_SHORT).show();
//                        getData(pageNum);
//                    } else if (!recyclerProject.canScrollVertically(-1)) { // 到达顶部
//                        projectDataList.clear();
//                        pageNum = 1;
////                        Toast.makeText(LogActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
//                        addHead();
//                        getData(pageNum);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });


        commonAdapter = new CommonAdapter<ProjectData>(this, R.layout.item_project_view, projectDataList) {
            @Override
            protected void convert(ViewHolder holder, ProjectData projectData, int position) {
                holder.setText(R.id.project_name, "" + projectData.getItems_name());
                holder.setText(R.id.dlog_util_name, "" + projectData.getProject_name());
                holder.setText(R.id.project_time, "" + projectData.getUptime());
                holder.setText(R.id.contract_amount, "￥" + projectData.getAmount());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent con = new Intent();
                        con.putExtra("projectData", projectData.toString());
                        con.setClass(ProjectActivity.this, DetailsActivity.class);
                        startActivity(con);
                    }
                });
            }

        };

        getData(pageNum);
    }


    private void addEmptyView() {

        mEmptyWrapper = new EmptyWrapper(commonAdapter);
        mEmptyWrapper.setEmptyView(R.layout.empty_view);

        recyclerProject.setAdapter(mEmptyWrapper);
    }


    private void getData(int pageNum) {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:conglomerate_id(集团id),staff_id(负责人id),pageNo,pageSize
        listcanshu.put("staff_id", OKHttpClass.getUserId(this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("pageNo", pageNum);
        listcanshu.put("pageSize", 20);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(ProjectActivity.this, RequestURL.getProjectList, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("projectList", "getProjectList==" + dataString);
                Gson gson = new Gson();

                addEmptyView();
//                ToastUtils.showBottomToast(NoticeActivity.this, "已是最新数据");
//
////                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);
                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProjectData data = gson.fromJson(jsonArray.get(i).toString(), ProjectData.class);

                        projectDataList.add(data);
                    }

                    if (pageNum > 1 && jsonArray.length() == 0) {
                        //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
                        refreshLayout.finishLoadMore(true, false);
//                        refreshLayout.showEmpty();
                    } else {
                        //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
                        refreshLayout.finishLoadMore(true, true);
                        commonAdapter.notifyDataSetChanged();
//                        refreshLayout.hideEmpty();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });

    }


    private void initBar() {
        Menu menu = addLeaveToolbar.getMenu();
        menu.clear();

        addLeaveToolbar.inflateMenu(R.menu.toolbar_menu);
        addLeaveToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://

                        break;
                    default:
                        break;
                }
            }
        });
    }

}

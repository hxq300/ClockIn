package com.lsy.wisdom.clockin.activity;

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
import com.google.android.material.tabs.TabLayout;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.AddApprovalActivity;
import com.lsy.wisdom.clockin.activity.add.AddConversionActivity;
import com.lsy.wisdom.clockin.activity.add.AddLeaveActivity;
import com.lsy.wisdom.clockin.activity.add.AddPurchaseActivity;
import com.lsy.wisdom.clockin.activity.add.AddReimburseActivity;
import com.lsy.wisdom.clockin.activity.add.EvectionActivity;
import com.lsy.wisdom.clockin.activity.add.ExtraWorkActivity;
import com.lsy.wisdom.clockin.activity.approval.AddWorkActivity;
import com.lsy.wisdom.clockin.activity.approval.ApprovalActivity;
import com.lsy.wisdom.clockin.activity.approval.BusinessActivity;
import com.lsy.wisdom.clockin.activity.approval.ConversionActivity;
import com.lsy.wisdom.clockin.activity.approval.LeaveActivity;
import com.lsy.wisdom.clockin.activity.approval.PurchaseActivity;
import com.lsy.wisdom.clockin.activity.approval.ReimburseActivity;
import com.lsy.wisdom.clockin.activity.desc.AddWorkDescActivity;
import com.lsy.wisdom.clockin.activity.desc.BaoXiaoDescActivity;
import com.lsy.wisdom.clockin.activity.desc.ChuChaiDescActivity;
import com.lsy.wisdom.clockin.activity.desc.ConversionDescActivity;
import com.lsy.wisdom.clockin.activity.desc.DetailsDescActivity;
import com.lsy.wisdom.clockin.activity.desc.LeaveDescActivity;
import com.lsy.wisdom.clockin.activity.desc.ShenPiDescActivity;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.mvp.record.RecordInterface;
import com.lsy.wisdom.clockin.mvp.record.RecordPresent;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.GroupButtonView;
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
 * Created by lsy on 2020/5/8
 * todo : 记录
 */
public class RecordActivity extends AppCompatActivity implements RecordInterface.View {

    @BindView(R.id.record_toolbar)
    IToolbar recordToolbar;
    @BindView(R.id.id_record_recycler)
    RecyclerView idRecordRecycler;
    @BindView(R.id.record_tablayout)
    TabLayout recordTablayout;
    @BindView(R.id.gbv_record)
    GroupButtonView gbvRecord;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    private List<String> titleData = new ArrayList<>();

    private CommonAdapter commonAdapter;
    private List<RecordData> recordDatas = null;

    private int type = 10;

    private RecordInterface.Presenter presenter;
    private int pageNum = 1;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;

    private EmptyWrapper mEmptyWrapper;

    private String titleState = "待审核";
    private int open = 0; // 0 我创建的 1 提交给我的

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setSupportActionBar(recordToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 10);

        presenter = new RecordPresent(this, RecordActivity.this);

        initBar();

        initView();
        addTabToTabLayout();

        loadData(titleState, pageNum);

    }

    /**
     *
     * @param state 审核状态 （待审核 / 已审批 / 已驳回）
     * @param pageNum 分页加载 页码
     */
    private void loadData(String state, int pageNum) {

        L.log("open", "open====" + open);
        if (open == 0) {
            switch (type) {
                case 0://审批
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "审批", pageNum);
                    break;
                case 1://出差
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "出差", pageNum);
                    break;
                case 2://加班
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "加班", pageNum);
                    break;
                case 3://采购
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "报销", pageNum);
                    break;
                case 4://请假
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "请假", pageNum);
                    break;
                case 5://报销
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "报销", pageNum);
                    break;

                case 6://采购
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "采购", pageNum);
                    break;
                case 7://采购
                    presenter.getRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "转正", pageNum);
                    break;

                default:
                    break;
            }
        } else {
            switch (type) {
                case 0://审批
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "审批", pageNum);
                    break;
                case 1://出差
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "出差", pageNum);
                    break;
                case 2://加班
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "加班", pageNum);
                    break;
                case 3://采购
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "报销", pageNum);
                    break;
                case 4://请假
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "请假", pageNum);
                    break;

                case 5://报销
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "报销", pageNum);
                    break;

                case 6://采购
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "采购", pageNum);
                    break;
                case 7://转正
                    presenter.getStaffRecord(state, OKHttpClass.getUserId(RecordActivity.this), pageNum, 10, "转正", pageNum);
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("待审核".equals(titleState))
        loadData(titleState, 1);
    }

    /**
     * Description：给TabLayout添加tab
     */
    private void addTabToTabLayout() {

        titleData.add("待审批");
        titleData.add("已审批");
        titleData.add("已驳回");

        for (int i = 0; i < titleData.size(); i++) {
            L.log("title", "" + titleData.get(i));
            recordTablayout.addTab(recordTablayout.newTab().setText(titleData.get(i)));
        }

        recordTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                recordDatas.clear();
                if (tab.getText().equals("待审批")) {
                    pageNum = 1;
                    titleState = "待审核";
                } else if (tab.getText().equals("已审批")) {
                    pageNum = 1;
                    titleState = "通过";
                } else {
                    pageNum = 1;
                    titleState = "未通过";
                }

                loadData(titleState, 1);
                L.log("state", "titleState=" + titleState);

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
        recordTablayout.bringToFront();
    }


    private void initView() {

        //===========
        idRecordRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecordActivity.this);
        idRecordRecycler.setLayoutManager(linearLayoutManager);
        idRecordRecycler.setNestedScrollingEnabled(false);
        idRecordRecycler.setFocusableInTouchMode(false);

        recordDatas = new ArrayList<>();


        //设置头部(刷新)
        refreshLayout.setHeaderView(new HeaderView(this));

        //设置尾部(加载更多)
        refreshLayout.setFooterView(new FooterView(this));

        //设置刷新监听，触发刷新时回调
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新回调
                recordDatas.clear();
                pageNum = 1;
                loadData(titleState, pageNum);
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

                loadData(titleState, pageNum);
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


//        idRecordRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
//                    if (!idRecordRecycler.canScrollVertically(1)) { // 到达底部
//                        pageNum++;
//                        if (recyclerView != null) {
//                            loadMore();
//                        }
//
//                        loadData(titleState, pageNum);
//                    } else if (!idRecordRecycler.canScrollVertically(-1)) { // 到达顶部
//                        recordDatas.clear();
//                        pageNum = 1;
////                        Toast.makeText(RecordActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
//                        if (recyclerView != null) {
//                            addHead();
//                        }
//                        loadData(titleState, pageNum);
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });


        commonAdapter = new CommonAdapter<RecordData>(this, R.layout.item_record_view, recordDatas) {
            @Override
            protected void convert(ViewHolder holder, RecordData recordData, int position) {
                holder.setText(R.id.record_name, "" + recordData.getStaff_name());
                holder.setText(R.id.record_time, "" + recordData.getUptime());
                holder.setText(R.id.record_desc, "" + recordData.getContent());
                holder.setText(R.id.record_progress, "" + recordData.getState());
                holder.setText(R.id.record_type, "" + recordData.getExamine_type());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                            Toast.makeText(RecordActivity.this, "点击了item" + position, Toast.LENGTH_SHORT).show();

                        switch (type) {
                            case 0://审批详情
//                                    Intent aDesc = new Intent(RecordActivity.this, ApprovalDescActivity.class);
//                                    startActivity(aDesc);
                                if (open == 1 && titleState.equals("待审核")) {
                                    Intent approval = new Intent();
                                    approval.putExtra("RecordData", recordData.toString());
                                    approval.setClass(RecordActivity.this, ApprovalActivity.class);
                                    startActivity(approval);
                                } else {
                                    Intent shenpi = new Intent();
                                    shenpi.putExtra("RecordData", recordData.toString());
                                    shenpi.setClass(RecordActivity.this, ShenPiDescActivity.class);
                                    startActivity(shenpi);
                                }

                                break;
                            case 1://出差
                                if (open == 1 && titleState.equals("待审核")) {
                                    Intent business = new Intent();
                                    business.putExtra("RecordData", recordData.toString());
                                    business.setClass(RecordActivity.this, BusinessActivity.class);
                                    startActivity(business);
                                } else {
                                    Intent chuchai = new Intent();
                                    chuchai.putExtra("RecordData", recordData.toString());
                                    chuchai.setClass(RecordActivity.this, ChuChaiDescActivity.class);
                                    startActivity(chuchai);
                                }


                                break;
                            case 2://加班
                                if (open == 1 && titleState.equals("待审核")) {
                                    Intent spaw = new Intent();
                                    spaw.putExtra("RecordData", recordData.toString());
                                    spaw.setClass(RecordActivity.this, AddWorkActivity.class);
                                    startActivity(spaw);
                                } else {
                                    Intent addesc = new Intent();
                                    addesc.putExtra("RecordData", recordData.toString());
                                    addesc.setClass(RecordActivity.this, AddWorkDescActivity.class);
                                    startActivity(addesc);
                                }
                                break;
                            case 3://客户
                                recordToolbar.setTitle("采购记录");
                                break;

                            case 4://请假
                                if (open == 1 && titleState.equals("待审核")) {
                                    Intent spL = new Intent();
                                    spL.putExtra("RecordData", recordData.toString());
                                    spL.setClass(RecordActivity.this, LeaveActivity.class);
                                    startActivity(spL);
                                } else {
                                    Intent leave = new Intent();
                                    leave.putExtra("RecordData", recordData.toString());
                                    leave.setClass(RecordActivity.this, LeaveDescActivity.class);
                                    startActivity(leave);
                                }

                                break;

                            case 5://报销
                                if (open == 1 && titleState.equals("待审核")) {
                                    Intent reimburse = new Intent();
                                    reimburse.putExtra("RecordData", recordData.toString());
                                    reimburse.setClass(RecordActivity.this, ReimburseActivity.class);
                                    startActivity(reimburse);
                                } else {
                                    Intent baoxiao = new Intent();
                                    baoxiao.putExtra("RecordData", recordData.toString());
                                    baoxiao.setClass(RecordActivity.this, BaoXiaoDescActivity.class);
                                    startActivity(baoxiao);
                                }

                                break;


                            case 6://采购

                                if (open == 1 && titleState.equals("待审核")) {
                                    Intent spP = new Intent();
                                    spP.putExtra("RecordData", recordData.toString());
                                    spP.setClass(RecordActivity.this, PurchaseActivity.class);
                                    startActivity(spP);
                                } else {
                                    Intent caigou = new Intent();
                                    caigou.putExtra("RecordData", recordData.toString());
                                    caigou.setClass(RecordActivity.this, DetailsDescActivity.class);
                                    startActivity(caigou);
                                }

                                break;


                            case 7://转正

                                if (open == 1 && titleState.equals("待审核")) {
                                    Intent spZZ = new Intent();
                                    spZZ.putExtra("RecordData", recordData.toString());
                                    spZZ.setClass(RecordActivity.this, ConversionActivity.class);
                                    startActivity(spZZ);
                                } else {
                                    Intent con = new Intent();
                                    con.putExtra("RecordData", recordData.toString());
                                    con.setClass(RecordActivity.this, ConversionDescActivity.class);
                                    startActivity(con);
                                }

                                break;

                            default:
                                break;
                        }

                    }
                });
            }

        };

        idRecordRecycler.setAdapter(commonAdapter);

        gbvRecord.setOnGroupBtnClickListener(new GroupButtonView.OnGroupBtnClickListener() {
            @Override
            public void groupBtnClick(String code) {
                recordDatas.clear();
                open = Integer.parseInt(code);

                switch (open) {
                    case 0:
                        pageNum = 1;
                        loadData(titleState, 1);
                        break;
                    case 1:
                        pageNum = 1;
                        loadData(titleState, 1);
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

        recordToolbar.removeTitleText();

//        switch (type) {
////            审批,请假,加班,出差,报销
//            case 0://审批
//                presenter.getRecord(OKHttpClass.getUserId(RecordActivity.this), 1, 10, "审批", 1);
//                break;
//            case 1://出差
////                presenter.getRecord(OKHttpClass.getUserId(RecordActivity.this), 1, 10, "出差", 1);
//                break;
//            case 2://加班
//                presenter.getRecord(OKHttpClass.getUserId(RecordActivity.this), 1, 10, "加班", 1);
//                break;
//            case 3://采购
//                presenter.getRecord(OKHttpClass.getUserId(RecordActivity.this), 1, 10, "报销", 1);
//                break;
//
//            case 4://请假
//                presenter.getRecord(OKHttpClass.getUserId(RecordActivity.this), 1, 10, "请假", 1);
//                break;
//            case 5://报销
//                presenter.getRecord(OKHttpClass.getUserId(RecordActivity.this), 1, 10, "报销", 1);
//                break;
//
//            default:
//                break;
//        }

        recordToolbar.inflateMenu(R.menu.add_menu);
        recordToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://添加

                        switch (type) {
                            case 0://审批
                                Intent shenpi = new Intent(RecordActivity.this, AddApprovalActivity.class);
                                startActivity(shenpi);
                                break;
                            case 1://出差
                                Intent chuchai = new Intent(RecordActivity.this, EvectionActivity.class);
                                startActivity(chuchai);
                                break;
                            case 2://加班
                                Intent extraWork = new Intent(RecordActivity.this, ExtraWorkActivity.class);
                                startActivity(extraWork);
                                break;
                            case 3://采购
//                                recordToolbar.setTitle("采购记录");
                                break;

                            case 4://请假
                                Intent addLeace = new Intent(RecordActivity.this, AddLeaveActivity.class);
                                startActivity(addLeace);
                                break;

                            case 5://报销
                                Intent addBaoxiao = new Intent(RecordActivity.this, AddReimburseActivity.class);
                                startActivity(addBaoxiao);
                                break;

                            case 6://采购
                                Intent caigou = new Intent(RecordActivity.this, AddPurchaseActivity.class);
                                startActivity(caigou);
                                break;
                            case 7://转正
                                Intent zhuanzheng = new Intent(RecordActivity.this, AddConversionActivity.class);
                                startActivity(zhuanzheng);
                                break;

                            default:
                                break;
                        }

                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void setFailure(String message) {

    }

    @Override
    public void setSuccess(List<RecordData> datas, int lei) {

        L.log("setSuccess", "数据返回");
        if (lei == 1 && datas.size() > 0) {
            L.log("setSuccess", "第一页数据");
            recordDatas.clear();
            for (int i = 0; i < datas.size(); i++) {
                recordDatas.add(datas.get(i));
            }
        } else {
            L.log("setSuccess", "其他页数据");
            for (int i = 0; i < datas.size(); i++) {
                recordDatas.add(datas.get(i));
            }
        }

//        L.log("setSuccess", "数据刷新" + recordDatas.toString());
//        commonAdapter.notifyDataSetChanged();
        if (commonAdapter != null) {
            addEmptyView();
            L.log("setSuccess", "头部置空");
        }

        if (pageNum > 1 && datas.size() == 0) {
            //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
            refreshLayout.finishLoadMore(true, false);
//                        refreshLayout.showEmpty();
        } else {
            //通知加载完成，isSuccess是否加载成功，hasMore是否还有更多数据
            refreshLayout.finishLoadMore(true, true);
            commonAdapter.notifyDataSetChanged();
//                        refreshLayout.hideEmpty();
        }

//        ToastUtils.showBottomToast(this, "数据返回成功" + datas.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.distory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadData(titleState, pageNum);
    }

    private void addEmptyView() {

        mEmptyWrapper = new EmptyWrapper(commonAdapter);
        mEmptyWrapper.setEmptyView(R.layout.empty_view);

        idRecordRecycler.setAdapter(mEmptyWrapper);

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

        idRecordRecycler.setAdapter(mHeaderAndFooterWrapper);

        mHeaderAndFooterWrapper.notifyDataSetChanged();
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

        idRecordRecycler.setAdapter(mLoadMoreWrapper);
        mLoadMoreWrapper.setLoadMoreView(null);
    }


}

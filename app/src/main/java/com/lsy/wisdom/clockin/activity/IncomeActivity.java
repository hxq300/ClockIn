package com.lsy.wisdom.clockin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.adapter.PayIncomeAdapter;
import com.lsy.wisdom.clockin.bean.PayIncomeEntity;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.GroupButtonView;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by hxq on 2021/3/3
 * describe :  TODO
 */
public class IncomeActivity extends AppCompatActivity {
    @BindView(R.id.gbv_record)
    GroupButtonView gbvRecord;
    @BindView(R.id.record_toolbar)
    IToolbar recordToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.incomeRecycler)
    RecyclerView mIncomeRecycler;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;

    private int pageNum = 1;
    private PayIncomeAdapter mPayIncomeAdapter;
    private List<PayIncomeEntity.ItemsBean> mPayIncomeList;
    private int type = 0; // 0 费用收支明细 1 付款申请明细

    private List<String> titleData = new ArrayList<>();
    private String titleState = "待审核";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initView();
        addTabToTabLayout();
        initAdapter();
    }

    /**
     * Description：给TabLayout添加tab
     */
    private void addTabToTabLayout() {

        titleData.add("待审批");
        titleData.add("通过");
        titleData.add("驳回");

        for (int i = 0; i < titleData.size(); i++) {
            L.log("title", "" + titleData.get(i));
            tablayout.addTab(tablayout.newTab().setText(titleData.get(i)));
        }

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mPayIncomeList.clear();
                if (tab.getText().equals("待审批")) {
                    pageNum = 1;
                    titleState = "待审核";
                } else if (tab.getText().equals("通过")) {
                    pageNum = 1;
                    titleState = "通过";
                } else {
                    pageNum = 1;
                    titleState = "驳回";
                }

                getDetailData();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                L.log("onTabUnselected", "未选中的" + tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                L.log("onTabReselected", "复选的" + tab.getText());

            }
        });
//        tablayout.bringToFront();
    }


    // 初始化 适配器
    private void initAdapter() {
        mPayIncomeList = new ArrayList<>();
        mPayIncomeAdapter = new PayIncomeAdapter(mPayIncomeList);
        mIncomeRecycler.setLayoutManager(new LinearLayoutManager(this));
        mIncomeRecycler.setAdapter(mPayIncomeAdapter);

        // 下拉
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                mPayIncomeList.clear();
                if (type == 0) { // 费用收支明细
                    getDetailData();
                } else { // 付款申请明细
                    getApplyData();
                }
            }
        });
        // 上拉
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                if (type == 0) { // 费用收支明细
                    getApplyData();
                } else { // 付款申请明细
                    getDetailData();

                }
            }
        });

        // 条目点击是事件
        mPayIncomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(IncomeActivity.this, IncomePayDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", mPayIncomeList.get(position));
                intent.putExtras(bundle);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    // 初始化视图
    private void initView() {

        recordToolbar.inflateMenu(R.menu.add_menu);
        recordToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        break;
                    case 1://添加 (传参 根据参数判断 不同明细的添加)
                        Intent intent = new Intent(IncomeActivity.this, AddIncomeActivity.class);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });


        gbvRecord.setOnGroupBtnClickListener(new GroupButtonView.OnGroupBtnClickListener() {
            @Override
            public void groupBtnClick(String code) {
                int open = Integer.parseInt(code);

                switch (open) {
                    case 0:
                        pageNum = 1;
                        type = 0;
                        mPayIncomeList.clear();
                        tablayout.setVisibility(View.VISIBLE);
                        getDetailData();// 费用申请明细
                        break;
                    case 1:
                        pageNum = 1;
                        type = 1;
                        mPayIncomeList.clear();
                        tablayout.setVisibility(View.GONE);
                        getApplyData(); // 付款收支明细
                        break;
                    default:
                        break;
                }
            }
        });


    }

    // 申请  todo ： 根据不同状态 获取不同数据
    private void getDetailData() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:staff_id(员工id,登录返回),pageNo  pageSize
        listcanshu.put("staff_id", OKHttpClass.getUserId(IncomeActivity.this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("pageNo", pageNum);
        listcanshu.put("check", titleState);
        listcanshu.put("pageSize", 20);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(IncomeActivity.this, RequestURL.payRecord, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                Gson gson = new Gson();
                PayIncomeEntity payIncomeEntity = gson.fromJson(dataString, PayIncomeEntity.class);
                if (payIncomeEntity.getItems().size() != 0) {
                    mPayIncomeList.addAll(payIncomeEntity.getItems());
                }
                mPayIncomeAdapter.notifyDataSetChanged();
                mSmartRefreshLayout.finishRefresh();
                mSmartRefreshLayout.finishLoadmore();
                return dataString;
            }
        });

    }

    // 明细
    private void getApplyData() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:staff_id(员工id,登录返回),pageNo  pageSize
        listcanshu.put("staff_id", OKHttpClass.getUserId(IncomeActivity.this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("pageNo", pageNum);
        listcanshu.put("pageSize", 20);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(IncomeActivity.this, RequestURL.payApplyFor, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                Gson gson = new Gson();
                PayIncomeEntity payIncomeEntity = gson.fromJson(dataString, PayIncomeEntity.class);
                if (payIncomeEntity.getItems().size() != 0) {
                    mPayIncomeList.addAll(payIncomeEntity.getItems());
                }
                mPayIncomeAdapter.notifyDataSetChanged();
                mSmartRefreshLayout.finishRefresh();
                mSmartRefreshLayout.finishLoadmore();
                return dataString;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPayIncomeAdapter != null) {
            mPayIncomeList.clear();
            if (type == 1) {
                getApplyData();
            } else if (type == 0) {
                getDetailData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

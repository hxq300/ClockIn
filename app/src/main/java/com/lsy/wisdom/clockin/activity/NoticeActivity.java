package com.lsy.wisdom.clockin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.desc.NoticeDescActivity;
import com.lsy.wisdom.clockin.bean.LogData;
import com.lsy.wisdom.clockin.bean.NoticeData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
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
 * Created by lsy on 2020/5/7
 * todo :
 */
public class NoticeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    IToolbar toolbar;
    @BindView(R.id.id_notice_recycler)
    RecyclerView idNoticeRecycler;

    private List<NoticeData> noticeDatas = null;

    private CommonAdapter commonAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;

    private EmptyWrapper mEmptyWrapper;

    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initView();

        getData();
    }


    private void initView() {

        noticeDatas = new ArrayList<>();
        noticeDatas.clear();

        //===========
        idNoticeRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NoticeActivity.this);
        idNoticeRecycler.setLayoutManager(linearLayoutManager);
        idNoticeRecycler.setNestedScrollingEnabled(false);


        idNoticeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
                    if (!idNoticeRecycler.canScrollVertically(1)) { // 到达底部
                        pageNum++;
                        loadMore();
//                        Toast.makeText(NoticeActivity.this, "到达底部", Toast.LENGTH_SHORT).show();
                        getData();
                    } else if (!idNoticeRecycler.canScrollVertically(-1)) { // 到达顶部
                        noticeDatas.clear();
                        pageNum = 1;
//                        Toast.makeText(NoticeActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
                        addHead();
                        getData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        commonAdapter = new CommonAdapter<NoticeData>(this, R.layout.item_notice_view, noticeDatas) {
            @Override
            protected void convert(ViewHolder holder, NoticeData noticeData, int position) {
                holder.setText(R.id.notice_title, "" + noticeData.getTitle());
                holder.setText(R.id.notice_time, "" + noticeData.getUptime());
                holder.setText(R.id.notice_count, "" + noticeData.getRead_count());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getCount(noticeData.getId());

                        Intent desc = new Intent();
                        desc.putExtra("noticeData", "" + noticeData.toString());
                        desc.setClass(NoticeActivity.this, NoticeDescActivity.class);
                        startActivity(desc);


//                        Toast.makeText(NoticeActivity.this, "点击了item" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };

        idNoticeRecycler.setAdapter(commonAdapter);

    }


    private void getData() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:conglomerate_id(id,登录返回),pageNo  pageSize
        listcanshu.put("staff_id", "" + OKHttpClass.getUserId(this));
        listcanshu.put("conglomerate_id", "" + OKHttpClass.getConglomerate(this));
        listcanshu.put("pageNo", pageNum);
        listcanshu.put("pageSize", 10);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(NoticeActivity.this, RequestURL.getNotice, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("notice", "getNotice==" + dataString);
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
                        NoticeData notice = gson.fromJson("" + jsonArray.get(i).toString(), NoticeData.class);
                        noticeDatas.add(notice);
                    }

                    commonAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });

    }

    private void getCount(int id) {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        listcanshu.put("id", id);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(NoticeActivity.this, RequestURL.addCount, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("notice", "addCount==" + dataString);
//                Gson gson = new Gson();

//                addEmptyView();
////                ToastUtils.showBottomToast(NoticeActivity.this, "已是最新数据");
////
//////                //{"message":"打卡成功","data":null,"code":200}
//                JSONObject jsonObject = null;
//                JSONArray jsonArray = null;
//                try {
//                    jsonObject = new JSONObject(dataString);
//                    jsonArray = new JSONArray(jsonObject.getString("items"));
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        NoticeData notice = gson.fromJson("" + jsonArray.get(i).toString(), NoticeData.class);
//                        noticeDatas.add(notice);
//                    }
//
//                    commonAdapter.notifyDataSetChanged();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                return dataString;
            }
        });

    }

    private void initBar() {
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
//                    case 1:
//                        Log.v("TTT", "公告");
//                        finish();
//                        break;
//                    case 2:
//                        Log.v("TTT", "菜单2");
//                        break;
//                    case 3:
//                        Log.v("TTT", "菜单3");
//                        break;

                    default:
                        break;
                }
            }
        });
    }


    private void addEmptyView() {

        mEmptyWrapper = new EmptyWrapper(commonAdapter);
        mEmptyWrapper.setEmptyView(R.layout.empty_view);

        idNoticeRecycler.setAdapter(mEmptyWrapper);
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

        idNoticeRecycler.setAdapter(mHeaderAndFooterWrapper);

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

        idNoticeRecycler.setAdapter(mLoadMoreWrapper);
        mLoadMoreWrapper.setLoadMoreView(null);
    }

}

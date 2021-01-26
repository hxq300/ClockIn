package com.lsy.wisdom.clockin.activity.desc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.UserData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
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
 * Created by lsy on 2020/5/31
 * todo : 分享列表
 */
public class ShareActivity extends AppCompatActivity {

    @BindView(R.id.share_toolbar)
    IToolbar shareToolbar;
    @BindView(R.id.share_recycler)
    RecyclerView shareRecycler;

    private int pageNum = 1;

    private List<UserData> userList;

    private CommonAdapter commonAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;

    private EmptyWrapper mEmptyWrapper;

    private int items_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setSupportActionBar(shareToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initRecycle();

        getData(pageNum);
    }

    private void getData(int num) {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("pageNo", num);
        listcanshu.put("pageSize", 50);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(this, RequestURL.getStaff, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

//                recordDataList = new ArrayList<>();
//                //请求成功数据回调
                L.log("getRecord", "" + dataString);
//
                Gson gson = new Gson();
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    jsonArray = new JSONArray(jsonObject.getString("items"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        UserData data = gson.fromJson(jsonArray.get(i).toString(), UserData.class);
                        userList.add(data);
                    }

                    commonAdapter.notifyDataSetChanged();
                    addEmptyView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }

    private void initRecycle() {

        userList = new ArrayList<>();
        //===========
        shareRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShareActivity.this);
        shareRecycler.setLayoutManager(linearLayoutManager);
        shareRecycler.setNestedScrollingEnabled(false);
        shareRecycler.setFocusableInTouchMode(true);

        shareRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动
                    if (!shareRecycler.canScrollVertically(1)) { // 到达底部
                        pageNum++;
                        loadMore();
//                        Toast.makeText(ShareActivity.this, "到达底部", Toast.LENGTH_SHORT).show();
                        getData(pageNum);
                    } else if (!shareRecycler.canScrollVertically(-1)) { // 到达顶部
                        userList.clear();
                        pageNum = 1;
//                        Toast.makeText(ShareActivity.this, "到达顶部", Toast.LENGTH_SHORT).show();
                        addHead();
                        getData(pageNum);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        commonAdapter = new CommonAdapter<UserData>(this, R.layout.item_user_view, userList) {
            @Override
            protected void convert(ViewHolder holder, UserData data, int position) {

                TextView uTex = holder.itemView.findViewById(R.id.user_name);

                uTex.setText("" + data.getStaff_name());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (GeneralMethod.isFastClick()) {
                            uTex.setTextColor(0xffFF1D1D);
                            addPush(data.getId());
                        }

                    }
                });
            }

        };


    }


    private void addEmptyView() {

        mEmptyWrapper = new EmptyWrapper(commonAdapter);
        mEmptyWrapper.setEmptyView(R.layout.empty_view);

        shareRecycler.setAdapter(mEmptyWrapper);
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

        shareRecycler.setAdapter(mHeaderAndFooterWrapper);
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

        shareRecycler.setAdapter(mLoadMoreWrapper);
        mLoadMoreWrapper.setLoadMoreView(null);
    }

    private void initBar() {

        Intent intent = getIntent();
        items_id = intent.getIntExtra("id", 0);

        Menu menu = shareToolbar.getMenu();
        menu.clear();

        shareToolbar.inflateMenu(R.menu.toolbar_menu);
        shareToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    public void addPush(int staff_id) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

//        传参:staff_id(被推送人的id,滚动选择),items_id(客户id)

        listcanshu.put("staff_id", staff_id);
        listcanshu.put("items_id", items_id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(ShareActivity.this, RequestURL.addPush, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("customerQuery", "" + dataString);

//                Gson gson = new Gson();

                //{"message":"客户添加成功,暂无联系人!","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(ShareActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(ShareActivity.this, "" + message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return dataString;
            }
        });
    }


}

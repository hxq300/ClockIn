package com.lsy.wisdom.clockin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.SysData;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/14
 * todo : 系统消息
 */
public class SysMessageActivity extends AppCompatActivity {


    @BindView(R.id.sys_message_toolbar)
    IToolbar sysMessageToolbar;
    @BindView(R.id.sys_message_recycler)
    RecyclerView sysMessageRecycler;
    private List<SysData> sysDataList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_message);
        setSupportActionBar(sysMessageToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        //===========
        sysMessageRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SysMessageActivity.this);
        sysMessageRecycler.setLayoutManager(linearLayoutManager);
        sysMessageRecycler.setNestedScrollingEnabled(false);

        initData();
    }

    private void initData() {
        sysDataList = new ArrayList<>();
        sysDataList.clear();
        for (int i = 0; i < 16; i++) {
            SysData data = new SysData();
            data.setTime("2020-03-12 15:30:15");
            data.setTitle("系统提示" + i);
            data.setContent("检测到新版本V10.2，为方便您的使用，建议您尽快更新！");

            sysDataList.add(data);
        }

        sysMessageRecycler.setAdapter(new CommonAdapter<SysData>(this, R.layout.item_sys_message_view, sysDataList) {
            @Override
            protected void convert(ViewHolder holder, SysData data, int position) {
                holder.setText(R.id.smes_time, "" + data.getTime());
                holder.setText(R.id.smes_title, "" + data.getTitle());
                holder.setText(R.id.smes_content, "" + data.getContent());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.showBottomToast(SysMessageActivity.this, "点击了item" + position);
                    }
                });
            }

        });

    }

    private void initBar() {

        Menu menu = sysMessageToolbar.getMenu();
        menu.clear();

        sysMessageToolbar.inflateMenu(R.menu.toolbar_menu);
        sysMessageToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
//                    case 1://添加客户
//                        Intent addCustomer = new Intent(SysMessageActivity.this, AddCustomerlActivity.class);
//                        startActivity(addCustomer);
//
//                        break;

                    default:
                        break;
                }
            }
        });
    }

}

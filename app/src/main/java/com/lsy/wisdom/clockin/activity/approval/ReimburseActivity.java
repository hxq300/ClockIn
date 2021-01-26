package com.lsy.wisdom.clockin.activity.approval;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.desc.BaoXiaoDescActivity;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.mvp.shenpi.ApprovalInterface;
import com.lsy.wisdom.clockin.mvp.shenpi.ApprovalPresenter;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/6/23
 * todo : 报销
 */
public class ReimburseActivity extends AppCompatActivity implements ApprovalInterface.View {

    @BindView(R.id.approval_toolbar)
    IToolbar approvalToolbar;
    @BindView(R.id.baoxiao_type)
    TextView baoxiaoType;
    @BindView(R.id.baoxiao_amount)
    TextView baoxiaoAmount;
    @BindView(R.id.baoxiao_reasons)
    TextView baoxiaoReasons;
    @BindView(R.id.baoxiao_recycler)
    RecyclerView baoxiaoRecycler;


    private ApprovalInterface.Presenter presenter;

    private RecordData recordData;
    private List<String> images;
    private CommonAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse);
        setSupportActionBar(approvalToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initData();

        presenter = new ApprovalPresenter(this, ReimburseActivity.this);

    }

    private void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("RecordData");


        //===添加图片部分
        baoxiaoRecycler.setItemViewCacheSize(100);
        baoxiaoRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        baoxiaoRecycler.setNestedScrollingEnabled(false);

        Gson gson = new Gson();
        recordData = gson.fromJson(data, RecordData.class);

        if (recordData.getExpenses_type() != null) {
            baoxiaoType.setText("" + recordData.getExpenses_type());
        }

        baoxiaoAmount.setText("" + recordData.getExpenses_sum());

        if (recordData.getContent() != null) {
            baoxiaoReasons.setText("" + recordData.getContent());
        }

        images = new ArrayList<>();

        listAdapter = new CommonAdapter<String>(getApplicationContext(), R.layout.view_reimburse_image, images) {
            @Override
            protected void convert(ViewHolder holder, String iamge, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                Glide.with(ReimburseActivity.this).load(RequestURL.OssUrl + iamge).into(image);
            }
        };

        baoxiaoRecycler.setAdapter(listAdapter);

        if (recordData.getExpenses_picture() != null) {

            String jsons = recordData.getExpenses_picture().substring(1, recordData.getExpenses_picture().length() - 1);
            String str[] = jsons.split("[,]");
            for (int i = 0; i < str.length; i++) {
                L.log(str[i]);
                images.add("" + str[i].trim());
            }

            listAdapter.notifyDataSetChanged();
        }

    }


    private void initBar() {
        approvalToolbar.inflateMenu(R.menu.toolbar_menu);
        approvalToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

    @Override
    public void setSuccess() {
        ToastUtils.showBottomToast(this, "审批成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.distory();
    }

    @OnClick({R.id.btn_green, R.id.btn_no_green})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_green:
                if (GeneralMethod.isFastClick()) {
                    presenter.sendPass("" + recordData.getId());
                }
                break;
            case R.id.btn_no_green:
                if (GeneralMethod.isFastClick()) {
                    showTwo();
                }

                break;
        }
    }

    /**
     * 两个按钮的 dialog
     */
    private AlertDialog.Builder builder;

    private void showTwo() {

        builder = new AlertDialog.Builder(this)
                .setMessage("确定将对方的申请驳回吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        presenter.sendNoPass("" + recordData.getId());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

}

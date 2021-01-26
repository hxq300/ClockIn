package com.lsy.wisdom.clockin.activity.approval;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.RecordData;
import com.lsy.wisdom.clockin.mvp.shenpi.ApprovalInterface;
import com.lsy.wisdom.clockin.mvp.shenpi.ApprovalPresenter;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/6/23
 * todo :
 */
public class ApprovalActivity extends AppCompatActivity implements ApprovalInterface.View {

    @BindView(R.id.approval_toolbar)
    IToolbar approvalToolbar;
    @BindView(R.id.approval_reasons)
    TextView approvalReasons;

    private ApprovalInterface.Presenter presenter;

    private RecordData recordData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        setSupportActionBar(approvalToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initData();

        presenter = new ApprovalPresenter(this, ApprovalActivity.this);

    }

    private void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("RecordData");
        Gson gson = new Gson();
        recordData = gson.fromJson(data, RecordData.class);

        if (recordData.getContent() != null) {
            approvalReasons.setText("" + recordData.getContent());
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

package com.lsy.wisdom.clockin.activity.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.mvp.append.AddInterface;
import com.lsy.wisdom.clockin.mvp.append.AddPresent;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.TimeUtils;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/12
 * todo : 新建请假
 */
public class AddConversionActivity extends AppCompatActivity implements AddInterface.View {

    @BindView(R.id.add_leave_toolbar)
    IToolbar addLeaveToolbar;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.add_to)
    TextView addTo;
    @BindView(R.id.conversion_station)
    EditText conversionStation;
    @BindView(R.id.add_conversion_content)
    EditText addConversionContent;

    private long timeStart = 0;
    private long timeReturn = 0;

    private AddInterface.Presenter presenter;

    private TimePickerView pickerView;

    private SharedUtils sharedUtils;

    private String listIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conversio);
        setSupportActionBar(addLeaveToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();
//        initData();

        sharedUtils = new SharedUtils(AddConversionActivity.this, SharedUtils.CLOCK);

        listIds = sharedUtils.getData(SharedUtils.LISTID, "[]");

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }

        presenter = new AddPresent(this, AddConversionActivity.this);
    }

//    private void initData() {
//        timeStart = System.currentTimeMillis();
//
//        tvStartTime.setText("" + TimeUtils.timeslashData("" + timeStart));
//    }


    private void initBar() {
        addLeaveToolbar.inflateMenu(R.menu.toolbar_menu);
        addLeaveToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    @OnClick({R.id.line_start, R.id.line_end, R.id.add_leave_btn, R.id.check_line,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.line_start:
                if (GeneralMethod.isFastClick()) {

                    if (pickerView != null) {
                        pickerView.dismiss();
                    }

                    pickerView = new TimePickerBuilder(AddConversionActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            timeStart = date.getTime();
                            tvStartTime.setText("" + TimeUtils.getTimeYMD(date));
                        }
                    }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            L.log("pvTime", "onTimeSelectChanged");
                        }
                    })
                            .setType(new boolean[]{true, true, true, false, false, false})
//                            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                            .build();
                    pickerView.show();
                }
                break;
            case R.id.line_end:
                if (GeneralMethod.isFastClick()) {

                    if (pickerView != null) {
                        pickerView.dismiss();
                    }

                    pickerView = new TimePickerBuilder(AddConversionActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            timeReturn = date.getTime();
                            tvEndTime.setText("" + TimeUtils.getTimeYMD(date));
                        }
                    }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            L.log("pvTime", "onTimeSelectChanged");
                        }
                    })
                            .setType(new boolean[]{true, true, true, false, false, false})
//                            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                            .build();
                    pickerView.show();
                }
                break;
            case R.id.add_leave_btn:

                if (GeneralMethod.isFastClick()) {
                    if (is_input()) {
                        presenter.addCoutentZZ(sharedUtils.getData(SharedUtils.LISTID, "[]"), OKHttpClass.getUserId(this), OKHttpClass.getConglomerate(this), OKHttpClass.getToken(this), "转正", addConversionContent.getText().toString(), timeStart, timeReturn, conversionStation.getText().toString().trim());
                    }
                }

                break;
            case R.id.check_line:

                Intent intent = new Intent(AddConversionActivity.this, SubmitToActivity.class);
                startActivity(intent);

                break;
        }
    }


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (timeStart < 11) {
            Toast.makeText(AddConversionActivity.this, "未选择入职日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (timeReturn < 11) {
            Toast.makeText(AddConversionActivity.this, "未选择转正日期", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (conversionStation.getText().toString().trim().length() < 1) {
            Toast.makeText(AddConversionActivity.this, "岗位名称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (addConversionContent.getText().toString().trim().length() < 1) {
            Toast.makeText(AddConversionActivity.this, "请填写请假事由", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public void setFailure(String message) {
        ToastUtils.showBottomToast(this, "" + message);
        L.log("addContent", "setFailure" + message);
    }

    @Override
    public void setSuccess() {
        L.log("addContent", "setSuccess");
        ToastUtils.showBottomToast(this, "提交成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.distory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }
    }

}

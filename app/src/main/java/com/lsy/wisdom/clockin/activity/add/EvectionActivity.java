package com.lsy.wisdom.clockin.activity.add;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
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
 * Created by lsy on 2020/5/7
 * todo : 出差申请
 */
public class EvectionActivity extends AppCompatActivity implements AddInterface.View {


    @BindView(R.id.evection_toolbar)
    IToolbar evectionToolbar;
    @BindView(R.id.evection_area_tv)
    TextView evectionAreaTv;//地区
    @BindView(R.id.evection_detailed_address)
    EditText evectionDetailedAddress;//详细地址
    @BindView(R.id.tv_departure_date)
    TextView tvDepartureDate;
    @BindView(R.id.tv_return_date)
    TextView tvReturnDate;
    @BindView(R.id.evection_area)
    LinearLayout evectionArea;
    @BindView(R.id.evection_reasons)
    EditText evectionReasons;
    @BindView(R.id.evection_num)
    TextView evectionNum;
    @BindView(R.id.add_to)
    TextView addTo;

    private long timeStart = 0;
    private long timeReturn = 0;

    private AddInterface.Presenter presenter;

    private SharedUtils sharedUtils;

    private String listIds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evection);
        setSupportActionBar(evectionToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initData();

        sharedUtils = new SharedUtils(EvectionActivity.this, SharedUtils.CLOCK);

        listIds = sharedUtils.getData(SharedUtils.LISTID, "[]");

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }


        presenter = new AddPresent(this, EvectionActivity.this);
    }

    private void initData() {
        timeStart = System.currentTimeMillis();

        tvDepartureDate.setText("" + TimeUtils.timeslashData("" + timeStart));

        evectionReasons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                evectionNum.setText(i + "/200");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                evectionNum.setText(editable.length() + "/200");
            }
        });
    }


    private void initBar() {
        evectionToolbar.inflateMenu(R.menu.toolbar_menu);
        evectionToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    public void cityPicker() {
        JDCityPicker cityPicker = new JDCityPicker();
        JDCityConfig jdCityConfig = new JDCityConfig.Builder().build();

        jdCityConfig.setShowType(JDCityConfig.ShowType.PRO_CITY_DIS);
        cityPicker.init(this);
        cityPicker.setConfig(jdCityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                evectionAreaTv.setText(province.getName() + "\t\t" + city.getName() + "\t\t" + district.getName());
                evectionAreaTv.setTextColor(Color.parseColor("#333333"));
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
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


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (timeStart < 11) {
            Toast.makeText(EvectionActivity.this, "未选择出发日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (timeReturn < 11) {
            Toast.makeText(EvectionActivity.this, "未选择返回日期", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (evectionAreaTv.getText().toString().trim().length() < 1) {
            Toast.makeText(EvectionActivity.this, "请选择出差地区", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (evectionDetailedAddress.getText().toString().trim().length() < 1) {
            Toast.makeText(EvectionActivity.this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (evectionReasons.getText().toString().trim().length() < 1) {
            Toast.makeText(EvectionActivity.this, "请填写出差事由", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.distory();
    }


    @OnClick({R.id.evection_area, R.id.evection_departure_date, R.id.evection_return_date, R.id.evection_submit, R.id.check_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.evection_area:
                cityPicker();
                break;
            case R.id.evection_departure_date:

                if (GeneralMethod.isFastClick()) {
                    TimePickerView pickerView = new TimePickerBuilder(EvectionActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            timeStart = date.getTime();
                            tvDepartureDate.setText("" + TimeUtils.getTime(date));
                        }
                    }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            L.log("pvTime", "onTimeSelectChanged");
                        }
                    })
                            .setType(new boolean[]{true, true, true, true, true, true})
//                            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                            .build();
                    pickerView.show();
                }

                break;
            case R.id.evection_return_date:
                if (GeneralMethod.isFastClick()) {
                    TimePickerView pickerView = new TimePickerBuilder(EvectionActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            timeReturn = date.getTime();
                            tvReturnDate.setText("" + TimeUtils.getTime(date));
                        }
                    }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            L.log("pvTime", "onTimeSelectChanged");
                        }
                    })
                            .setType(new boolean[]{true, true, true, true, true, true})
//                            .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                            .build();
                    pickerView.show();
                }
                break;
            case R.id.evection_submit:
                if (GeneralMethod.isFastClick()) {
                    if (is_input()) {
                        presenter.addCoutentCC(sharedUtils.getData(SharedUtils.LISTID, "[]"), OKHttpClass.getUserId(this), OKHttpClass.getConglomerate(this), OKHttpClass.getToken(this), "出差"
                                , evectionReasons.getText().toString(), evectionAreaTv.getText().toString() + "\t\t" + evectionDetailedAddress.getText().toString()
                                , timeStart, timeReturn);

                    }
                }
                break;

            case R.id.check_line:
                Intent intent = new Intent(EvectionActivity.this, SubmitToActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
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

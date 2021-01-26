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
import com.lsy.wisdom.clockin.activity.task.PlayersActivity;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.TimeUtils;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/7/6
 * describe :  客户签约
 */
public class AddSignedActivity extends AppCompatActivity {

    @BindView(R.id.signed_toolbar)
    IToolbar signedToolbar;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.signed_amount)
    EditText signedAmount;
    @BindView(R.id.add_to)
    TextView addTo;
    @BindView(R.id.signed_util_name)
    EditText signedUtilName;
    @BindView(R.id.join_people)
    TextView joinPeople;

    private int items_id = 0;
    private String items_name = null;

    private TimePickerView pickerView;

    private long timeStart = 0;
    private long timeEnd = 0;

    private SharedUtils sharedUtils;

    private String peopleId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_signed);
        setSupportActionBar(signedToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        sharedUtils = new SharedUtils(AddSignedActivity.this, SharedUtils.CLOCK);

        Intent intent = getIntent();
        items_id = intent.getIntExtra("items_id", 0);
        items_name = intent.getStringExtra("items_name");

        String principal = sharedUtils.getData(SharedUtils.PRINCIPAL, null);
        if (principal != null) {
            addTo.setText("" + principal);
            addTo.setTextColor(0xff333333);
        }

        peopleId = sharedUtils.getData(SharedUtils.JOINID, "[]");

        String peoName = sharedUtils.getData(SharedUtils.JOINNAME, null);
        L.log("task", "+peoName");
        if (peoName != null && peoName.length() > 2) {
            joinPeople.setText("" + peoName.substring(1, peoName.length() - 1));
            joinPeople.setTextColor(0xff333333);
        }
    }


    private void initBar() {
        signedToolbar.inflateMenu(R.menu.toolbar_menu);
        signedToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    @OnClick({R.id.line_start, R.id.line_end, R.id.check_line, R.id.btn_cancel, R.id.btn_commit, R.id.join_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.line_start://开始时间
                if (GeneralMethod.isFastClick()) {

                    if (pickerView != null) {
                        pickerView.dismiss();
                    }

                    pickerView = new TimePickerBuilder(AddSignedActivity.this, new OnTimeSelectListener() {
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
            case R.id.line_end://结束时间

                if (GeneralMethod.isFastClick()) {

                    if (pickerView != null) {
                        pickerView.dismiss();
                    }

                    pickerView = new TimePickerBuilder(AddSignedActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            timeEnd = date.getTime();
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
            case R.id.check_line:
                Intent intent = new Intent(AddSignedActivity.this, CheckPrincipalActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cancel://取消
                finish();
                break;
            case R.id.btn_commit://提交

                if (GeneralMethod.isFastClick()) {
                    if (is_input()) {
                        addProject();
                    }
                }
                break;


            case R.id.join_line://选择参与人
                Intent people = new Intent(AddSignedActivity.this, PlayersActivity.class);
                startActivity(people);
                break;
        }
    }


    //增加项目
    public void addProject() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:conglomerate_id(集团id),items_id(客户id),staff_id(负责人id),start_timeC(项目开工时间戳),end_timeC(项目完工时间戳),amount(项目款)

        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("items_id", items_id);
        listcanshu.put("staff_id", sharedUtils.getIntData(SharedUtils.PRINCIPALID));
        listcanshu.put("start_timeC", timeStart);
        listcanshu.put("end_timeC", timeEnd);
        listcanshu.put("amount", Double.parseDouble("" + signedAmount.getText().toString()));
        listcanshu.put("project_name", "" + signedUtilName.getText().toString());
        listcanshu.put("participant", "" + peopleId);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddSignedActivity.this, RequestURL.addProject, listcanshu);
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
                        ToastUtils.showBottomToast(AddSignedActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(AddSignedActivity.this, "" + message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });
    }


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (signedUtilName.getText().toString().trim().length() < 1) {
            Toast.makeText(AddSignedActivity.this, "请输入项目名称", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (timeStart <= 0) {
            Toast.makeText(AddSignedActivity.this, "未选择开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (timeEnd <= 0) {
            Toast.makeText(AddSignedActivity.this, "未选择结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (signedAmount.getText().toString().trim().length() < 1) {
            Toast.makeText(AddSignedActivity.this, "请输入签约金额", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (addTo.getText().toString().trim().length() < 1) {
            Toast.makeText(AddSignedActivity.this, "请选择项目负责人", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        String principal = sharedUtils.getData(SharedUtils.PRINCIPAL, null);
        if (principal != null) {
            addTo.setText("" + principal);
            addTo.setTextColor(0xff333333);
        }

        peopleId = sharedUtils.getData(SharedUtils.JOINID, "[]");

        String peoName = sharedUtils.getData(SharedUtils.JOINNAME, null);
        if (peoName != null && peoName.length() > 2) {
            joinPeople.setText("" + peoName.substring(1, peoName.length() - 1));
            joinPeople.setTextColor(0xff333333);
        }
    }

}

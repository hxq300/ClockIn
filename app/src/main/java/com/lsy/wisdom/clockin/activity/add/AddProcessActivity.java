package com.lsy.wisdom.clockin.activity.add;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.desc.ShareActivity;
import com.lsy.wisdom.clockin.mvp.append.AddInterface;
import com.lsy.wisdom.clockin.mvp.append.AddPresent;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.TimeUtils;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/6/19
 * todo : 新建跟进
 */
public class AddProcessActivity extends AppCompatActivity implements AddInterface.View {

    @BindView(R.id.add_process_toolbar)
    IToolbar addPurchaseToolbar;
    @BindView(R.id.pro_time)
    TextView proTime;
    @BindView(R.id.pro_custromer_name)
    TextView proCustromerName;
    @BindView(R.id.pro_principal)
    EditText proPrincipal;
    @BindView(R.id.pro_content)
    EditText proContent;
    @BindView(R.id.pro_state)
    TextView proState;

    private TimePickerView pickerView;
    private long timeStart = 0;

    private int items_id = 0;
    private String items_name = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_process);
        setSupportActionBar(addPurchaseToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        Intent intent = getIntent();
        items_id = intent.getIntExtra("items_id", 0);
        items_name = intent.getStringExtra("items_name");
        if (items_name != null) {
            proCustromerName.setText("" + items_name);
        }

        L.log("intent", items_name + "id==" + items_id);

    }


    private void initBar() {
        addPurchaseToolbar.inflateMenu(R.menu.toolbar_menu);
        addPurchaseToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    /**
     * 判断输入完成情况
     */
    private boolean is_input() {

        if (proTime.getText().toString().trim().length() < 1) {
            Toast.makeText(AddProcessActivity.this, "请选择跟进时间", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (proCustromerName.getText().toString().trim().length() < 1) {
            Toast.makeText(AddProcessActivity.this, "请选择客户", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (proContent.getText().toString().trim().length() < 1) {
            Toast.makeText(AddProcessActivity.this, "跟进内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (proState.getText().toString().trim().length() < 1) {
            Toast.makeText(AddProcessActivity.this, "请选择跟进状态", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private List<String> gradeData; //自行添加一些string

    private OptionsPickerView pvOptions;


    private void initSelector() {

        gradeData = new ArrayList<>();
        gradeData.add("已建档");
        gradeData.add("已沟通");
        gradeData.add("已签约");
        gradeData.add("已执行");
        gradeData.add("已完成");
        gradeData.add("已失效");

        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            proState.setText(gradeData.get(options1));
            // gradeData.get(options1)
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("跟进状态")
                .setSubCalSize(16)//确定和取消文字大小
                .setSubmitColor(0xff3B73FF)//确定按钮文字颜色
                .setCancelColor(0xff999999)//取消按钮文字颜色
                .setTitleBgColor(0xffF4F4F4)//标题背景颜色 Night mode
                .setTitleSize(15)
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xff333333)
                .setTextColorOut(0x999999)
                .setDividerColor(0xffEEEEEE)
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLineSpacingMultiplier((float) 2.5) //设置item的高度
                .build();

        pvOptions.setPicker(gradeData);
        pvOptions.show();
    }

    @Override
    public void setFailure(String message) {
        ToastUtils.showBottomToast(this, "" + message);
    }

    @Override
    public void setSuccess() {
        ToastUtils.showBottomToast(this, "添加成功");
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.line_ptime, R.id.line_customer_name, R.id.line_pstate, R.id.process_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.line_ptime:

                pickerView = new TimePickerBuilder(AddProcessActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        timeStart = date.getTime();
                        proTime.setText("" + TimeUtils.getTimeYMD(date));
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

                break;
            case R.id.line_customer_name:
                break;
            case R.id.line_pstate:
                initSelector();
                break;
            case R.id.process_btn:
                if (is_input()) {
                    addProcess();
                }
                break;
        }
    }


    public void addProcess() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

//        传参:conglomerate_id(集团id), itmes_id(客户id), staff_id(员工id), principal(负责人), content(跟进内容), schedule_type(跟进状态)

        listcanshu.put("staff_id", OKHttpClass.getUserId(this));
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("principal", "" + proPrincipal.getText().toString());
        listcanshu.put("content", "" + proContent.getText().toString());
        listcanshu.put("schedule_type", "" + proState.getText().toString());
        listcanshu.put("items_id", items_id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(AddProcessActivity.this, RequestURL.addProcess, listcanshu);
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
                        ToastUtils.showBottomToast(AddProcessActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(AddProcessActivity.this, "" + message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return dataString;
            }
        });
    }

}

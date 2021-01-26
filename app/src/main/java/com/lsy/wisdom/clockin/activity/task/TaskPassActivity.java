package com.lsy.wisdom.clockin.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/7/31
 * describe :  TODO
 */
public class TaskPassActivity extends AppCompatActivity {


    @BindView(R.id.pass_toolbar)
    IToolbar passToolbar;
    @BindView(R.id.pass_reason)
    EditText passReason;

    private int taskId = 0;
    private int type = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pass);
        setSupportActionBar(passToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        Intent intent = getIntent();
        taskId = intent.getIntExtra("task_id", 0);
        type = intent.getIntExtra("type", 0);

        initBar();


    }

    @OnClick(R.id.pass_submit)
    public void onViewClicked() {
        if (GeneralMethod.isFastClick()) {
            if (type == 0) {
                pass();
            } else {
                reject();
            }
        }

    }


    private void pass() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:id(任务id),check(通过原因)
        listcanshu.put("id", "" + taskId);
        listcanshu.put("check", "" + passReason.getText().toString());

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(TaskPassActivity.this, RequestURL.taskPass, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
//                L.log("getPermission", OKHttpClass.getUserId(MyTaskActivity.this) + "----" + dataString);

                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(TaskPassActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(TaskPassActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }

    private void reject() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:id(任务id),check(未通过原因)
        listcanshu.put("id", "" + taskId);
        listcanshu.put("check", "" + passReason.getText().toString());

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(TaskPassActivity.this, RequestURL.taskReject, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
//                L.log("getPermission", OKHttpClass.getUserId(MyTaskActivity.this) + "----" + dataString);

                //{"message":"打卡成功","data":null,"code":200}
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(TaskPassActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(TaskPassActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }

    private void initBar() {
        Menu menu = passToolbar.getMenu();
        menu.clear();

        if (type == 0) {
            passToolbar.setTitle("任务通过");
        } else {
            passToolbar.setTitle("任务驳回");
        }

        passToolbar.inflateMenu(R.menu.toolbar_menu);

        passToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1:
                        break;

                    default:
                        L.log("munu", "新建跟进" + pos);
                        break;
                }
            }
        });
    }

}

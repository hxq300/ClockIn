package com.lsy.wisdom.clockin.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
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
 * Created by lsy on 2020/7/28
 * todo : 我的任务
 */
public class MyTaskActivity extends AppCompatActivity {


    @BindView(R.id.my_task_toolbar)
    IToolbar myTaskToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        setSupportActionBar(myTaskToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        isPermission();

    }

    private void initBar() {

        Menu menu = myTaskToolbar.getMenu();
        menu.clear();

        myTaskToolbar.inflateMenu(R.menu.add_menu);
        myTaskToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        break;
                    case 1://创建任务
                        if (isP) {
                            Intent addTask = new Intent(MyTaskActivity.this, AddTaskActivity.class);
                            startActivity(addTask);
                        } else {
                            ToastUtils.showBottomToast(MyTaskActivity.this, "您暂无相关权限");
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }


    @OnClick({R.id.task_my, R.id.task_join, R.id.task_creat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.task_my://我的任务

                Intent task = new Intent();
                task.putExtra("type", 0);
                task.putExtra("title", "我的任务");
                task.setClass(this, TaskActivity.class);
                startActivity(task);

                break;
            case R.id.task_join://我参加的任务
                Intent task1 = new Intent();
                task1.putExtra("type", 1);
                task1.putExtra("title", "我参与的");
                task1.setClass(this, TaskActivity.class);
                startActivity(task1);
                break;
            case R.id.task_creat://我创建的任务
                if (isP) {
                    Intent task2 = new Intent();
                    task2.putExtra("type", 2);
                    task2.putExtra("title", "我派发的");
                    task2.setClass(this, TaskActivity.class);
                    startActivity(task2);
                } else {
                    ToastUtils.showBottomToast(MyTaskActivity.this, "您暂无相关权限");
                }

                break;

            default:
                break;
        }
    }

    private boolean isP = false;

    private void isPermission() {

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("staff_id", "" + OKHttpClass.getUserId(this));

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(MyTaskActivity.this, RequestURL.getPermission, listcanshu);
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
                        isP = true;
                    } else {
                        isP = false;
//                        ToastUtils.showBottomToast(context, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }


}

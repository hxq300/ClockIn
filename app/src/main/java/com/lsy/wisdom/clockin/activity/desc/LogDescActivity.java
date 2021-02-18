package com.lsy.wisdom.clockin.activity.desc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.LogData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/28
 * todo : 日志详情
 */
public class LogDescActivity extends AppCompatActivity {

    @BindView(R.id.ld_time)
    TextView ldTime;
    @BindView(R.id.ld_work_content)
    TextView ldWorkContent;
    @BindView(R.id.ld_plan)
    TextView ldPlan;
    @BindView(R.id.ld_toolbar)
    IToolbar ldToolbar;
    @BindView(R.id.log_recycler)
    RecyclerView logRecycler;
    @BindView(R.id.btn_del)
    Button btnDel;

    private LogData logData;

    private CommonAdapter listAdapter;

    private List<String> images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_desc);
        setSupportActionBar(ldToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        initData();

    }

    private void initData() {

        //===添加图片部分
        logRecycler.setItemViewCacheSize(100);
        logRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        logRecycler.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        String data = intent.getStringExtra("logData");
        Gson gson = new Gson();
        logData = gson.fromJson(data, LogData.class);

        if (logData.getUptime() != null) {
            ldTime.setText("" + logData.getUptime());
        }
        if (logData.getContent() != null) {
            ldWorkContent.setText("" + logData.getContent());
        }
        if (logData.getTomorrow_plan() != null) {
            ldPlan.setText("" + logData.getTomorrow_plan());
        }


        images = new ArrayList<>();

        listAdapter = new CommonAdapter<String>(getApplicationContext(), R.layout.view_reimburse_image, images) {
            @Override
            protected void convert(ViewHolder holder, String iamge, int position) {

                ImageView image = holder.getView(R.id.reimburse_photo);
                Glide.with(LogDescActivity.this).load(RequestURL.OssUrl + iamge).into(image);

            }
        };

        logRecycler.setAdapter(listAdapter);

        if (logData.getUrl() != null) {

            String jsons = logData.getUrl().substring(1, logData.getUrl().length() - 1);
            String str[] = jsons.split("[,]");
            for (int i = 0; i < str.length; i++) {
                L.log(str[i]);
                images.add("" + str[i].trim());
            }

            listAdapter.notifyDataSetChanged();
        }

    }


    private void initBar() {
        ldToolbar.inflateMenu(R.menu.toolbar_menu);
        ldToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        break;

                    default:
                        break;
                }
            }
        });
    }

// todo : 添加删除功能待测试
    @OnClick(R.id.btn_del)
    public void onViewClicked() {
        if (GeneralMethod.isFastClick()) {
//            delete("" + logData.getId());
        }

    }

    public void delete(String id) {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        listcanshu.put("id", id);

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(LogDescActivity.this, RequestURL.removeProjectLog, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                L.log("deleted", "" + dataString);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(LogDescActivity.this, "删除成功");
                        finish();
                    } else {
                        ToastUtils.showBottomToast(LogDescActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }
}

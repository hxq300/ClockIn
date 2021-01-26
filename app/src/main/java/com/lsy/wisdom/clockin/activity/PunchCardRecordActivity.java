package com.lsy.wisdom.clockin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsy.wisdom.calendardemo.bean.DateEntity;
import com.lsy.wisdom.calendardemo.view.DataView;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.PunchRecord;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.GroupButtonView;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/20
 * todo : 打卡历史记录
 */
public class PunchCardRecordActivity extends AppCompatActivity {


    @BindView(R.id.punch_card_toolbar)
    IToolbar punchCardToolbar;
    @BindView(R.id.week)
    DataView dataView;
    @BindView(R.id.recycler_record)
    RecyclerView recyclerRecord;
    @BindView(R.id.gbv_pcard)
    GroupButtonView gbvPcard;

    private List<PunchRecord> punchRecords;

    private CommonAdapter listAdapter;

    private int open = 0;
    private String time = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_card_record);
        setSupportActionBar(punchCardToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        punchRecords = new ArrayList<>();

        initBar();

        initView();

    }

    private void initView() {

        //===========
        recyclerRecord.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PunchCardRecordActivity.this);
        recyclerRecord.setLayoutManager(linearLayoutManager);
        recyclerRecord.setNestedScrollingEnabled(false);

//        info = (TextView) findViewById(R.id.info);
        dataView = (DataView) findViewById(R.id.week);
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {

                time = "" + date.million;
                if (open == 0) {
                    getData("" + date.million);
                } else {
                    getAllData("" + date.million);
                }


            }
        });
        dataView.getData("");

        listAdapter = new CommonAdapter<PunchRecord>(this, R.layout.item_daka_record, punchRecords) {
            @Override
            protected void convert(ViewHolder holder, PunchRecord data, int position) {

                ImageView cardView = holder.getView(R.id.image_card);
                TextView remark = holder.getView(R.id.record_remark);
                holder.setText(R.id.record_time, "" + data.getTime());
                holder.setText(R.id.record_location, "" + data.getAddress());
                holder.setText(R.id.card_name, "" + data.getStaff_name());

                if (data.getRemark() != null && data.getRemark().length() > 0) {
                    remark.setVisibility(View.VISIBLE);
                    remark.setText("" + data.getRemark());
                } else {
                    remark.setVisibility(View.GONE);
                }


                if (data.getUrl() != null) {

                    String jsons = data.getUrl().substring(1, data.getUrl().length() - 1);
                    String str[] = jsons.split("[,]");
//                    for (int i = 0; i < str.length; i++) {
//                        L.log(str[i]);
//                        images.add("" + str[i].trim());
//                    }

                    if (str.length > 0) {
                        String image = "" + str[0].trim();
                        cardView.setVisibility(View.VISIBLE);
                        Glide.with(PunchCardRecordActivity.this).load(RequestURL.OssUrl + image).into(cardView);
                    } else {
                        cardView.setVisibility(View.GONE);
                    }


                }

//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ToastUtils.showBottomToast(PunchCardRecordActivity.this, "点击了item" + position);
//                    }
//                });
            }

        };

        recyclerRecord.setAdapter(listAdapter);


        gbvPcard.setOnGroupBtnClickListener(new GroupButtonView.OnGroupBtnClickListener() {
            @Override
            public void groupBtnClick(String code) {
                open = Integer.parseInt(code);
                switch (open) {
                    case 0:
                        open = 0;
                        punchRecords.clear();
                        getData(time);
                        break;
                    case 1:
                        open = 1;
                        punchRecords.clear();
                        getAllData(time);
                        break;
                    default:
                        break;
                }

//                Toast.makeText(GoodsDetailsActivity.this, code, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getData(String time) {

        punchRecords.clear();

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:staff_id(员工id,登录返回),timeC(要查询日期时间戳)
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(PunchCardRecordActivity.this));
        listcanshu.put("staff_id", OKHttpClass.getUserId(PunchCardRecordActivity.this));
        listcanshu.put("timeC", time);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(PunchCardRecordActivity.this, RequestURL.getRecordHistory, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                punchRecords.clear();
                //请求成功数据回调
                L.log("punchCard", "" + dataString);
                Gson gson = new Gson();

//                //{"message":"打卡成功","data":null,"code":200}
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(dataString);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        PunchRecord record = gson.fromJson("" + jsonArray.get(i).toString(), PunchRecord.class);
                        punchRecords.add(record);
                    }

                    listAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return dataString;
            }
        });


    }

    private void getAllData(String time) {

        punchRecords.clear();

        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        //传参:staff_id(员工id,登录返回),timeC(要查询日期时间戳)
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(PunchCardRecordActivity.this));
        listcanshu.put("staff_id", OKHttpClass.getUserId(PunchCardRecordActivity.this));
        listcanshu.put("timeC", time);
        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(PunchCardRecordActivity.this, RequestURL.getAllCard, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                //请求成功数据回调
                punchRecords.clear();
                L.log("punchCard", "" + dataString);
                Gson gson = new Gson();

//                //{"message":"未获得权限!","data":null,"code":200}

                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                try {
                    jsonObject = new JSONObject(dataString);

                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200 && data != null) {
                        jsonArray = new JSONArray(data);
//
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PunchRecord record = gson.fromJson("" + jsonArray.get(i).toString(), PunchRecord.class);
                            punchRecords.add(record);
                        }
                    } else {
                        ToastUtils.showBottomToast(PunchCardRecordActivity.this, "" + message);
                    }

                    listAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                return dataString;
            }
        });


    }

    private void initBar() {
        Menu menu = punchCardToolbar.getMenu();
        menu.clear();

        punchCardToolbar.inflateMenu(R.menu.toolbar_menu);
        punchCardToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://

                        break;
                    default:
                        break;
                }
            }
        });
    }

}
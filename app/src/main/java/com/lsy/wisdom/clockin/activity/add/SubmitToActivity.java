package com.lsy.wisdom.clockin.activity.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.ClockInActivity;
import com.lsy.wisdom.clockin.activity.desc.PersonalDataActivity;
import com.lsy.wisdom.clockin.activity.desc.ShareActivity;
import com.lsy.wisdom.clockin.bean.Staff;
import com.lsy.wisdom.clockin.bean.UserData;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
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
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/12
 * todo : 选择提交人
 */
public class SubmitToActivity extends AppCompatActivity {


    @BindView(R.id.sto_toolbar)
    IToolbar stoToolbar;
    @BindView(R.id.sto_recycler)
    RecyclerView stoRecycler;

    private CommonAdapter commonAdapter;

    private List<Staff> staffList;

    private List<Integer> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    private SharedUtils sharedUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_to);
        setSupportActionBar(stoToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        sharedUtils = new SharedUtils(SubmitToActivity.this, SharedUtils.CLOCK);

        String listIds = sharedUtils.getData(SharedUtils.LISTID, null);

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (listIds != null) {
            try {
                JSONArray jsonArray = new JSONArray(listIds);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ids.add(Integer.parseInt(jsonArray.get(i).toString()));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (nameIds != null) {
            try {
                JSONArray jsonArray = new JSONArray(nameIds);
                for (int i = 0; i < jsonArray.length(); i++) {
                    names.add(jsonArray.get(i).toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        initRecycle();

        getData();
    }


    private void initBar() {
        stoToolbar.inflateMenu(R.menu.toolbar_menu);
        stoToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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


    private void initRecycle() {

        staffList = new ArrayList<>();
        //===========
        stoRecycler.setItemViewCacheSize(100);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubmitToActivity.this);
        stoRecycler.setLayoutManager(linearLayoutManager);
        stoRecycler.setNestedScrollingEnabled(false);
        stoRecycler.setFocusableInTouchMode(true);

        commonAdapter = new CommonAdapter<Staff>(this, R.layout.item_commit_staff, staffList) {
            @Override
            protected void convert(ViewHolder holder, Staff data, int position) {

                TextView uTex = holder.itemView.findViewById(R.id.st_name);
                ImageView isCheck = holder.itemView.findViewById(R.id.is_check);
                ImageView staffImage = holder.itemView.findViewById(R.id.staff_image);

                Glide.with(SubmitToActivity.this).load(RequestURL.OssUrl + data.getPicture())
                        .error(R.mipmap.renyuan_image)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(staffImage);

                if (ids != null) {
                    if (ids.contains(data.getId())) {
                        staffList.get(position).setCheck(true);
                        isCheck.setVisibility(View.VISIBLE);
                    } else {
                        staffList.get(position).setCheck(false);
                        isCheck.setVisibility(View.GONE);
                    }
                }


                uTex.setText("" + data.getStaff_name());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (GeneralMethod.isFastClick()) {

                            L.log("submit", "点击了tiemView");

                            if (data.isCheck()) {
                                for (int i = 0; i < ids.size(); i++) {
                                    if (ids.get(i).equals(data.getId())) {
                                        ids.remove(i);
                                        names.remove(i);
                                        isCheck.setVisibility(View.GONE);
                                    }
                                }
                                data.setCheck(false);
                            } else {
                                ids.add(data.getId());
                                names.add(data.getStaff_name());
                                data.setCheck(true);
                                isCheck.setVisibility(View.VISIBLE);
                            }

                            L.log("submit", "names=" + ids.toString());
//                            uTex.setTextColor(0xff333333);
                        }

                    }
                });
            }

        };

        stoRecycler.setAdapter(commonAdapter);
    }


    private void getData() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();
        listcanshu.put("conglomerate_id", OKHttpClass.getConglomerate(this));
        listcanshu.put("staff_name", "");

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(this, RequestURL.getAllStaff, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {

//                recordDataList = new ArrayList<>();
//                //请求成功数据回调
                L.log("getAllStaff", "" + dataString);

                Gson gson = new Gson();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(dataString);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Staff data = gson.fromJson(jsonArray.get(i).toString(), Staff.class);
                        data.setCheck(false);
                        staffList.add(data);
                    }

                    commonAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });

    }


    @OnClick(R.id.sto_commit)
    public void onViewClicked() {
        sharedUtils.setData(SharedUtils.LISTID, ids.toString());
        sharedUtils.setData(SharedUtils.NAMEID, names.toString());
        finish();
    }
}

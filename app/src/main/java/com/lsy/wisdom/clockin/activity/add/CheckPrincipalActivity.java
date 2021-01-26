package com.lsy.wisdom.clockin.activity.add;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.Staff;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/7/6
 * describe :  选择负责人
 */
public class CheckPrincipalActivity extends AppCompatActivity {


    @BindView(R.id.sto_toolbar)
    IToolbar stoToolbar;
    @BindView(R.id.sto_recycler)
    RecyclerView stoRecycler;

    private CommonAdapter commonAdapter;

    private List<Staff> staffList;

    private String principal = "";
    private int principalID = 0;


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

        sharedUtils = new SharedUtils(CheckPrincipalActivity.this, SharedUtils.CLOCK);

        principal = sharedUtils.getData(SharedUtils.PRINCIPAL, null);

        initRecycle();

        getData();
    }


    private void initBar() {
        stoToolbar.inflateMenu(R.menu.toolbar_menu);
        stoToolbar.setTitle("选择负责人");
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CheckPrincipalActivity.this);
        stoRecycler.setLayoutManager(linearLayoutManager);
        stoRecycler.setNestedScrollingEnabled(false);
        stoRecycler.setFocusableInTouchMode(true);

        commonAdapter = new CommonAdapter<Staff>(this, R.layout.item_commit_staff, staffList) {
            @Override
            protected void convert(ViewHolder holder, Staff data, int position) {

                TextView uTex = holder.itemView.findViewById(R.id.st_name);
                ImageView isCheck = holder.itemView.findViewById(R.id.is_check);
                ImageView staffImage = holder.itemView.findViewById(R.id.staff_image);

                Glide.with(CheckPrincipalActivity.this).load(RequestURL.OssUrl + data.getPicture())
                        .error(R.mipmap.renyuan_image)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(staffImage);

                if (principal != null) {
                    if (principal.contains(data.getStaff_name())) {
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

                            if (!data.isCheck()) {
                                data.setCheck(true);
                                isCheck.setVisibility(View.VISIBLE);
                                principal = "" + data.getStaff_name();
                                principalID = data.getId();
                                notifyDataSetChanged();
                            }


//                            if (data.isCheck()) {
//                                for (int i = 0; i < ids.size(); i++) {
//                                    if (ids.get(i).equals(data.getId())) {
//                                        ids.remove(i);
//                                        names.remove(i);
//                                        isCheck.setVisibility(View.GONE);
//                                    }
//                                }
//                                data.setCheck(false);
//                            } else {
//                                ids.add(data.getId());
//                                names.add(data.getStaff_name());
//                                data.setCheck(true);
//                                isCheck.setVisibility(View.VISIBLE);
//                            }

//                            L.log("submit", "names=" + ids.toString());
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
        sharedUtils.setData(SharedUtils.PRINCIPAL, "" + principal);
        sharedUtils.setData(SharedUtils.PRINCIPALID, principalID);
        finish();
    }
}

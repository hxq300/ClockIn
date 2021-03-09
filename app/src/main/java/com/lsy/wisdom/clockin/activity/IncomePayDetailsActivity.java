package com.lsy.wisdom.clockin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.adapter.ImgAdapter;
import com.lsy.wisdom.clockin.bean.PayIncomeEntity;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.request.RequestURL;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

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
 * Created by hxq on 2021/3/4
 * describe :  TODO 收支详情页面
 */
public class IncomePayDetailsActivity extends AppCompatActivity {
    @BindView(R.id.ld_toolbar)
    IToolbar ldToolbar;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.supplier_name)
    TextView supplierName;
    @BindView(R.id.items_name)
    TextView itemsName;
    @BindView(R.id.project_name)
    TextView projectName;
    @BindView(R.id.cost_types)
    TextView costTypes;
    @BindView(R.id.payment_amount)
    TextView paymentAmount;
    @BindView(R.id.explain)
    TextView explain;
    @BindView(R.id.payment_time)
    TextView paymentTime;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.companyBtn)
    TextView companyBtn;
    @BindView(R.id.deleteBtn)
    TextView deleteBtn;
    @BindView(R.id.imgRv)
    RecyclerView mImgRv;
    @BindView(R.id.status_line)
    LinearLayout statusLine;

    private PayIncomeEntity.ItemsBean mData;
    private int mType;

    private ImgAdapter mImgAdapter;
    private List<String> mImgPathList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_pay_detail);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mData = (PayIncomeEntity.ItemsBean) bundle.get("data");
        mType = intent.getIntExtra("type", 10);
        initView();
        initAdapter();
        initData();
    }

    private void initAdapter() {
        mImgPathList = new ArrayList<>();
        mImgAdapter = new ImgAdapter(mImgPathList);
        mImgRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImgRv.setAdapter(mImgAdapter);
    }

    private void initView() {
        ldToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                finish();
            }
        });


    }

    private void initData() {
        if (mData != null) {
            companyName.setText(mData.getCompany_name());
            supplierName.setText(mData.getSupplier_name());
            itemsName.setText(mData.getItems_name());
            projectName.setText(mData.getProject_name());
            costTypes.setText(mData.getCost_types());
            paymentAmount.setText(mData.getPayment_amount());
            explain.setText(mData.getExplain());
            paymentTime.setText(mData.getPayment_time());
            state.setText(mData.getState());
            if (mType == 1) { // 费用收支
                statusLine.setVisibility(View.VISIBLE);
                tvChange.setText("有无发票");
                ldToolbar.setTitleText("费用收支明细");
                if (null != mData.getStatus())
                    status.setText(mData.getStatus());
            } else if (mType == 0) { // 付款申请
                statusLine.setVisibility(View.GONE);
                tvChange.setText("是否通过");
                ldToolbar.setTitleText("付款申请明细");
                state.setText(mData.getCheck());

            }
            String picture = mData.getPicture();
            if (!"".equals(picture)) {
                String substring = picture.substring(1, picture.length() - 1);
                String[] split = substring.split(",");
                if (split.length != 0) {
                    for (String s : split) {
                        mImgPathList.add(s);
                    }
                    mImgAdapter.notifyDataSetChanged();
                    Log.d("imgList", "initData: " + mImgPathList.size());
                    Log.d("imgList", "initData: " + mImgPathList.get(0));
                } else {
                    mImgRv.setVisibility(View.GONE);
                }
            } else {
                mImgRv.setVisibility(View.GONE);
            }

        }
    }

    @OnClick({R.id.companyBtn, R.id.deleteBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.companyBtn: // 编辑
                break;
            case R.id.deleteBtn: // 删除
                if (mType == 1) {
                    //删除费用收支明细
                    deleteApply();
                } else if (mType == 0) {
                    //删除付款申请明细
                    deleteRecord();
                }
                break;
        }
    }

    /**
     * 删除 费用收支明细
     */
    private void deleteApply() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        listcanshu.put("id", mData.getId());

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(IncomePayDetailsActivity.this, RequestURL.deleteApply, listcanshu);
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
                        ToastUtils.showBottomToast(IncomePayDetailsActivity.this, "删除成功");
                        finish();
                    } else {
                        ToastUtils.showBottomToast(IncomePayDetailsActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    /**
     * 删除 付款申请明细
     */
    private void deleteRecord() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        listcanshu.put("id", mData.getId());

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(IncomePayDetailsActivity.this, RequestURL.deleteRecord, listcanshu);
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
                        ToastUtils.showBottomToast(IncomePayDetailsActivity.this, "删除成功");
                        finish();
                    } else {
                        ToastUtils.showBottomToast(IncomePayDetailsActivity.this, "" + message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }

    /**
     * todo 提交编辑
     */
    private void submitCompile() {
        Map<String, Object> listcanshu = new HashMap<>();
        OKHttpClass okHttpClass = new OKHttpClass();

        listcanshu.put("id", mData.getId());

        //设置请求类型、地址和参数
        okHttpClass.setPostCanShu(IncomePayDetailsActivity.this, RequestURL.updateMessage, listcanshu);
        okHttpClass.setGetIntenetData(new OKHttpClass.GetData() {
            @Override
            public String requestData(String dataString) {
                L.log("customerQuery", "" + dataString);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataString);


                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        ToastUtils.showBottomToast(IncomePayDetailsActivity.this, "" + message);
                        finish();
                    } else {
                        ToastUtils.showBottomToast(IncomePayDetailsActivity.this, "" + message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return dataString;
            }
        });
    }


}

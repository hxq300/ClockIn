package com.lsy.wisdom.clockin.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.PayIncomeEntity;

import java.util.List;

/**
 * Created by hxq on 2021/3/4
 * describe :  TODO 收支页面适配器
 */
public class PayIncomeAdapter extends BaseQuickAdapter<PayIncomeEntity.ItemsBean, BaseViewHolder> {

    public PayIncomeAdapter(@Nullable List<PayIncomeEntity.ItemsBean> data) {
        super(R.layout.view_item_detail, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, PayIncomeEntity.ItemsBean item) {
        helper.setText(R.id.tv_company_name,item.getCompany_name());
        helper.setText(R.id.tv_payment_time,item.getPayment_time());
    }
}

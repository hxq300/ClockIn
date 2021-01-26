package com.lsy.wisdom.clockin.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.LogData;

import java.util.List;

/**
 * Created by lsy on 2020/12/29
 * describe :  TODO
 */
public class LogDataAdapter extends BaseQuickAdapter<LogData, BaseViewHolder> {
    public LogDataAdapter(@Nullable List<LogData> data) {
        super(R.layout.item_log_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LogData item) {
        helper.setText(R.id.log_name, "" + item.getStaff_name());
        helper.setText(R.id.log_time, "" + item.getUptime());
        helper.setText(R.id.log_content, "" + item.getContent());
    }
}

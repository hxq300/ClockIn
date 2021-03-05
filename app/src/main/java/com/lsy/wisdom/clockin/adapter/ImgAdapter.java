package com.lsy.wisdom.clockin.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.request.RequestURL;

import java.util.List;

/**
 * Created by hxq on 2021/3/5
 * describe :  TODO
 */
public class ImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ImgAdapter(@Nullable List<String> data) {
        super(R.layout.view_item_img, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext)
                .load(RequestURL.OssUrl+item)
                .into((ImageView) helper.getView(R.id.iv));
    }
}

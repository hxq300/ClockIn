package com.lsy.wisdom.clockin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.donkingliang.refresh.RefreshLayout;

/**
 * Created by lsy on 2020/8/10
 * describe :  TODO
 */
public class MyFooterView extends LinearLayout implements RefreshLayout.OnFooterStateListener{

    public MyFooterView(Context context) {
        super(context);
    }

    public MyFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 尾部滑动变化
     *
     * @param footerView   尾部View
     * @param scrollOffset 滑动距离
     * @param scrollRatio  从开始到触发阀值的滑动比率（0到100）如果滑动到达了阀值，就算在滑动，这个值也是100
     */
    @Override
    public void onScrollChange(View footerView, int scrollOffset, int scrollRatio) {
    }

    /**
     * 尾部处于加载状态 （触发上拉加载的时候调用）
     *
     * @param footerView 尾部View
     */
    @Override
    public void onRefresh(View footerView) {
    }

    /**
     * 加载完成，尾部收起
     *
     * @param footerView 尾部View
     @param isSuccess  是否加载成功
     */
    @Override
    public void onRetract(View footerView, boolean isSuccess) {
    }

    /**
     * 是否还有更多(是否可以加载下一页)
     *
     * @param footerView
     * @param hasMore
     */
    @Override
    public void onHasMore(View footerView, boolean hasMore) {
    }
}
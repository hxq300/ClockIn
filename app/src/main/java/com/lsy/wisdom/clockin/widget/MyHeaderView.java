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
public class MyHeaderView extends LinearLayout implements RefreshLayout.OnHeaderStateListener{

    public MyHeaderView(Context context) {
        super(context);
    }

    public MyHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 头部滑动变化
     *
     * @param headerView   头部View
     * @param scrollOffset 滑动距离
     * @param scrollRatio  从开始到触发阀值的滑动比率（0到100）如果滑动到达了阀值，就算再滑动，这个值也是100
     */
    @Override
    public void onScrollChange(View headerView, int scrollOffset, int scrollRatio) {
    }

    /**
     * 头部处于刷新状态 （触发下拉刷新的时候调用）
     *
     * @param headerView 头部View
     */
    @Override
    public void onRefresh(View headerView) {
    }

    /**
     * 刷新完成，头部收起
     *
     * @param headerView 头部View
     @param isSuccess  是否刷新成功
     */
    @Override
    public void onRetract(View headerView, boolean isSuccess) {
    }
}

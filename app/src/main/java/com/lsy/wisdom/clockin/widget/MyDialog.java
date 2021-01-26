package com.lsy.wisdom.clockin.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Create by lsy on 2019/8/12
 * MODO :
 */
public class MyDialog extends Dialog {

    private int layout;

    public MyDialog(Context context, int theme, int layout) {
        super(context, theme);
        this.layout = layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
    }
}

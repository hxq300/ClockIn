package com.lsy.wisdom.clockin.widget;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.utils.GeneralMethod;


/**
 * Create by lsy on 2019/10/30
 * MODO : 自定义打电话弹窗
 */
public class FeedTip {

    private Context context;
    private MyDialog dialog;


    public FeedTip(Context context) {
        this.context = context;
    }

    public void showDialog() {
        dialog = new MyDialog(context, R.style.DialogTheme, R.layout.dialog_tip);
        GeneralMethod.XiuGaiDialog(context, dialog);
        dialog.show();

        TextView guanbi=(TextView)dialog.findViewById(R.id.tv_close);

        guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void dismiss() {
        dialog.dismiss();
    }

}

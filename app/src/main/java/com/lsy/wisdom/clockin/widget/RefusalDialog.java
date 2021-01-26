package com.lsy.wisdom.clockin.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.utils.GeneralMethod;


/**
 * Create by lsy on 2020/6/23
 * MODO : 申请驳回弹窗
 */
public class RefusalDialog {

    private Context context;
    private MyDialog dialog;


    public RefusalDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        dialog = new MyDialog(context, R.style.DialogTheme, R.layout.dialog_tip);
        GeneralMethod.XiuGaiDialog(context, dialog);
        dialog.show();

        TextView guanbi = (TextView) dialog.findViewById(R.id.tv_close);

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

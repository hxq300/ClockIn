package com.lsy.wisdom.calendardemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lsy.wisdom.calendardemo.bean.DateEntity;
import com.lsy.wisdom.calendardemo.view.DataView;

public class MainActivity extends FragmentActivity {
    private DataView dataView;
    private TextView info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        dataView = (DataView) findViewById(R.id.week);
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                info.setText("日期：" + date.date + "\n" +
                        "周几：" + date.weekName + "\n" +
                        "今日：" + date.isToday + "\n" +
                        "时间戳：" + date.million + "\n");
                Log.e("wenzhiao--------------", date.toString());
            }
        });
        dataView.getData("2017-04-19");
    }

}

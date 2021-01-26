package com.lsy.wisdom.clockin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.AddLogActivity;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/22
 * todo : 文档
 */
public class DocActivity extends AppCompatActivity {

    @BindView(R.id.doc_toolbar)
    IToolbar docToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        setSupportActionBar(docToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

    }


    private void initBar() {

        Menu menu = docToolbar.getMenu();
        menu.clear();

        docToolbar.inflateMenu(R.menu.toolbar_menu);
        docToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;
                    case 1://新建日志
//
//                        Intent addLog = new Intent(LogActivity.this, AddLogActivity.class);
//                        startActivity(addLog);

                        break;
                    default:
                        break;
                }
            }
        });
    }

}

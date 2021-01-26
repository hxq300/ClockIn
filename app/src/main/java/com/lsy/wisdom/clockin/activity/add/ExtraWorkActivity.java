package com.lsy.wisdom.clockin.activity.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.mvp.append.AddInterface;
import com.lsy.wisdom.clockin.mvp.append.AddPresent;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.GeneralMethod;
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/12
 * todo : 加班申请
 */
public class ExtraWorkActivity extends AppCompatActivity implements AddInterface.View {


    @BindView(R.id.extra_work_toolbar)
    IToolbar extraWorkToolbar;
    @BindView(R.id.ew_content)
    EditText ewContent;
    @BindView(R.id.add_to)
    TextView addTo;

    private AddInterface.Presenter presenter;

    private SharedUtils sharedUtils;

    private String listIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_work);
        setSupportActionBar(extraWorkToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        sharedUtils = new SharedUtils(ExtraWorkActivity.this, SharedUtils.CLOCK);

        listIds = sharedUtils.getData(SharedUtils.LISTID, "[]");

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }


        presenter = new AddPresent(this, ExtraWorkActivity.this);

    }


    private void initBar() {
        extraWorkToolbar.inflateMenu(R.menu.toolbar_menu);
        extraWorkToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
            @Override
            public void onClickListener(int pos) {
                switch (pos) {
                    case 0:
                        finish();
                        Log.v("TTT", "返回");
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void setFailure(String message) {
        ToastUtils.showBottomToast(this, "" + message);
    }

    @Override
    public void setSuccess() {
        ToastUtils.showBottomToast(this, "添加成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.distory();
    }

    @OnClick({R.id.check_line, R.id.ew_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_line:
                Intent intent = new Intent(ExtraWorkActivity.this, SubmitToActivity.class);
                startActivity(intent);
                break;
            case R.id.ew_commit:
                if (ewContent.getText().toString().length() > 1) {
                    if (GeneralMethod.isFastClick()) {
                        L.log("list=", sharedUtils.getData(SharedUtils.LISTID, "[]"));
                        presenter.addCoutentSP(sharedUtils.getData(SharedUtils.LISTID, "[]"), OKHttpClass.getUserId(ExtraWorkActivity.this), OKHttpClass.getConglomerate(this), OKHttpClass.getToken(this), "加班", ewContent.getText().toString());
                    }

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }
    }

}

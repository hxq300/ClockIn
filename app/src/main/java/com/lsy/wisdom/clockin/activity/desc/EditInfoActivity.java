package com.lsy.wisdom.clockin.activity.desc;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.bean.UserData;
import com.lsy.wisdom.clockin.mvp.means.InformationInterface;
import com.lsy.wisdom.clockin.mvp.means.InformationPresent;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/6/10
 * todo :
 */
public class EditInfoActivity extends AppCompatActivity implements InformationInterface.View {

    @BindView(R.id.personal_toolbar)
    IToolbar personalToolbar;
    @BindView(R.id.signature)
    EditText signature;

    //=======
    private InformationInterface.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        setSupportActionBar(personalToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        //========
        presenter = new InformationPresent(this, EditInfoActivity.this);
        presenter.getInformation("" + OKHttpClass.getUserId(EditInfoActivity.this));

    }


    private void initBar() {
        personalToolbar.inflateMenu(R.menu.toolbar_menu);
        personalToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

    //提交
    @OnClick(R.id.p_commit)
    public void onViewClicked() {
        presenter.updateInformation(OKHttpClass.getUserId(EditInfoActivity.this), null, "" + signature.getText().toString());
    }

    @Override
    public void setInformation(UserData userData) {
        if (userData != null && userData.getSignature() != null) {
            signature.setText("" + userData.getSignature());
        }
    }

    @Override
    public void success() {
        finish();
    }
}

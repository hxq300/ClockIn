package com.lsy.wisdom.clockin.activity.add;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.lsy.wisdom.clockin.utils.L;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;
import com.lsy.wisdom.clockin.widget.IToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lsy on 2020/5/11
 * todo : 新建审批
 */
public class AddApprovalActivity extends AppCompatActivity implements AddInterface.View {


    @BindView(R.id.add_approval_toolbar)
    IToolbar addApprovalToolbar;
    @BindView(R.id.add_approval_content)
    EditText addApprovalContent;
    @BindView(R.id.add_approval_num)
    TextView addApprovalNum;
    @BindView(R.id.add_to)
    TextView addTo;

    private AddInterface.Presenter presenter;

    private SharedUtils sharedUtils;

    private String listIds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_approval);
        setSupportActionBar(addApprovalToolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

        initBar();

        sharedUtils = new SharedUtils(AddApprovalActivity.this, SharedUtils.CLOCK);

        listIds = sharedUtils.getData(SharedUtils.LISTID, "[]");

        String nameIds = sharedUtils.getData(SharedUtils.NAMEID, null);
        if (nameIds != null) {
            addTo.setText("" + nameIds.substring(1, nameIds.length() - 1));
            addTo.setTextColor(0xff333333);
        }


        presenter = new AddPresent(this, AddApprovalActivity.this);

        addApprovalContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                addApprovalNum.setText(i + "/200");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                addApprovalNum.setText(editable.length() + "/200");
            }
        });


    }


    private void initBar() {
        addApprovalToolbar.inflateMenu(R.menu.toolbar_menu);
        addApprovalToolbar.setIToolbarCallback(new IToolbar.IToolbarCallback() {
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

    @OnClick({R.id.check_line, R.id.add_approval_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_line:
                Intent intent = new Intent(AddApprovalActivity.this, SubmitToActivity.class);
                startActivity(intent);
                break;
            case R.id.add_approval_submit:
                if (addApprovalContent.getText().toString() != null) {
                    L.log(listIds.toString());
                    presenter.addCoutentSP(sharedUtils.getData(SharedUtils.LISTID, "[]"), OKHttpClass.getUserId(AddApprovalActivity.this), OKHttpClass.getConglomerate(this), OKHttpClass.getToken(this), "审批", addApprovalContent.getText().toString());
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

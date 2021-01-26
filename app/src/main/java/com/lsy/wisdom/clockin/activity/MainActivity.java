package com.lsy.wisdom.clockin.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.FeedbackActivity;
import com.lsy.wisdom.clockin.fragment.MyFragment;
import com.lsy.wisdom.clockin.fragment.index.MessageFragment;
import com.lsy.wisdom.clockin.fragment.index.MineFragment;
import com.lsy.wisdom.clockin.fragment.index.WorkbenchFragment;
import com.lsy.wisdom.clockin.permission.QuanXian;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;
import com.lsy.wisdom.clockin.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements QuanXian.OnPermission {

    @BindView(R.id.id_one_img)
    ImageView imgOne;
    @BindView(R.id.id_one_tex)
    TextView textOne;
    @BindView(R.id.id_two_img)
    ImageView imgTwo;
    @BindView(R.id.id_two_tex)
    TextView textTwo;
    @BindView(R.id.id_three_img)
    ImageView imgThree;
    @BindView(R.id.id_three_text)
    TextView textThree;


    private ArrayList<MyFragment> fragments;//主页碎片
    private int open = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xF3ef3ed);
        }

//        ToastUtils.showBottomToast(this, "userId:" + OKHttpClass.getUserId(this));

        //====
        fragments = getFragments();
//        setDefaultFragment();
        setFragmentNum(open);


        //判断权限是否全部打开
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_LOGS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SET_DEBUG_APP) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_APN_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }

    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        QuanXian quanXian = new QuanXian(MainActivity.this, MainActivity.this);
        quanXian.setOnPermission_isok(this);
        quanXian.requestPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    @OnClick({R.id.id_one_tab, R.id.id_two_tab, R.id.id_three_tab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_one_tab:
                setFragmentNum(0);
                break;
            case R.id.id_two_tab:
                setFragmentNum(1);
                break;
            case R.id.id_three_tab:
                setFragmentNum(2);
                break;
            default:
                break;
        }
    }


    /**
     * 设置Fragment
     *
     * @return
     */
    private ArrayList<MyFragment> getFragments() {
        ArrayList<MyFragment> fragments = new ArrayList<>();

        fragments.add(MessageFragment.newInstance());
        fragments.add(WorkbenchFragment.newInstance());
        fragments.add(MineFragment.newInstance());
        return fragments;
    }

    /**
     * 设置选中的Fragment
     */
    private MyFragment now_fragment;

    private void setFragmentNum(int num) {
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        now_fragment = null;
        switch (num) {
            case 0:
                now_fragment = fragments.get(0);

                textOne.setTextColor(getResources().getColor(R.color.tab_focuse));
                textTwo.setTextColor(getResources().getColor(R.color.tab_nofocuse));
                textThree.setTextColor(getResources().getColor(R.color.tab_nofocuse));

                imgOne.setImageResource(R.mipmap.tab_home_blue);
                imgTwo.setImageResource(R.mipmap.tab_work);
                imgThree.setImageResource(R.mipmap.tab_mine);

                break;

            case 1:
                now_fragment = fragments.get(1);
                textOne.setTextColor(getResources().getColor(R.color.tab_nofocuse));
                textTwo.setTextColor(getResources().getColor(R.color.tab_focuse));
                textThree.setTextColor(getResources().getColor(R.color.tab_nofocuse));

                imgOne.setImageResource(R.mipmap.tab_home);
                imgTwo.setImageResource(R.mipmap.tab_work_blue);
                imgThree.setImageResource(R.mipmap.tab_mine);
                break;

            case 2:
                now_fragment = fragments.get(2);
                textOne.setTextColor(getResources().getColor(R.color.tab_nofocuse));
                textTwo.setTextColor(getResources().getColor(R.color.tab_nofocuse));
                textThree.setTextColor(getResources().getColor(R.color.tab_focuse));

                imgOne.setImageResource(R.mipmap.tab_home);
                imgTwo.setImageResource(R.mipmap.tab_work);
                imgThree.setImageResource(R.mipmap.tab_mine_blue);
                break;

            default:
                break;
        }
        // 使用当前Fragment的布局替代id_content的控件
        transaction.replace(R.id.layFrame, now_fragment);
        transaction.commit();
    }

    @Override
    public void one_permission_isok(String permission_name) {

    }

    @Override
    public void one_permission_is_refuse(String permission_name) {
        requestPermissions();
    }

    @Override
    public void one_permission_is_refuse_no_prompt(String permission_name) {

    }
}

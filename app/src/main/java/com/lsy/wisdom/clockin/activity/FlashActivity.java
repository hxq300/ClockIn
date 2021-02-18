package com.lsy.wisdom.clockin.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.lsy.wisdom.clockin.R;
import com.lsy.wisdom.clockin.activity.add.UpdateSignedActivity;
import com.lsy.wisdom.clockin.dialog.OnUserAgreementInterface;
import com.lsy.wisdom.clockin.dialog.UserAgreementDialog;
import com.lsy.wisdom.clockin.request.OKHttpClass;
import com.lsy.wisdom.clockin.utils.SharedUtils;
import com.lsy.wisdom.clockin.utils.StatusBarUtil;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 2020/5/14
 * todo :
 */
public class FlashActivity extends AppCompatActivity{

    @BindView(R.id.flash_image)
    ImageView flashImage;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        ButterKnife.bind(this);

        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0xFFA400);
        }

        handler = new Handler();
        ObjectAnimator.ofFloat(flashImage, "translationX", 300f, 0F).setDuration(2600).start();//设置X轴移动200
        initGif();
    }

    private void initGif() {

//        Glide.with(this)
//                .asGif()
//                .load(R.drawable.flash_gif)
//                .into(new ImageViewTarget<GifDrawable>(flashImage) {
//                    @Override
//                    protected void setResource(@Nullable GifDrawable resource) {
//                        if (resource != null) {
//                            if (true) {
//                                resource.setLoopCount(1);
//                                view.setImageDrawable(resource);
//                            } else {
//                                view.setImageBitmap(resource.getFirstFrame());
//                            }
//                        }
//                    }
//                });
    // todo:老业务

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (OKHttpClass.getUserId(FlashActivity.this) == 0) {
//                    //如果用户是第一次启动
//                    Intent toLogin = new Intent(FlashActivity.this, LoginActivity.class);
//                    startActivity(toLogin);
//                    finish();
//                } else {
//                    Intent tomain = new Intent(FlashActivity.this, MainActivity.class);
//                    startActivity(tomain);
//                    finish();
//                }
//            }
//        }, 3000);

        // todo: 添加用户隐私政策
        SharedUtils sharedUtils = new SharedUtils(FlashActivity.this, SharedUtils.CLOCK);
        UserAgreementDialog userAgreementDialog = new UserAgreementDialog();
        if (!sharedUtils.getBoolean(SharedUtils.IS_AGREEMENT)){
            userAgreementDialog.setOnUserAgreementInterface(new OnUserAgreementInterface() {
                @Override
                public void onRefuseClick() { // 拒接
                    finish();
                }

                @Override
                public void onAgreeClick() { // 同意

                    sharedUtils.setBoolean(SharedUtils.IS_AGREEMENT,true);
                    login();
                }


            });
        }else {
            login();
        }

        userAgreementDialog.show(getSupportFragmentManager(),"");

    }

    private void login() {
        if (OKHttpClass.getUserId(FlashActivity.this) == 0) {
            //如果用户是第一次启动
            Intent toLogin = new Intent(FlashActivity.this, LoginActivity.class);
            startActivity(toLogin);
            finish();
        } else {
            Intent tomain = new Intent(FlashActivity.this, MainActivity.class);
            startActivity(tomain);
            finish();
        }
    }


}

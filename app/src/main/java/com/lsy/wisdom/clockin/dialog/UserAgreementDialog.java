package com.lsy.wisdom.clockin.dialog;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.lsy.wisdom.clockin.R;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;

public class UserAgreementDialog extends BaseDialogFragment {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_right)
    TextView tv_right;

    private OnUserAgreementInterface onUserAgreementInterface;


    public void setOnUserAgreementInterface(OnUserAgreementInterface onUserAgreementInterface) {
        this.onUserAgreementInterface = onUserAgreementInterface;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.user_agreement_dialog;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws ExecutionException, InterruptedException {
        setCancelable(false);
        initWebView();

        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onUserAgreementInterface!=null)
                    onUserAgreementInterface.onRefuseClick();
                dismissAllowingStateLoss();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onUserAgreementInterface!=null)
                    onUserAgreementInterface.onAgreeClick();
                dismissAllowingStateLoss();
            }
        });
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDatabaseEnabled(true);
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        String dir = "/sdcard/temp";//设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if(getContext()!=null)
        webSettings.setGeolocationDatabasePath(getContext().getFilesDir().getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(this, "AndroidWebView");
        webView.loadUrl("http://101.132.154.10:8080/xiyi/xiyi.html");
    }


}

package com.lsy.wisdom.clockin.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import java.net.URISyntaxException;

/**
 * Created by lsy on 2020/5/6
 * todo : fragment基类
 */
public class MyFragment extends Fragment {

    /**
     * 根据url的scheme处理跳转第三方app的业务
     */
    public boolean shouldOverrideUrlLoadingByApp(WebView view, String url) {
        return shouldOverrideUrlLoadingByAppInternal(view, url, true);
    }


    /**
     * 广告业务的特殊处理
     * 根据url的scheme处理跳转第三方app的业务
     * 如果应用程序可以处理该url,就不要让浏览器处理了,返回true;
     * 如果没有应用程序可以处理该url，先判断浏览器能否处理，如果浏览器也不能处理，直接返回false拦截该url，不要再让浏览器处理。
     * 避免出现当deepLink无法调起目标app时，展示加载失败的界面。
     * <p>
     * 某些含有deepLink链接的网页，当使用deepLink跳转目标app失败时，如果将该deepLinkUrl交给系统处理，系统处理不了，会导致加载失败；
     * 示例：
     * 网页Url：https://m.ctrip.com/webapp/hotel/hoteldetail/687592/checkin-1-7.html?allianceid=288562&sid=964106&sourceid=2504&sepopup=12
     * deepLinkUrl：ctrip://wireless/InlandHotel?checkInDate=20170504&checkOutDate=20170505&hotelId=687592&allianceid=288562&sid=960124&sourceid=2504&ouid=Android_Singapore_687592
     *
     * @param interceptExternalProtocol 是否拦截自定义的外部协议，true：拦截，无论如何都不让浏览器处理；false：不拦截，如何没有成功处理该协议，继续让浏览器处理
     */
    public boolean shouldOverrideUrlLoadingByAppInternal(WebView view, String url, boolean interceptExternalProtocol) {
        if (isAcceptedScheme(url)) {
            //如果这个地址是浏览器可以处理的地址
            return false;
        }
        Intent intent;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            return interceptExternalProtocol;
        }

        intent.setComponent(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            intent.setSelector(null);
        }

        //该Intent无法被设备上的应用程序处理
        if (getContext().getPackageManager().resolveActivity(intent, 0) == null) {
            return tryHandleByMarket(intent) || interceptExternalProtocol;
        }

        try {
            MyFragment.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            return interceptExternalProtocol;
        }
        return true;
    }


    public boolean tryHandleByMarket(Intent intent) {
        String packagename = intent.getPackage();
        if (packagename != null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packagename));
            try {
                MyFragment.this.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * 该url是否属于浏览器能处理的内部协议
     */
    public boolean isAcceptedScheme(String url) {
        if (Patterns.WEB_URL.matcher(url).matches() || URLUtil.isValidUrl(url)) {
            return true;
        } else {
            return false;
        }
    }

}

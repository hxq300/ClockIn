package com.lsy.wisdom.clockin.permission;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by lsy on 2020/5/12
 * todo :
 */
public class QuanXian {
    private Context context;
    private Activity activity;

    private static final String TAG = "RxPermissionTest";

    public QuanXian(Context context) {
        this.context = context;
    }

    public QuanXian(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void requestPermissions(String... permissions) {
        if (permissions == null) {
            return;
        }
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                            if (onPermission != null) {
                                onPermission.one_permission_isok(permission.name);
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                            if (onPermission != null) {
                                onPermission.one_permission_is_refuse(permission.name);
                            }
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");

                            if (onPermission != null) {
                                onPermission.one_permission_is_refuse_no_prompt(permission.name);
                            }

                        }
                    }
                });
    }

    public interface OnPermission {
        void one_permission_isok(String permission_name);

        void one_permission_is_refuse(String permission_name);

        void one_permission_is_refuse_no_prompt(String permission_name);
    }

    public OnPermission onPermission;

    public void setOnPermission_isok(OnPermission onPermission_isok) {
        this.onPermission = onPermission_isok;
    }

}

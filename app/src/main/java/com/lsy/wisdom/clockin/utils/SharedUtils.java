package com.lsy.wisdom.clockin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Create by lsy on 2019/12/04
 * MODO :
 */
public class SharedUtils {

    //文件名
    public static final String CLOCK = "ColockIn";

    //
    public static final String USERID = "userId";//
    public static final String COMPANYID = "companyId";//
    public static final String CLOCKID = "clockId";//
    public static final String JITUANID = "jituanId";//

    public static final String LISTID = "listId";//
    public static final String NAMEID = "nameId";//

    public static final String JOINID = "joinId";//
    public static final String JOINNAME = "joinName";//

    public static final String PRINCIPAL = "principal";//负责人
    public static final String PRINCIPALID = "principalId";//负责人ID

    public static final String IRDETAILS = "ins_record_details";//检查记录详情


    private Context context;
    private SharedPreferences share;
    private SharedPreferences.Editor edit;

    public SharedUtils(Context context, String filename) {
        if (context == null) {
            return;
        }
        this.context = context;
        //指定操作的文件名称
        share = context.getSharedPreferences(filename, MODE_PRIVATE);
        //编辑文件
        edit = share.edit();
    }

    public void setData(String key, String value) {
        try {
            if (edit != null) {
                edit.putString(key, value);
                edit.commit();    //保存数据信息
            }
        } catch (Exception e) {

        }
    }

    public void setBoolean(String key, boolean value) {
        try {
            if (edit != null) {
                edit.putBoolean(key, value);
                edit.commit();    //保存数据信息
            }
        } catch (Exception e) {

        }
    }

    public void setData(String key, int value) {
        try {
            if (edit != null) {
                edit.putInt(key, value);
                edit.commit();    //保存数据信息
            }
        } catch (Exception e) {

        }
    }

    public String getData(String key) {
        String data = share.getString(key, "");
        return data;
    }

    public int getIntData(String key) {
        int data = share.getInt(key, 0);
        return data;
    }

    public String getData(String key, String moren) {
        String data = share.getString(key, moren);
        return data;
    }

    public boolean getBoolean(String key) {
        boolean data = share.getBoolean(key, false);
        return data;
    }

    public void remove_data() {
        share.edit().clear().commit();
        //用户推出以后，设置别名为空
//        setJpushBieMing("");
    }

    /**
     * 设置激光推送的别名
     */
//    private void setJpushBieMing(String user_id) {
//        //设置别名，一对一推送使用
//        BieMing bieMing = new BieMing(context);
//        bieMing.setAlias(user_id);
//    }

}

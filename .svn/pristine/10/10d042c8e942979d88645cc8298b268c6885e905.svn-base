package com.yda.yiyunchain.DataUtil;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.yda.yiyunchain.Bean.LoginInfoBean;

public class SaveGetSharedPreferencesInfo {


    final static String SAVECITYKEY = "logininfo";
    private static String defaultcity = "";


    public static void saveLoginInfo(Context context, LoginInfoBean loginInfoBean) {
//        Log.e("存储的对象json", "json" + beanToJSONString(loginInfoBean));
        SharedPreferencesUtils.setParam(context, SAVECITYKEY, beanToJSONString(loginInfoBean));
    }

    public static void clearLoginInfo(Context context) {
        SharedPreferencesUtils.clear(context, SAVECITYKEY);
    }

    public static LoginInfoBean getLoginInfo(Context context) {
        try {
            Gson gson = new Gson();
            String json = (String) SharedPreferencesUtils.getParam(context, SAVECITYKEY, defaultcity);
//            Log.e("取出来的对象json", "json" + json);
            LoginInfoBean res = gson.fromJson(json, LoginInfoBean.class);
            return res;
        } catch (Exception e) {
//            Log.e("取出来的对象json", "异常" + e.getMessage());
            return null;
        }

    }

    public static String beanToJSONString(Object bean) {
        return new Gson().toJson(bean);
    }
}

package com.yda.yiyunchain.AccountBind;

import android.app.Activity;

public class AccountBindUtil {


//    public static void


    /**
     * 绑定1 微信 2 支付宝
     */

    public static void bindingAccount(int accountType, Activity activity, BindingCallBack bindingCallBack) {

        if (accountType == 1) {
            new BindingWeiXin().bingAccount(bindingCallBack);
        } else {
            new BindingAlipay().authV2(activity,bindingCallBack);
        }


    }


}

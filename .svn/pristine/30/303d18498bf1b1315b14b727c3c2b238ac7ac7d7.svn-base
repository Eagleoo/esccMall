package com.yda.yiyunchain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yda.yiyunchain.PayUtils.PayInfo;

/**
 * Created by Administrator on 2018/5/11.
 */

public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);


        api.registerApp(PayInfo.WI_APP_ID);
    }
}

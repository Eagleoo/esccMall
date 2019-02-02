package com.yda.esccmall.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class AliaReceiver extends JPushMessageReceiver {
    private Context context;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                JPushInterface.setAlias(context, 200, context.getSharedPreferences("loginUser", Context.MODE_PRIVATE).getString("alias",""));
            }
        }
    };
        @Override
        public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
            super.onAliasOperatorResult(context, jPushMessage);
            this.context=context;
            if (jPushMessage.getErrorCode() == 6002) {//超时处理
                Message obtain = Message.obtain();
                obtain.what = 100;
                mHandler.sendMessageDelayed(obtain, 1000 * 60);//60秒后重新验证
            }else {
                Log.e("****用户*****",context.getSharedPreferences("loginUser", Context.MODE_PRIVATE).getString("alias",""));
                Log.e("onAliasOperatorResult: ", jPushMessage.getErrorCode()+"");
            }
        }

}

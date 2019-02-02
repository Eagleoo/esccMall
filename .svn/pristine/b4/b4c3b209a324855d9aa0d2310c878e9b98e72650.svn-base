package com.yda.yiyunchain.AccountBind;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.AuthTask;
import com.yda.yiyunchain.PayUtils.AliPayUtile.OrderInfoUtil2_0;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.yda.yiyunchain.PayUtils.PayInfo.ALI_APP_ID;
import static com.yda.yiyunchain.PayUtils.PayInfo.ALI_PARTNER;
import static com.yda.yiyunchain.PayUtils.PayInfo.RSA2_PRIVATE;

public class BindingAlipay {


    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static String TARGET_ID = "";

    BindingCallBack mCallBack;


    /**
     * 支付宝账户授权业务
     *
     * @param
     */
    public void authV2(final Activity activity, BindingCallBack callBack) {

        this.mCallBack = callBack;
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        TARGET_ID = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(ALI_PARTNER, ALI_APP_ID, TARGET_ID, true);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = RSA2_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, true);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.obj = result;
                doLoginBack(msg);

            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }


    @SuppressLint("CheckResult")
    private void doLoginBack(Message msg) {
        Observable.just(msg).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(Message message) throws Exception {
                        System.out.println("阿里奇拿" + ((Map<String, String>) message.obj).toString());
                        AuthResult authResult = new AuthResult((Map<String, String>) message.obj, true);
                        String resultStatus = authResult.getResultStatus();

                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                            // 获取alipay_open_id，调支付时作为参数extern_token 的value
                            // 传入，则支付账户为该授权账户
//                            Toast.makeText(PayDemoActivity.this,
//                                    "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                    .show();
                            mCallBack.onSuccess(authResult.getUserId());
                        } else {
                            // 其他状态值则为授权失败
                            mCallBack.onError("授权失败");
                        }
                    }


                });
    }

}

package com.yda.yiyunchain.PayUtils.AliPayUtile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.yda.yiyunchain.PayUtils.PayInfo.RSA2_PRIVATE;

/**
 * Created by Administrator on 2018/5/11.
 */

public class PayUtil {
    //APPID
//    public static final String APPID = "2018060460271585";
//    // 商户PID
//    public static final String PARTNER = "2088131266650563";
//    // 商户收款账号
//    // public static final String SELLER = "2201277132@qq.com";
//    public static final String SELLER = "1257032385@qq.com";
    // 商户私钥，pkcs8格式

    //    public static final String RSA_PRIVATE = "";
    PayCallBack payCallBack;

    public interface PayCallBack {
        void doCallBackSuccess(PayResult payResult);

        void doCallBackFailure(PayResult payResult);
    }

    /**
     * @param activity
     * @param payid    订单号
     * @param money    金额
     * @param callBack
     */

    public void payV2(final Activity activity,String  subject, String payid, double money, PayCallBack callBack) {
        payCallBack = callBack;


        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(payid, "商品名", money);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = RSA2_PRIVATE;

        String sign = OrderInfoUtil2_0.getSign(params, privateKey, true);

        final String orderInfo = orderParam + "&" + sign;

//        Log.e("payInfo:", "payInfo===" + orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.obj = result;
                doPayBack(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("CheckResult")
    private void doPayBack(Message msg) {
        Observable.just(msg).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(Message message) throws Exception {
                        PayResult payResult = new PayResult((Map<String, String>) message.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (payCallBack != null) {
                            if (TextUtils.equals(resultStatus, "9000")) {
                                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                                payCallBack.doCallBackSuccess(payResult);
                            } else {
                                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                                payCallBack.doCallBackFailure(payResult);
                            }
                        }
                    }


                });
    }

}

package com.yda.yiyunchain.PayUtils.AliPayUtile;

import com.yda.yiyunchain.PayUtils.PayInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/5/11.
 */

public class OrderInfoUtil2_0 {

    ;

    /**
     * 构造授权参数列表
     *
     * @param pid
     * @param app_id
     * @param target_id
     * @return
     */
    public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<String, String>();

        // 商户签约拿到的app_id，如：2013081700024223
        keyValues.put("app_id", app_id);

        // 商户签约拿到的pid，如：2088102123816631
        keyValues.put("pid", pid);

        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");

        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");

        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");

        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");

        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");

        // 商户唯一标识，如：kkkkk091125
        keyValues.put("target_id", target_id);

        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");

        // 签名类型
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

        return keyValues;
    }
    //构造支付订单参数列表

    /**
     * @param out_trade_no 订单号
     * @param subject      标题
     * @return
     */
    public static Map<String, String> buildOrderParamMap(String out_trade_no, String subject, double money) {
        Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put("app_id", PayInfo.ALI_APP_ID);

        keyValues.put("method", "alipay.trade.app.pay");

        //请求使用的编码格式

        keyValues.put("charset", "utf-8");

        keyValues.put("sign_type", "RSA2");

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        keyValues.put("timestamp", time);

        keyValues.put("version", "1.0");

        // 服务器异步通知页面路径
        keyValues.put("notify_url", PayInfo.ALI_CALL_BACK);

        keyValues.put("biz_content",
                "{" +
                        "\"subject\":\"" + subject + "\"," +
                        "\"out_trade_no\":\"" + out_trade_no + "\"," +
                        "\"total_amount\":\"" + money + "\"," +
                        "\"product_code\":\"" + "QUICK_MSECURITY_PAY" + "\"," +
                        "}"

        );

//        // 签约合作者身份ID
//        keyValues.put("partner", PayInfo.ALI_PARTNER);
//        // 签约卖家支付宝账号
//        keyValues.put("seller_id", PayInfo.ALI_SELLER);
//        // 商户网站唯一订单号
//        keyValues.put("out_trade_no", payid);
//        // 商品名称
//        keyValues.put("subject", orderTitle);
//        // 商品详情
//        keyValues.put("body", orderTitle);
//        // 商品金额
//        keyValues.put("total_fee", money + "");
//
//        // 服务接口名称， 固定值
//        keyValues.put("service", "mobile.securitypay.pay");
//        // 支付类型， 固定值
//        keyValues.put("payment_type", "1");
//        // 参数编码， 固定值
//        keyValues.put("_input_charset", "utf-8");
//        keyValues.put("it_b_pay", "1c");
//        keyValues.put("return_url", " ");


        return keyValues;
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {

        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < keys.size() - 1; i++) {

            String key = keys.get(i);

            String value = map.get(key);

            sb.append(buildKeyValue(key, value, true));

            sb.append("&");

        }

        String tailKey = keys.get(keys.size() - 1);

        String tailValue = map.get(tailKey);

        sb.append(buildKeyValue(tailKey, tailValue, true));


        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
//        Log.e("buildKeyValue", "K:" + key + "value:" + value);
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

}


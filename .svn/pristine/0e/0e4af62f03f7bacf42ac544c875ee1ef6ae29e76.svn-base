package com.yda.yiyunchain.PayUtils.WeiXinUtlis;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import com.mob.tools.network.SSLSocketFactoryEx;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yda.yiyunchain.PayUtils.PayInfo;
import com.yda.yiyunchain.util.MD5;
import com.yda.yiyunchain.util.Util;
import com.yda.yiyunchain.widget.ShapeLoadingDialog;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/5/11.
 */

public class WeiXinPay {
    private IWXAPI msgApi;
    private ShapeLoadingDialog pd;
    private StringBuffer sb = new StringBuffer();
    private Context mContenxt;

    private String orderBody;
    private String orderId;
    private double money;

    public WeiXinPay(Context context, double money, String orderId, String order) {
//        pd = customProgressDialog;
        mContenxt = context;
        orderBody = order;
        this.orderId = orderId;
        this.money = money;
    }
    public WeiXinPay(ShapeLoadingDialog customProgressDialog, Context context, double money, String orderId, String order) {
        pd = customProgressDialog;
        mContenxt = context;
        orderBody = order;
        this.orderId = orderId;
        this.money = money;
    }

    public void Pay() {
        msgApi = WXAPIFactory.createWXAPI(mContenxt, null);
        msgApi.registerApp(PayInfo.WI_APP_ID);
        new GetPrepayIdTask().execute();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            pd.getBuilder().loadText("正在创建预支付订单..").build().show();
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (null != result && !result.containsKey("error")) {
                sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
                pd.getBuilder().loadText("正在调起微信...").build().show();
                genPayReq(result);
            } else {
                Util.show(mContenxt, result.get("error"));
                pd.cancel();
                pd.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

//            Log.e("orion1", entity + "___1");
            String content = "";
            try {
                byte[] buf = httpPost(url, new String(entity.getBytes(), "ISO8859-1"));
                content = new String(buf);
//                Log.e("orion2", content);
                Map<String, String> xml = decodeXml(content);

                return xml;
            } catch (UnsupportedEncodingException e) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("error", content);
                map.put("srouce", e.getMessage());
                return map;
            }
        }
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
//            Log.e("orionXml", e.toString());
        }
        return null;

    }

    private String genProductArgs() {

        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", PayInfo.WI_APP_ID));
            packageParams.add(new BasicNameValuePair("body", orderBody));
            packageParams.add(new BasicNameValuePair("mch_id", PayInfo.WI_MER_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
//            if (Web.test_url.equals(Web.url) || Web.test_url2.equals(Web.url))
//                packageParams.add(new BasicNameValuePair("notify_url", "http://test.yda360.cn/mmPay/wxV3.aspx"));
//            else
            packageParams.add(new BasicNameValuePair("notify_url", PayInfo.WI_CALL_BACK));
            packageParams.add(new BasicNameValuePair("out_trade_no", orderId));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "10.10.10.10"));
//            if (Web.test_url.equals(Web.url) || Web.test_url2.equals(Web.url))
//                packageParams.add(new BasicNameValuePair("total_fee", "1"));
//            else
            packageParams.add(new BasicNameValuePair("total_fee", ((int) (money * 100)) + ""));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return xmlstring;

        } catch (Exception e) {
//            LogUtils.e("genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(PayInfo.WI_API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Log.e("orionPackageSign", packageSign);
        return packageSign;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

//        Log.e("oriontoXml", sb.toString());
        return sb.toString();
    }

    public static byte[] httpPost(String url, String entity) {
        if (url == null || url.length() == 0) {
//            Log.e("wxhttppost", "httpPost, url is null");
            return null;
        }
        HttpClient httpClient = getNewHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new StringEntity(entity));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                Log.e("wxhttppost", "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
                return null;
            }
            return EntityUtils.toByteArray(resp.getEntity());
        } catch (Exception e) {
//            Log.e("wxhttppost", "httpPost exception, e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private void genPayReq(Map<String, String> resultunifiedorder) {
        PayReq req = new PayReq();
        req.appId = PayInfo.WI_APP_ID;
        req.partnerId = PayInfo.WI_MER_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");
//        Log.e("orionReq", signParams.toString());

        msgApi.registerApp(PayInfo.WI_APP_ID);
        msgApi.sendReq(req);
        if (null != pd) {
            pd.cancel();
            pd.dismiss();
        }
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(PayInfo.WI_API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Log.e("orionSign", appSign);
        return appSign;
    }

}

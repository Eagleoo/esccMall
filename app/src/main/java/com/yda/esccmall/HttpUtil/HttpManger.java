package com.yda.esccmall.HttpUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yda.esccmall.Bean.UserInfoBean;
import com.yda.esccmall.util.EncryptionUtil;
import com.yda.esccmall.util.UrlUtil;
import com.yda.esccmall.util.Web;
import com.yda.esccmall.widget.ShapeLoadingDialog;

import java.util.Date;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HttpManger {

    public interface DoRequetCallBack {
        void onSuccess(String t);

        void onError(String t);
    }

    /**
     * @param context
     * @param uri
     * @param params           请求的参数
     * @param message
     * @param doRequetCallBack
     */

    public static void postRequest(final Context context, final String uri, Map<String, String> params, String message, final DoRequetCallBack doRequetCallBack) {
        final ShapeLoadingDialog shapeLoadingDialog = new ShapeLoadingDialog.Builder(context)
                .loadText(message)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .build();
        SharedPreferences sp=context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);


        shapeLoadingDialog.show();

        String timestamps = Integer.valueOf(String.valueOf(new Date().getTime() / 1000)) + "";
        UserInfoBean userInfoBean = UserInfoBean.getUser();

        if (!sp.getString("token","").equals("")){
            params.put("token", sp.getString("token",""));
        }

//        StringBuilder p1 = new StringBuilder();
//        if (null != params) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                p.append("&" + entry.getKey() + "=" + entry.getValue());
//            }
//        }
//        Log.e("post参数", "post:" + p.toString());
//        Log.e("header参数", "header:" + "X-Auth-Sign=" + EncryptionUtil.getSign(params, uri, timestamps) + "&X-Auth-Key=" + EncryptionUtil.KEY
//                + "&X-Auth-TimeStamp=" + timestamps);


            OkGo.<String>post(Web.url + uri).tag(context)
                    .headers("X-Auth-Sign", EncryptionUtil.getSign(params, uri, timestamps))
                    .headers("X-Auth-Key", EncryptionUtil.KEY)
                    .headers("X-Auth-TimeStamp", timestamps)
                    .headers("timestamp", timestamps)
                    .headers("method", "POST")
                    .headers("uri", UrlUtil.getURLEncoderString(uri))
                    .headers("contentlength", EncryptionUtil.getRequestCountLength(params) + "")
                    .headers("key", EncryptionUtil.KEY)
                    .params(params)
                    .execute(new StringCallback() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void onSuccess(Response<String> response) {
                            Observable.just(response).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Response<String>>() {
                                        @Override
                                        public void accept(Response<String> stringResponse) throws Exception {
                                            Gson gson = new Gson();
                                            Log.e("数据", "stringResponse:" + stringResponse.body());
                                            //BaseBean baseBean = gson.fromJson(stringResponse.body(), BaseBean.class);

//                                        if (baseBean.getStatus() == -1) {
//                                            Util.show(context, baseBean.getMsg());
//                                            Activity activity = (Activity) context;
//                                            activity.startActivity(new Intent(activity, LoginActivity.class));
//                                            ActivityCollector.finishAll();
//                                            return;
//                                        }

                                            if (shapeLoadingDialog!=null){
                                                shapeLoadingDialog.dismiss();
                                            }


                                            doRequetCallBack.onSuccess(stringResponse.body());
                                        }
                                    });


                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            shapeLoadingDialog.dismiss();
                            doRequetCallBack.onError(response.body());

                        }
                    });
    }
    public static void postRequest(final Context context, boolean isShow,final String uri, Map<String, String> params, String message, final DoRequetCallBack doRequetCallBack) {
        final ShapeLoadingDialog shapeLoadingDialog = new ShapeLoadingDialog.Builder(context)
                .loadText(message)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .build();
        if (isShow){
            shapeLoadingDialog.show();
        }
        SharedPreferences sp=context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);

        String timestamps = Integer.valueOf(String.valueOf(new Date().getTime() / 1000)) + "";
        UserInfoBean userInfoBean = UserInfoBean.getUser();


        if (!sp.getString("token","").equals("")){
            params.put("token", sp.getString("token",""));
        }

        OkGo.<String>post(Web.url + uri).tag(context)
                .headers("X-Auth-Sign", EncryptionUtil.getSign(params, uri, timestamps))
                .headers("X-Auth-Key", EncryptionUtil.KEY)
                .headers("X-Auth-TimeStamp", timestamps)
                .headers("timestamp", timestamps)
                .headers("method", "POST")
                .headers("uri", UrlUtil.getURLEncoderString(uri))
                .headers("contentlength", EncryptionUtil.getRequestCountLength(params) + "")
                .headers("key", EncryptionUtil.KEY)
                .params(params)
                .execute(new StringCallback() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(Response<String> response) {
                        Observable.just(response).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Response<String>>() {
                                    @Override
                                    public void accept(Response<String> stringResponse) throws Exception {
                                        Gson gson = new Gson();
                                        Log.e("数据", "stringResponse:" + stringResponse.body());
                                        //BaseBean baseBean = gson.fromJson(stringResponse.body(), BaseBean.class);

                                        if (shapeLoadingDialog!=null){
                                            shapeLoadingDialog.dismiss();
                                        }


                                        doRequetCallBack.onSuccess(stringResponse.body());
                                    }
                                });


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (shapeLoadingDialog!=null){
                            shapeLoadingDialog.dismiss();
                        }
                        doRequetCallBack.onError(response.body());

                    }
                });
    }
}

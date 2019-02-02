package com.yda.yiyunchain.AccountBind;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BindingWeiXin implements PlatformActionListener {
    BindingCallBack mCallBack;

    public void bingAccount(BindingCallBack bindingCallBack) {
        mCallBack = bindingCallBack;
        Platform plat = new Wechat();
        plat.setPlatformActionListener(this);
        plat.removeAccount(true);
        plat.SSOSetting(false);
        plat.showUser(null);
    }


    /**
     * @param status 0 成功 1 失败 2 取消
     */
    @SuppressLint("CheckResult")
    public void processfun(int status, final String openid) {
        Observable.just(status).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer status) throws Exception {
                        if (status == 0) {
                            mCallBack.onSuccess(openid);
                        } else if (status == 1) {
                            mCallBack.onError("");
                        } else if (status == 2) {
                            mCallBack.onCancel("");
                        }


                    }

                });
    }


    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {

        if (action == Platform.ACTION_USER_INFOR) {
            //授权成功
//            LogUtil.e("[" + hashMap + "]");
//            LogUtil.e("------User getUserGender ---------" + platform.getDb().getUserGender());
//            LogUtil.e("------User getUserIcon ---------" + platform.getDb().getUserIcon());
//            LogUtil.e("------User getUserName ---------" + platform.getDb().getUserName());
//            LogUtil.e("------User Name ---------" + platform.getName());
//            LogUtil.e("------User ID ---------" + platform.getDb().getUserId());
//            LogUtil.e("------Open ID ---------" + platform.getDb().getUserId());
//            banknumber.setText(platform.getDb().getUserName());
            processfun(0, platform.getDb().getUserId());

        }


    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e("rx--onComplete ", Thread.currentThread().getName());
        processfun(1, "");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        processfun(2, "");
    }
}

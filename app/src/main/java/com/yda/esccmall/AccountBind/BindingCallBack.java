package com.yda.esccmall.AccountBind;

public interface BindingCallBack {

    public void onSuccess(String id);


    public void onError(String message);

    public void onCancel(String message);
}

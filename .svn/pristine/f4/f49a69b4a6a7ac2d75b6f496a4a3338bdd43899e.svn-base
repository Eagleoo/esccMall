package com.yda.yiyunchain.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yda.yiyunchain.AccountBind.AccountBindUtil;
import com.yda.yiyunchain.AccountBind.BindingCallBack;
import com.yda.yiyunchain.HttpUtil.HttpManger;
import com.yda.yiyunchain.R;
import com.yda.yiyunchain.util.SelectorFactory;
import com.yda.yiyunchain.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddWeiXinAliActivity extends BaseActivity {

    @BindView(R.id.center)
    TextView titleTv;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.nametag)
    TextView nametag;
    @BindView(R.id.passwordtag)
    TextView passwordtag;
    @BindView(R.id.next_tv)
    TextView next_tv;
    @BindView(R.id.accountline)
    View accountline;
    @BindView(R.id.refresh)
    View refresh;
    @BindView(R.id.addtitle)
    TextView addtitle;

    @BindView(R.id.pass_editText)
    EditText pass_editText;

    public static String BINGTAG = "BINGTAG";
    public static String WINXINACCOUNT = "WINXINACCOUNT";
    public static String ALIACCOUNT = "ALIACCOUNT";

    private int addType = 0; //1 微信 2 支付宝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wei_xin_ali);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        refresh.setVisibility(View.GONE);
        String title = "";
        if (intent.hasExtra(BINGTAG)) {
            if (intent.getStringExtra(BINGTAG).equals(WINXINACCOUNT)) {
                title = "绑定微信";
                addType = 1;
            } else if (intent.getStringExtra(BINGTAG).equals(ALIACCOUNT)) {
                title = "绑定支付宝";
                addType = 2;
            }
        }
        titleTv.setText(title);
        init();
    }

    private void init() {
        inittitletag();
        next_tv.setBackground(SelectorFactory.newShapeSelector()
                .setDefaultBgColor(Color.parseColor("#00a1ea"))
                .setPressedBgColor(Color.parseColor("#ffffff"))
                .setCornerRadius(Util.dpToPx(context, 5))
                .create(context));
    }

    //设置标题
    private void inittitletag() {
        CharSequence str1 = "";
        CharSequence str2 = getStr("交易密码");
        if (addType == 1) {
            str1 = getStr("微信账户");
            addtitle.setText("请绑定本人的" + "微信账户" + "，未绑定本人账户会影响你的提现到账。");

        } else if (addType == 2) {
            str1 = getStr("支付宝账户");
            addtitle.setText("请绑定本人的" + "支付宝账户" + "，未绑定本人账户会影响你的提现到账。");
        }

        nametag.setText(str1);
        passwordtag.setText(str2);
    }

    @OnClick({R.id.next_tv, R.id.back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next_tv:
                if (Util.isNull(pass_editText.getText().toString())) {
                    Util.show(context, "请先输入支付密码");
                    return;
                }
                AccountBindUtil.bindingAccount(addType, AddWeiXinAliActivity.this, new BindingCallBack() {
                    @Override
                    public void onSuccess(String id) {
                        add(addType == 1 ? "weixin" : "alipay", id, pass_editText.getText().toString());
                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onCancel(String message) {

                    }
                });
                break;
        }


    }


    private CharSequence getStr(String string) {
        return Util.justifyString(string, 5);
    }

    public void add(final String name, String openid, String password) {


        HashMap<String, String> params = new HashMap<>();
        params.put("oauth_name", name);
        params.put("oauth_access_token", "XXX");
        params.put("oauth_openid", openid);
        params.put("pay_password", password);
        HttpManger.postRequest(context, "/tools/submit_api.ashx?action=user_oauth_Binding", params, "加载中...", new HttpManger.DoRequetCallBack() {

            @Override
            public void onSuccess(String o) {
                Log.e("请求结果", "str" + o);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    Util.show(context, jsonObject.getString("msg"));
                    if (jsonObject.getString("status").equals("1")) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("dealWith", 3);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String o) {

            }
        });


    }
}

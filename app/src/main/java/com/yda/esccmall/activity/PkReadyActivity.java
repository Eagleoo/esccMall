package com.yda.esccmall.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanzhenjie.sofia.Sofia;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.util.DeviceUtils;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.widget.calendar.Article;
import com.yda.esccmall.widget.toast.BamToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class PkReadyActivity extends BaseActivity {

    @BindView(R.id.bowen1)
    ImageView bowen1;
    @BindView(R.id.bowen2)
    ImageView bowen2;
    @BindView(R.id.start)
    TextView start;
    @BindView(R.id.relative_go)
    RelativeLayout relative_go;
    @BindView(R.id.pk_tv)
    TextView pk_tv;
    @BindView(R.id.shimmer_linear)
    ShimmerLayout shimmerLayout;
    @BindView(R.id.back)
    ImageView back;
    int padding=0;
    private List<Article> list_sign;
    String sign="";//本月签到天数
    String username;
    String exp;
    String diamond;
    String signshu;
    String grade;
    String grade_id;
    String winlose;
    String portrait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_ready);

        ButterKnife.bind(this);

        Sofia.with(context)
                .statusBarBackground(Color.parseColor("#26b6f9"))
                .invasionNavigationBar()
                .navigationBarBackground(Color.TRANSPARENT)
                .navigationBarBackgroundAlpha(0);

        init();

        bowen2DoRepeatAnim();

    }

    private void init(){
        start.setEnabled(true);

        padding = (int) DeviceUtils.dpToPixel(context, 20);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(pk_tv, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(pk_tv, "scaleY", 1f, 1.2f);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        final AnimatorSet animatorSetsuofang=new AnimatorSet();
        animatorSetsuofang.setDuration(1000);
        animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();

        relative_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                animatorSetsuofang.cancel();
                animatorSetsuofang.end();
                shimmerLayout.setShimmerColor(Color.BLACK);
                shimmerLayout.startShimmerAnimation();
                pk_tv.setText("PK即将开始");
                start.setText("Loading...");
                start.setTextColor(Color.parseColor("#b1b1b1"));
                start.setTextSize(20);
                getSign();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void bowen2DoRepeatAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bowen2, "translationX", -padding, padding);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setDuration(4000);
//        animator.setStartDelay(1000);
        animator.start();
    }

    private void getInfo(final String sign_me){
        HashMap<String, String> params = new HashMap<>();
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=PKGetUser", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){

                        JSONObject jsonObject=json.getJSONObject("model");
                        username=jsonObject.optString("user_name");
                        exp=jsonObject.optString("exp");
                        diamond=jsonObject.optString("diamond");
                        signshu=jsonObject.optString("signshu");
                        grade=jsonObject.optString("grade");
                        grade_id=jsonObject.optString("group_id");
                        winlose=jsonObject.optString("winorlose");
                        portrait=jsonObject.optString("avater");
                        Util.showIntent(context,PKActivity.class,new String[]{"username","exp","diamond","signshu","grade","grade_id","winlose","portrait","sign"}
                                ,new String[]{username,exp,diamond,signshu,grade,grade_id,winlose,portrait,sign_me});
                        shimmerLayout.stopShimmerAnimation();
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("PK请求结果", "str    " + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }

    //获取自己签到天数
    private void getSign(){
        HashMap<String, String> params = new HashMap<>();
        HttpManger.postRequest(context, false,"/tools/submit_api.ashx?action=GetUserSignList", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("1")){
                        list_sign= Util.getSignArrays("list",t);
                        sign=String.valueOf(list_sign.size());
                        getInfo(sign);
                    }
                    else {
                        BamToast.showText(context, json.optString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("******", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }

}

package com.yda.esccmall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.sofia.Sofia;
import com.yda.esccmall.Bean.Diamond;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.util.DeviceUtils;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.util.Web;
import com.yda.esccmall.water.DiamondTextView;
import com.yda.esccmall.water.WaterContainer;
import com.yda.esccmall.water.WaterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by 张远航 on 2018-12-14
 * describle:仿蚂蚁森林水滴效果
 */
public class StealActivity extends BaseActivity implements WaterView.GetCallBack{
    @BindView(R.id.relative)
    WaterContainer relative;

    @BindView(R.id.linear4)
    LinearLayout linear4;

    @BindView(R.id.portrait)
    ImageView portrait;

    @BindView(R.id.nick_name)
    TextView nick_name;

    @BindView(R.id.username)
    TextView username;

    @BindView(R.id.rel_countdown)
    RelativeLayout rel_countdown;

    int[] location = new int[2];
    private Animation shake;
    private Animation mAlpha;
    private long leftTime=10;
    int posx;
    int posy;
    int count=0;

    private List<Integer> xCanChooseList;
    private List<Integer> yCanChooseList;
    private List<Diamond> list=new ArrayList<>();
    private List<Diamond.DiamondBean> json_list;
    private List<Integer> random_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceUtils.setCustomDensity(this, getApplication());
        setContentView(R.layout.activity_diamondsteal);
        ButterKnife.bind(this);
//判断全面屏手势是否启动
        if (Settings.Global.getInt(context.getContentResolver(),"force_fsg_nav_bar",0)!=0){
            Sofia.with(context)
                    .statusBarBackground(Color.parseColor("#2682CF"))
                    .invasionNavigationBar()
                    .navigationBarBackground(Color.TRANSPARENT)
                    .navigationBarBackgroundAlpha(0);
        }else {
            Sofia.with(context)
                    .statusBarBackground(Color.parseColor("#2682CF"));
        }


        shake = AnimationUtils.loadAnimation(this, R.anim.shaker);
        mAlpha= AnimationUtils.loadAnimation(context, R.anim.alpha);
        posx = (int) DeviceUtils.dpToPixel(context, 50);
        posy = (int) DeviceUtils.dpToPixel(context, 50);
        xCanChooseList	= Arrays.asList(9*posx/2,posx/2, 2*posx, 3* posx, 4*posx, 6* posx, posx/2, 3* posx, 5* posx, posx, 5* posx/2,9*posx/2,11*posx/2,posx/4,7*posx/2, 2*posx,11*posx/2);
        yCanChooseList	= Arrays.asList(posy/3,posy, 2*posy, posy, 2*posy, posy, 3*posy, 3*posy, 3*posy, 9*posy/2,9*posy/2,5*posy,21*posy/5,6*posy,13*posy/2,31*posy/5,31*posy/5);

        Intent intent=getIntent();
        username.setText(intent.getStringExtra("username"));
        nick_name.setText(intent.getStringExtra("nick_name"));
        if (intent.getStringExtra("portrait").equals("")){
            Glide.with(context).load(Web.url+"/images/defaultHeadtp/headtp.jpg").asBitmap().placeholder(R.drawable.portrait).into(portrait);
        }
        else {
            Glide.with(context).load(Web.url+intent.getStringExtra("portrait")).asBitmap().placeholder(R.drawable.portrait).into(portrait);
        }
        getUsreDiamond(getIntent().getStringExtra("id"));

    }

    @OnClick({R.id.linear4})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.linear4:
                Util.showIntent(context, FrgMainActivity.class);
                finish();
                break;
        }
    }

    /**
     * 添加子钻石
     *
     * @param relative
     * @param diamond
     */
    private void addChildView(final Context context, final WaterContainer relative, final Diamond diamond, boolean isNew,int leftTime,boolean isSteal) {

        int width = (int) DeviceUtils.dpToPixel(context, 60);
        int height = (int) DeviceUtils.dpToPixel(context, 60);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        final WaterView waterView = new WaterView(context,StealActivity.this,diamond,isNew,leftTime,isSteal,relative);
        waterView.setPosition(diamond.getIndex(), diamond.getX(), diamond.getY());
        waterView.setLayoutParams(layoutParams);
        if (isNew){
            relative.setChildPosition(diamond.getX(), diamond.getY());
            relative.addView(waterView);
            //waterView.startAnimation(mAlpha);
            count++;
            waterView.setDuration();
            //isCountDown();
        }//
        else {
            relative.setChildPosition(diamond.getX(), diamond.getY());
            relative.addView(waterView);
            count=relative.getChildCount();
        }

    }

    /**
     * 添加偷取效果
     *
     * @param relative
     * @param diamond
     */
    public void addTextView(final Context context, final WaterContainer relative, final Diamond diamond,String value) {

        int width = (int) DeviceUtils.dpToPixel(context, 60);
        int height = (int) DeviceUtils.dpToPixel(context, 60);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        final DiamondTextView waterView = new DiamondTextView(context,diamond,value);
        waterView.setPosition(diamond.getIndex(), diamond.getX()+50, diamond.getY());
        waterView.setLayoutParams(layoutParams);
        relative.setChildPosition(diamond.getX()+50, diamond.getY());
        relative.addView(waterView);
    }

    private void getUsreDiamond(String id){
        HashMap<String, String> params = new HashMap<>();
        list=new ArrayList<>();
        params.put("token", sp.getString("token",""));
        params.put("userid", id);
        HttpManger.postRequest(context, "/tools/submit_api.ashx?action=GetUserDayDiamond", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {
                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");
                    if (status.equals("0")){
                        Util.showIntent(context, LoginActivity.class);
                    }
                    else {
                        String string=json.optString("serveTime");
                        String serveTime=string.substring(6,string.length()-2);
                        json_list=Util.getDiamondArrays("list",t);
                        random_list=new ArrayList<>();
                        Random random = new Random();
                        int s;
                        while (random_list.size()<10){
                            s = random.nextInt(17);//产生10个0-17的随机数
                            if (!random_list.contains(s)){
                                random_list.add(s);
                            }
                        }

                        //用这10个随机数去取对应的坐标
                        for (int i=0;i<json_list.size();i++){
                            Diamond diamond=new Diamond(random_list.get(i),xCanChooseList.get(random_list.get(i)),yCanChooseList.get(random_list.get(i)),json_list.get(i));
                            if (Long.valueOf(Util.cutDateString(diamond.getModel().getEndtime()))<=Long.valueOf(serveTime)){
                                addChildView(context, relative,diamond,false,-1,true);
                            }
                            else {
                                leftTime=(Long.valueOf(Util.cutDateString(diamond.getModel().getEndtime()))-Long.valueOf(serveTime))/1000;
                                addChildView(context, relative,diamond,true,(int)leftTime,true);
                            }

                        }
                    }

//                    shimmerLayout.stopShimmerAnimation();
//                    shimmerLayout.setVisibility(View.GONE);
                    //main_linear.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("请求结果", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }



    @Override
    public void doCallBack(Diamond diamond) {

    }
}

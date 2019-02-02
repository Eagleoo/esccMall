package com.yda.esccmall.water;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.helgi.flappybirdgame.FlappyBirdGame;
import com.yanzhenjie.sofia.Sofia;
import com.yda.esccmall.Bean.Diamond;
import com.yda.esccmall.R;
import com.yda.esccmall.activity.BaseActivity;
import com.yda.esccmall.activity.MainActivity;
import com.yda.esccmall.util.DeviceUtils;
import com.yda.esccmall.util.ListDataSave;
import com.yda.esccmall.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by 张远航 on 2018-12-14
 * describle:仿蚂蚁森林水滴效果
 */
public class WaterActivity extends BaseActivity implements WaterView.GetCallBack{
    @BindView(R.id.relative)
    WaterContainer relative;

    @BindView(R.id.linear1)
    LinearLayout linear1;

    @BindView(R.id.linear4)
    LinearLayout linear4;

    @BindView(R.id.diamond_bag)
    ImageView diamond_bag;

    @BindView(R.id.portrait)
    ImageView portrait;

    @BindView(R.id.countdown)
    TextView countdown;

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
    private ListDataSave listDataSave;
    private String[] titles = new String[]{"可偷取名单","常来偷取我的人"};

    Handler handler = new Handler();

    Runnable update_thread = new Runnable() {
        @Override
        public void run() {
            leftTime--;
            if (leftTime >= 0) {
                //倒计时效果展示
                String formatLongToTimeStr = Util.formatLongToTimeStr(leftTime);
                countdown.setText(formatLongToTimeStr);
                //每一秒执行一次
                handler.postDelayed(this, 1000);
            } else {//倒计时结束
                //处理业务流程
                list=listDataSave.getDataList("diamond_list");
                addChildView(context, relative, list.get(0),true,(int)leftTime);
                list.remove(0);//把第一个删除
                listDataSave.setDataList("diamond_list",list);

            }
        }
    };

    @SuppressLint("HandlerLeak")
    final Handler handlerStop = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    leftTime = 0;
                    handler.removeCallbacks(update_thread);
                    break;
            }
            super.handleMessage(msg);
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceUtils.setCustomDensity(this, getApplication());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        setAnyBarAlpha(0);
        shake = AnimationUtils.loadAnimation(this, R.anim.shaker);
        mAlpha= AnimationUtils.loadAnimation(context, R.anim.alpha);
        posx = (int) DeviceUtils.dpToPixel(this, 70);
        posy = (int) DeviceUtils.dpToPixel(this, 70);
        xCanChooseList	= Arrays.asList(posx, 2 * posx, 3 * posx, posx/2, 4* posx, 3* posx/2, posx/3, 2* posx, 3* posx, 3* posx);
        yCanChooseList	= Arrays.asList(posy, 2 * posy, posy, 2*posy, 2*posy, 3*posy, 4*posy, 4*posy, 4*posy,3*posy);
        //initView();
        listDataSave=new ListDataSave(context,"diamond");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isCountDown();
            }
        }, 1000);


    }

    @OnClick({R.id.linear4,R.id.linear2})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.linear4:
                Util.showIntent(context, FlappyBirdGame.class);
                break;
            case R.id.linear2:
                Util.showIntent(context, MainActivity.class);
                break;
        }
    }


//    private void initView(){
//        for (int i=0;i<10;i++){
//            Diamond diamond=new Diamond(i,xCanChooseList.get(i),yCanChooseList.get(i));
//            addChildView(this, relative, diamond,false,0);
//        }
//    }


    //onCreate()方法界面未初始化完，在onWindowFocusChanged获取view坐标
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        linear1.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leftTime = 0;
        handler.removeCallbacks(update_thread);
    }

    private void setAnyBarAlpha(int alpha) {

        Sofia.with(this)
                .statusBarBackgroundAlpha(alpha);
    }




    /**
     * 添加子水滴
     *
     * @param relative
     * @param diamond
     */
    private void addChildView(final Context context, final WaterContainer relative, final Diamond diamond, boolean isNew,int leftTime) {

        int width = (int) DeviceUtils.dpToPixel(context, 60);
        int height = (int) DeviceUtils.dpToPixel(context, 60);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        final WaterView waterView = new WaterView(context,WaterActivity.this,diamond,isNew,leftTime,isNew,relative);
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

    private void isCountDown(){
        if (count<10){
            Log.e("--------",count+"");
            leftTime=10;
            rel_countdown.setVisibility(View.VISIBLE);
            handler.postDelayed(update_thread,1000);
        }
        else {
            //发送消息，结束倒计时
            Message message = new Message();
            message.what = 1;
            handlerStop.sendMessage(message);
            rel_countdown.setVisibility(View.GONE);
        }
    }


    private void doScaleAnim(){
        //拉伸
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,0.8f,1,0.8f);//默认从（0,0）
        scaleAnimation.setDuration(200);
        diamond_bag.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                diamond_bag.startAnimation(shake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void doCallBack(Diamond diamond) {
        count--;
        //将点击的钻石信息存在本地
        list.add(diamond);
        listDataSave.setDataList("diamond_list",list);
        list=listDataSave.getDataList("diamond_list");
        if (leftTime==0){
            //isCountDown();
        }
        leftTime=12;
        addChildView(context, relative, list.get(0),true,(int)leftTime);
        list.remove(0);//把第一个删除
        listDataSave.setDataList("diamond_list",list);
        doScaleAnim();
    }
}

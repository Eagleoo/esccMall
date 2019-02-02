package com.yda.esccmall.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stx.xhb.xbanner.XBanner;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.sofia.Sofia;
import com.yda.esccmall.R;
import com.yda.esccmall.Service.StepService;
import com.yda.esccmall.util.Constant;
import com.yda.esccmall.util.MyShardPreferen;
import com.yda.esccmall.util.SelectorFactory;
import com.yda.esccmall.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.luncher)
    ImageView luncher;
    @BindView(R.id.skipTv)
    TextView skipTv;
    @BindView(R.id.xbanner)
    XBanner xBanner;
    @BindView(R.id.content)
    RelativeLayout relativeLayout;


    int point = 0;

    /**
     * 开始点击的位置
     */
    private int startX;

    /**
     * 临界值
     */
    private int criticalValue = 200;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        ButterKnife.bind(this);
        //MyReceiver.receivingNotification(context,"测试标题","测试内容哈哈哈哈");
        Req_Permisson();
        setupService();

        Sofia.with(context)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT)
                .statusBarBackgroundAlpha(0)
                .invasionNavigationBar()
                .navigationBarBackground(Color.TRANSPARENT)
                .navigationBarBackgroundAlpha(0);

        Log.e("isFristLaunch", "isFristLaunch" + MyShardPreferen.isFristLaunch(this));

        if (MyShardPreferen.isFristLaunch(this)) {
            xBanner.setVisibility(View.VISIBLE);

            relativeLayout.setVisibility(View.GONE);
            final List<Integer> imgesUrl = new ArrayList<>();
            imgesUrl.add(R.drawable.launcher_bg);
            imgesUrl.add(R.drawable.launcher_bg_night);
            imgesUrl.add(R.drawable.launcher_bg);
            xBanner.setData(imgesUrl, null);//第二个参数为提示文字资源集合
            xBanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {

                    Glide.with(LaunchActivity.this).load(imgesUrl.get(position)).into((ImageView) view);
                }
            });
            xBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Log.e("onPageScrolled", "position:" + position + "positionOffset:" + positionOffset + "positionOffsetPixels:" + positionOffsetPixels);
                    point = position;
                }

                @Override
                public void onPageSelected(int position) {
                    Log.e("onPageSelected", "position:" + position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            ViewPager viewPager = xBanner.getViewPager();
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case ACTION_DOWN:
                            startX = (int) event.getX();
                            Log.e("startX", "startX:" + startX);
                            break;
                        case ACTION_MOVE:
                            Log.e("event.getX()", "startX:" + startX + "event.getX():" + event.getX());
                            if (startX - event.getX() > criticalValue && point == imgesUrl.size() - 1) {
                                Log.e("event.getX()", "true");
                                enter();
                            }
                            break;
                    }

                    return false;
                }
            });

        } else {
            xBanner.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enter();
                }
            }, 3000);
        }


        skipTv.setBackground(SelectorFactory.newShapeSelector()
                .setDefaultBgColor(Color.parseColor("#00ffffff"))
                .setPressedBgColor(Color.parseColor("#489FE4"))
                .setStrokeWidth(Util.dpToPx(context, 0.5f))
                .setCornerRadius(Util.dpToPx(context, 5))
                .setDefaultStrokeColor(Color.parseColor("#ffffff"))
                .create(context));
    }


    @OnClick({R.id.skipTv})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.skipTv:
                enter();
                break;

        }
    }

    int star = 0;

    public synchronized void enter() {


        if (star > 0) {
            return;
        }
        star++;
        luncher.setVisibility(View.VISIBLE);


        Bundle bundle = context.getIntent().getExtras();
        if (!Util.isNull(bundle) && !Util.isNull(bundle.getString("name"))) {
            Intent intent = new Intent(context, FrgMainActivity.class);
            intent.putExtra("bundle", bundle);
            intent.putExtra("show_ver", "show_ver");
            startActivity(intent);
        } else {
            Intent intent = new Intent(context, FrgMainActivity.class);
            intent.putExtra("show_ver", "show_ver");
            startActivity(intent);
        }
        finish();
    }

    public void Req_Permisson(){
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE,Permission.ACCESS_FINE_LOCATION,Permission.ACCESS_COARSE_LOCATION)
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        Util.show(context,"权限拒绝，没有该权限可能无法正常使用某些功能！");
                    }
                }).start();
    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    private Messenger mGetReplyMessenger = new Messenger(new Handler());
    private boolean isBind = false;
    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);

    }
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        Messenger messenger = new Messenger(service);
                        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            };

            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}

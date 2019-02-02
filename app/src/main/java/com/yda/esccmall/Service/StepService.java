package com.yda.esccmall.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yda.esccmall.Bean.StepEntity;
import com.yda.esccmall.DBHelper.DBOpenHelper;
import com.yda.esccmall.HttpUtil.HttpManger;
import com.yda.esccmall.R;
import com.yda.esccmall.activity.StepActivity;
import com.yda.esccmall.util.Constant;
import com.yda.esccmall.util.LocationUtil;
import com.yda.esccmall.util.MyShardPreferen;
import com.yda.esccmall.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class StepService extends Service implements SensorEventListener {

    public static final String TAG = "StepService";
    private NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
    //当前日期
    private static String CURRENT_DATE;
    //当前步数
    private int CURRENT_STEP=0;
    //3秒进行一次存储
    private static int saveDuration = 3000;
    //传感器
    private SensorManager sensorManager;
    //数据库
    private DBOpenHelper db;
    //计步传感器类型 0-counter 1-detector
    private static int stepSensor = -1;
    //广播接收
    private BroadcastReceiver mInfoReceiver;
    //自定义简易计时器
    private TimeCount timeCount;
    private TimeCount1 timeCount1;
    //发送消息，用来和Service之间传递步数
    private Messenger messenger = new Messenger(new MessengerHandler());
    //是否有当天的记录
    private boolean hasRecord;
    //未记录之前的步数
    private int hasStepCount;
    //下次记录之前的步数
    private int previousStepCount;

    private NotificationManager notificationManager;
    private Notification notification = null;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationChannel mChannel;
    private Intent nfIntent;
    private SharedPreferences sp;
    private int total_steps=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                getStepDetector();
            }
        }).start();

        sp=this.getSharedPreferences("loginUser",Context.MODE_PRIVATE);

        notificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        nfIntent=new Intent(this,StepActivity.class);
        startTimeCount();
        startTimeCount1();
        initTodayData();
        Log.e(TAG,"服务创建了");

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        nfShow();
        Log.e(TAG,"服务开始了");
        return START_STICKY;
    }

    /**
     * 初始化广播
     */
    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    // 屏幕灭屏广播
                    case Intent.ACTION_SCREEN_OFF:
                        //屏幕熄灭改为10秒一存储
                        saveDuration = 10000;
                        break;
                    //关机广播，保存好当前数据
                    case Intent.ACTION_SHUTDOWN:
                        saveStepData();
                        break;
                    // 屏幕解锁广播
                    case Intent.ACTION_USER_PRESENT:
                        saveDuration = 3000;
                        break;
                    // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
                    // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
                    // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
                    case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
                        saveStepData();
                        break;
                    //监听日期变化
                    case Intent.ACTION_DATE_CHANGED:
                    case Intent.ACTION_TIME_CHANGED:
                    case Intent.ACTION_TIME_TICK:
//                        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_TIME_TICK);
//                        MyBroadcastReceiver receiver=new MyBroadcastReceiver();
//                        registerReceiver(receiver,intentFilter);
                        saveStepData();
                        isNewDay();
                        break;
                    default:
                        break;
                }
            }
        };
        //注册广播
        registerReceiver(mInfoReceiver, filter);
    }



    /**
     * 初始化当天数据
     */
    private void initTodayData() {
        //获取当前时间
        CURRENT_DATE = TimeUtil.getCurrentDate();
        //获取数据库
        db = new DBOpenHelper(getApplicationContext());
        //获取当天的数据，用于展示
        StepEntity entity = db.getCurDataByDate(CURRENT_DATE);
        //为空则说明还没有该天的数据，有则说明已经开始当天的计步了
        if (entity==null){
            CURRENT_STEP = 0;
        } else {
            CURRENT_STEP = Integer.parseInt(entity.getSteps());
        }
    }

    /**
     * 获取传感器实例
     */
    private void getStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        }
    }


    /**
     * 添加传感器监听
     */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if (countSensor != null) {
            stepSensor = 0;
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (detectorSensor != null) {

            stepSensor = 1;

            sensorManager.registerListener( StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    boolean flag=true;
    @Override
    public void onSensorChanged(SensorEvent event) {
        int tempStep = (int) event.values[0];
        int tempStep1=tempStep;
        if (MyShardPreferen.isFristLaunch(this)) {
            MyShardPreferen.setFristLaunch(this);
            tempStep1=0;
        }
        if (flag){
            int hasStep=tempStep1-sp.getInt("step",0);
            Log.e("没有记录的步数",hasStep+"-------------");
            CURRENT_STEP=CURRENT_STEP+hasStep;
            flag=false;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("step",tempStep);
        editor.commit();

        Log.e("检测的总步数",tempStep+"-------------");
        if (stepSensor == 0) {

            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;//未记录之前的步数
            } else {
                int thisStepCount = tempStep - hasStepCount;
                CURRENT_STEP += (thisStepCount - previousStepCount);
                previousStepCount = thisStepCount;
            }
        }
        if (stepSensor == 1) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
            nfShow();

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @SuppressLint("HandlerLeak")
    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constant.MSG_FROM_CLIENT:
                    try {
                        //这里负责将当前的步数发送出去，可以在界面或者其他地方获取，我这里是在MainActivity中获取来更新界面
                        Messenger messenger = msg.replyTo;
                        Message replyMsg = Message.obtain(null, Constant.MSG_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putInt("steps", CURRENT_STEP);
                        replyMsg.setData(bundle);
                        messenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private int nfShow(){

        // 通知行为（点击后能进入应用界面）
        Drawable drawable = this.getResources().getDrawable(R.drawable.logo1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 110, nfIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setChannelId("id")
                    .setContentTitle("今日步数"+CURRENT_STEP+"步")
                    .setContentText("加油，要记得勤加运动")
                    .setSmallIcon(R.drawable.logo1)
                    .setLargeIcon(((BitmapDrawable) drawable).getBitmap())
                    .setOngoing(false)
                    .setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("今日步数"+CURRENT_STEP+"步")
                    .setContentText("加油，要记得勤加运动")
                    .setSmallIcon(R.drawable.logo1)
                    .setLargeIcon(((BitmapDrawable) drawable).getBitmap())
                    .setOngoing(false)
                    .setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
        notificationManager.notify(110, notification);
        startForeground(110, notification);// 开始前台服务
        return START_STICKY;
    }

    private void saveStepData(){
        CURRENT_DATE = TimeUtil.getCurrentDate();
        db = new DBOpenHelper(getApplicationContext());
        //查询数据库中的数据
        StepEntity entity = db.getCurDataByDate(CURRENT_DATE);

        DecimalFormat df=new DecimalFormat("#.##");

        //为空则说明还没有该天的数据，有则说明已经开始当天的计步了
        if (entity == null) {
            //没有则新建一条数据
            entity = new StepEntity();
            entity.setCurDate(CURRENT_DATE);
            entity.setSteps(String.valueOf(CURRENT_STEP));
            entity.setTotalStepsKm(df.format(CURRENT_STEP*0.0007));
            entity.setTotalStepsKa(df.format(CURRENT_STEP*0.04));
            db.addNewData(entity);
        } else {
            //有则更新当前的数据
            entity.setSteps(String.valueOf(CURRENT_STEP));
            entity.setTotalStepsKm(df.format(CURRENT_STEP*0.0007));
            entity.setTotalStepsKa(df.format(CURRENT_STEP*0.04));
            db.updateCurData(entity);
        }//0点过后，entity没被初始化，step被初始化为0了，所以就执行的update，就直接被更新成0了
        total_steps=Integer.parseInt(entity.getSteps());

    }

    /**
     * 开始倒计时，去存储步数到数据库中
     */
    private void startTimeCount() {
        timeCount = new TimeCount(saveDuration, 1000);
        timeCount.start();
    }
    private void startTimeCount1() {
        timeCount1 = new TimeCount1(300000, 1000);
        timeCount1.start();
    }

    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则每隔三秒存储步数到数据库
            timeCount.cancel();
            saveStepData();

            startTimeCount();
        }
    }
    private class TimeCount1 extends CountDownTimer {
        public TimeCount1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则每隔三秒存储步数到数据库
            timeCount1.cancel();
            updateStep();
            startTimeCount1();
        }
    }

    /**
     * 监听晚上0点变化初始化数据
     */
    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) ||
                !CURRENT_DATE.equals(TimeUtil.getCurrentDate())) {
            initTodayData();
            onDestroy();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"服务销毁了");
    }

    //步数上传
    private void updateStep(){
        String s= LocationUtil.getLngAndLat(StepService.this);
        DecimalFormat df=new DecimalFormat("#.####");
        HashMap<String, String> params = new HashMap<>();
        params.put("steps", String.valueOf(total_steps));
        params.put("distance", String.valueOf(total_steps*0.0007));
        params.put("longitude", s.substring(0,s.indexOf(",")-1));//经度
        params.put("latitude", s.substring(s.indexOf(",")+1,s.length()));//纬度
        Log.e("****上传步数****",total_steps+"");
        params.put("currentTime", TimeUtil.getCurTime());
        HttpManger.postRequest(StepService.this, false,"/tools/submit_api.ashx?action=walkingMatch", params, "请稍后...", new HttpManger.DoRequetCallBack() {
            @Override
            public void onSuccess(String t) {

                JSONObject json = null;
                try {
                    json = new JSONObject(t);
                    String status=json.optString("status");

                    //BamToast.showText(StepService.this,status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("-----请求结果------", "str" + t);
            }

            @Override
            public void onError(String t) {

            }
        });
    }
}

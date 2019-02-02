package com.yda.esccmall.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.yda.esccmall.R;
import com.yda.esccmall.util.Util;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent,int flags, int startId) {
            nfShow();
            return START_STICKY;
        }

        private void nfShow(){
            Notification notification = null;
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            /**
             * 此处设将Service为前台，不然当APP结束以后很容易被GC给干掉，这也就是大多数音乐播放器会在状态栏设置一个
             * 原理大都是相通的
             */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Intent intent = new Intent(this,MainActivity.class);
                SharedPreferences sp= getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("service", 1);
                editor.commit();
                intent.putExtra("service", Util.getNotifyUrl("user_income"));
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationChannel mChannel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(mChannel);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setChannelId("id")
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo1)) // 设置下拉列表中的图标(大图标)
                        .setContentTitle("易云链标题") // 设置下拉列表里的标题
                        .setSmallIcon(R.mipmap.logo1) // 设置状态栏内的小图标
                        .setContentText("易云链测试内容")
                        .setAutoCancel(false)
                        ;// 设置上下文内容

                notificationBuilder.setContentIntent(pendingIntent);

                notificationBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                notification = notificationBuilder.build();
                notification.flags=Notification.FLAG_ONGOING_EVENT|Notification.FLAG_NO_CLEAR;

            }

            notificationManager.notify(110,notification);
            // 参数一：唯一的通知标识；参数二：通知消息。
            startForeground(110, notification);// 开始前台服务
        }



        @Override
        public void onDestroy() {
            super.onDestroy();

        }
}

package com.yda.esccmall.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.yda.esccmall.R;
import com.yda.esccmall.activity.MainActivity;
import com.yda.esccmall.util.Util;
import com.yda.esccmall.util.Web;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    private String notify_url="",notify_url1="";
    private SharedPreferences sp;
    public ReceiverCallBack callBack;
    public interface ReceiverCallBack{
        void doCallBack(String str);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent pushintent = new Intent(context, PushService.class);//启动极光推送的服务
//        context.startService(pushintent);
        System.out.print(TAG + "[MyReceiver] 接收onReceive : ");

        try {
            Bundle bundle = intent.getExtras();

            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                System.out.print(TAG + "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                SharedPreferences sp= context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                Log.e("**********JSON********",json.toString());

                JSONObject array=json.getJSONObject("body").getJSONObject("objurl");
                String token="";
                if (json.getJSONObject("body").has("token")){
                    token=json.getJSONObject("body").optString("token");
                }
                notify_url1=json.getJSONObject("body").optString("type");

                if (notify_url1.equals("")){
                    notify_url=array.optString(sp.getString("alias",""));
                }
                else {
                    if (Util.getNotifyUrl(notify_url1)!=null){
                        notify_url= Util.getNotifyUrl(notify_url1);
                    }
                    else {
                        notify_url=array.optString(sp.getString("alias",""));
                    }

                }

//                if (notify_url1.equals("")){
//                    notify_url1=json.optString("url");
//                }
//                if (notify_url1.equals("")){
//                    notify_url1= Web.url+"/userjiguang.aspx";
//                }

                if (json.optString("IsProduction").equals(Web.getIsProduction())){
                    Log.e("**********TOKEN********",token+"<<<<<<<<<<<"+sp.getString("token",""));
                    if (!token.equals(sp.getString("token",""))&&!token.equals("")){
                        callBack=MainActivity.cbs;
                        callBack.doCallBack("token");
                    }
//                    if (token.equals("")){
//                        receivingNotification(context, bundle);
//                    }
                    receivingNotification(context, bundle);
                }


                Log.e("**********跳转地址1********", Util.getNotifyUrl(notify_url1)+" *******");
                Log.e("**********跳转地址2********",notify_url+" *******");
//                if (json.optString("user").equals(sp.getString("username",""))&&!sp.getString("token","").equals("")){
//
//                }
//                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                System.out.print(TAG + "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                Log.e("****用户名*****",json.optString("user")+"   标题："+ bundle.getString(JPushInterface.EXTRA_TITLE));
                Log.e("****收到的消息*****","********    "+ bundle.getString(JPushInterface.EXTRA_MESSAGE)+"  **********");
                //Toast.makeText(context, "自定义消息:" + bundle.getString(JPushInterface.EXTRA_MESSAGE), Toast.LENGTH_LONG).show();
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                System.out.print(TAG + "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                System.out.print(TAG + "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
                System.out.print(TAG + "[MyReceiver] 用户点击打开了通知");

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                System.out.print(TAG + "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
                System.out.print(TAG + "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                System.out.print(TAG + "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //android.intent.action.SIG_STR

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            System.out.print("JIGUANG-Example key:" + key + "value:" + bundle.getString(key));
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
    }
    private void receivingNotification(Context context, Bundle bundle) {
        int curID;
        sp=context.getSharedPreferences("loginUser",Context.MODE_PRIVATE);
        if (sp.getInt("fid",0)!=0){
            curID=sp.getInt("fid",0);//通知id每次保持不同
        }
        else {
            curID=0;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("fid",curID+1);
        editor.commit();
        NotificationManager notificationManager;
        notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        // 通知行为（点击后能进入应用界面）
        Intent intent = new Intent(context,MainActivity.class);
        //intent.putExtra("notify_url", Util.getNotifyUrl(notify_url));
        if (!notify_url1.equals("")){
            intent.putExtra("notify_url",notify_url1);
        }
        if (!notify_url.equals("")){
            intent.putExtra("notify_url",notify_url);
        }
        if (notify_url1.equals("")&&notify_url.equals("")){
            intent.putExtra("notify_url",Web.url+"/userjiguang.aspx");
        }
        Drawable drawable = context.getResources().getDrawable(R.drawable.logo1);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, curID, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setChannelId("id")
                    .setContentTitle(bundle.getString(JPushInterface.EXTRA_TITLE))
                    .setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE))
                    .setSmallIcon(R.drawable.logo1)
                    .setLargeIcon(((BitmapDrawable) drawable).getBitmap())
                    .setOngoing(false)
                    .setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
            notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(bundle.getString(JPushInterface.EXTRA_TITLE))
                    .setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE))
                    .setSmallIcon(R.drawable.logo1)
                    .setLargeIcon(((BitmapDrawable) drawable).getBitmap())
                    .setOngoing(false)
                    .setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
            notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
        notificationManager.notify(curID, notification);

    }


    public static void receivingNotification(Context context, String str1,String str2) {

        NotificationManager notificationManager;
        notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);

//            notification = new Notification.Builder(context)
//                    .setChannelId("id")
//                    .setContentTitle(bundle.getString(JPushInterface.EXTRA_TITLE))
//                    .setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE))
//                    .setSmallIcon(R.mipmap.logo1)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
//                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
//                    .build();

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setChannelId("id")
                    .setContentTitle(str1)
                    .setContentText(str2)
                    .setSmallIcon(R.drawable.logo1)
                    .setOngoing(false)
                    .setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
            notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(str1)
                    .setContentText(str2)
                    .setSmallIcon(R.drawable.logo1)
                    .setOngoing(false)
                    .setAutoCancel(true);
//            IntentFilter filter_click = new IntentFilter();
//            filter_click.addAction(ONCLICK);
//            context.registerReceiver(receiver_onclick, filter_click);
//            Intent Intent_pre = new Intent(ONCLICK);
//            PendingIntent pendIntent_click = PendingIntent.getBroadcast(context, 0, Intent_pre, 0);
//            notificationBuilder.setContentIntent(pendIntent_click);
            // 通知行为（点击后能进入应用界面）

            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS);
            notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }

        notificationManager.notify(111123, notification);

    }
}

package com.yda.esccmall.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.style.ScaleXSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yda.esccmall.App;
import com.yda.esccmall.Bean.Diamond;
import com.yda.esccmall.Bean.Info;
import com.yda.esccmall.Bean.Rank;
import com.yda.esccmall.Bean.StepRank;
import com.yda.esccmall.Bean.Work;
import com.yda.esccmall.R;
import com.yda.esccmall.widget.calendar.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Consumer;

public class Util {


//    private static List<BaseActivity> activityList = new ArrayList<>();
//
//    public static void addActivity(BaseActivity activity) {
//        activityList.add(activity);
//    }
    private static boolean isLogin = false;

    public static boolean islock = false;//判断图形验证码是否正确

    public static boolean isRecent = false;//判断是否最近键按下（不知道为什么最近键按下切换回来会发出home键广播）


    public static boolean isIsLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        Util.isLogin = isLogin;
    }

    public static void showIntent(final Context activity, final Class c) {
        showIntent(activity, c, null, null);
    }

    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showIntent(final Context activity, final Class c, String[] keys, Serializable[] values) {
        Intent intent = new Intent();
        intent.setClass(activity, c);
        if (null != keys) {
            int i = 0;
            for (String key : keys) {
                intent.putExtra(key, values[i]);
                i++;
            }
        }
        activity.startActivity(intent);
        if (activity instanceof Activity)
            ((Activity) activity).overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
    }

    public static boolean isNull(Object obj) {
        return null == obj || "".equalsIgnoreCase(obj.toString());
    }

    public static void show(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        toast.setGravity(Gravity.TOP, 0, (displayMetrics.widthPixels / 2));
        toast.show();
    }

    /**
     * 获取当前本地apk的版本
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取当前屏幕大小和密度
     */
    public static WHD getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        int sWidth = displayMetrics.widthPixels;
        int sHeight = displayMetrics.heightPixels;
        int dpi = displayMetrics.densityDpi;
        return new WHD(sHeight, sWidth, dpi);
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static String formatTitleStr(String title) {

        title = title.replace(App.getContext().getResources().getString(R.string.app_name), "").replaceAll("\\s*", "")
                .replace("-", "").replace("－", "");
        return title;
    }

    /**
     * 将给定的字符串给定的长度两端对齐
     *
     * @param str  待对齐字符串
     * @param size 汉字个数，eg:size=5，则将str在5个汉字的长度里两端对齐
     * @Return
     */
    public static SpannableStringBuilder justifyString(String str, int size) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(str)) {
            return spannableStringBuilder;
        }
        char[] chars = str.toCharArray();
        if (chars.length >= size || chars.length == 1) {
            return spannableStringBuilder.append(str);
        }
        int l = chars.length;
        float scale = (float) (size - l) / (l - 1);
        for (int i = 0; i < l; i++) {
            spannableStringBuilder.append(chars[i]);
            if (i != l - 1) {
                SpannableString s = new SpannableString("　");//全角空格
                s.setSpan(new ScaleXSpan(scale), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(s);
            }
        }
        return spannableStringBuilder;
    }


    private String compressPath;


    public static double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        Double temp = Double.valueOf(yearString);
        return temp;
    }

    /**
     * 程序是否在前端运行,通过枚举运行的app实现。防止重复超时检测多次,保证只有一个activity进入超时检测
     * 当用户按home键时，程序进入后端运行，此时会返回false，其他情况引起activity的stop函数的调用，会返回true
     *
     * @return
     */

    public static boolean isOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public interface PermissionsCallBack {
        void success();

        void failure();

    }



    @SuppressLint("CheckResult")
    public static void checkpermissions(final String[] requestPermissionstr, Activity mActivity, final PermissionsCallBack permissionsCallBack) {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(requestPermissionstr).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    permissionsCallBack.success();
                } else {
                    permissionsCallBack.failure();
                }
            }
        });
    }

    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    public static List getDiamondArrays(String key, String jsonString) {
        List<Diamond.DiamondBean> list = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Diamond.DiamondBean diamond = gson.fromJson(jsonObject1.toString(), Diamond.DiamondBean.class);
                list.add(diamond);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List getWorkArrays(String key, String jsonString) {
        List<Work> list = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Work diamond = gson.fromJson(jsonObject1.toString(), Work.class);
                list.add(diamond);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List getStepRankArrays(String key, String jsonString) {
        List<StepRank> list = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                StepRank diamond = gson.fromJson(jsonObject1.toString(), StepRank.class);
                list.add(diamond);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List getRankArrays(String key, String jsonString) {
        List<Rank> list = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Rank diamond = gson.fromJson(jsonObject1.toString(), Rank.class);
                list.add(diamond);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List getSignArrays(String key, String jsonString) {
        List<Article> list = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Article diamond = gson.fromJson(jsonObject1.toString(), Article.class);
                list.add(diamond);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List getInfoArrays(String key, String jsonString) {
        List<Info> list = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Info diamond = gson.fromJson(jsonObject1.toString(), Info.class);
                list.add(diamond);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 判断网络情况
     *
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    /**
     * 用来判断是否开启通知权限
     * */
    public static boolean isNotificationEnabled(Context context){
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            boolean isOpened = manager.areNotificationsEnabled();
            return isOpened;
        } else if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOps =
                    (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE,
                        Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg)
                        == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                    | InvocationTargetException | IllegalAccessException | RuntimeException e) {
                return true;
            }
        } else {
            return true;
        }
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B ";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public static String getTotalCacheSize(Context context) throws Exception {

        //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
        long cacheSize = getFolderSize(context.getCacheDir());
        //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public static void clearAllCache(Context context) {
        //deleteDir(context.getCacheDir());
        Log.e("1删除","  "+deleteDir(context.getCacheDir()));
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //deleteDir(context.getExternalCacheDir());
            Log.e("2删除","  "+ deleteDir(context.getExternalCacheDir()));
            //下面两句清理webview网页缓存.但是每次执行都报false,我用的是魅蓝5.1的系统，后来发现且/data/data/应用package目录下找不到database文///件夹 不知道是不是个别手机的问题，
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        }
    }

    private static boolean deleteDir(File dir) {
        Log.e("************","   "+dir.getName());

        //清楚缓存不能删除手势密码的数据
        if (dir != null && dir.isDirectory()) {

//            else {
//                String[] children = dir.list();
//                for (int i = 0; i < children.length; i++) {
//                    boolean success = deleteDir(new File(dir, children[i]));
//                    Log.e("是否删除成功","   "+success);
//                    if (!success) {
//                        return false;
//                    }
//                }
//            }
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                if (!children[i].equals("")){
                    boolean success = deleteDir(new File(dir, children[i]));
                    Log.e("是否删除成功","   "+success);
                    if (!success) {
                        return false;
                    }
                }

            }

        }
        return dir.delete();
    }

    public static String phoneFormat(String num) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(num) && num.length() > 6) {
            for (int i = 0; i < num.length(); i++) {
                char c = num.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String stringForamat(String str) {
        String format = str;
        char[] stringArr = str.toCharArray();
        for (int i = 0; i < stringArr.length; i++) {
            if ('.' == (stringArr[i])) {
                format.replace(String.valueOf(stringArr[i]), "");
            }
        }
        return format;
    }

    public static String getString(String s, String s1)//s是需要删除某个子串的字符串s1是需要删除的子串
    {
        int postion = s.indexOf(s1);
        int length = s1.length();
        int Length = s.length();
        String newString = s.substring(0, postion) + s.substring(postion + length, Length);
        return newString;//返回已经删除好的字符串

    }

    public static String getNotifyUrl(String s) {
        HashMap<String , String> map = new HashMap<String , String>();
        map.put("pay_usd",Web.url+"/mobile/useramount.aspx?action=yw_list2");
        map.put("income_usd",Web.url+"/mobile/userbussions.aspx?action=yw_list2");
        map.put("user_pay",Web.url+"/mobile/useramount.aspx?action=yw_list2");
        map.put("user_income",Web.url+"/mobile/useramount.aspx?action=yw_list2");
        map.put("shop_pay",Web.url+"/aspx/mobile/userC2CPage.aspx?action=shopc2c_orders");
        map.put("shop_income",Web.url+"/aspx/mobile/userC2CPage.aspx?action=shopc2c_orders");
        map.put("base_usdt_in_msg",Web.url+"/userusdt.aspx?action=yw_list2");
        map.put("base_usdt_out_msg",Web.url+"/userusdt.aspx?action=yw_list2");
        return map.get(s);
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = get02String(hour)+":"+get02String(minute)+":"+get02String(second);
        return strtime;
    }

    private static String get02String(int str){
        return String.format("%02d", str);
    }

    public static String format2String(String s){
        float f= Float.parseFloat(s);
        DecimalFormat decimalFormat=new DecimalFormat("0.0000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(f);//format 返回的是字符串
        return p;
    }

    private static final int MIN_DELAY_TIME= 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;
    private static int index1=-1;

    public static boolean isFastClick(Diamond diamond) {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        int index2=diamond.getIndex();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        else {
            Log.e("*******",index1+"   "+index2);
            if (index1==index2){//一直点击一个球移动时会出bug，这里限制连续点击，也防止模拟点击外挂
                flag = true;
            }
            else {
                flag = false;
            }
        }
        lastClickTime = currentClickTime;
        index1=index2;
        return flag;
    }

    public static String cutDateString(String s){
        return s.substring(6,s.length()-2);
    }

    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
// 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
// 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }

    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime()+"";
    }

    public static void clickView(View view, float x, float y)
    {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(
                downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        downTime+=10;
        final MotionEvent upEvent = MotionEvent.obtain(
                downTime, downTime, MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

}
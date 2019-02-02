package com.yda.esccmall.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.yda.esccmall.R;
import com.yda.esccmall.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/10/31.
 */

public class UpDataVersionDialog extends Dialog {
    private Context context;
    private String message;
    private String down_url;
    private TextView uptext, tv1, button_title,update_tittle;
    private ImageView close;
    //进度条
    private CircleProgress mTasksView;

    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    private int progress;
    private boolean cancelUpdate = false;

    public UpDataVersionDialog(@NonNull Context context, String message, String url) {
        super(context, R.style.reddialog);
        this.context = context;
        this.message = message;
        this.down_url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.updataversion_layout);
        uptext = findViewById(R.id.uptext);
        close = findViewById(R.id.close);
        uptext.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv1 = findViewById(R.id.tv1);
        button_title = findViewById(R.id.button_title);
        update_tittle = findViewById(R.id.update_tittle);
        mTasksView = (CircleProgress) findViewById(R.id.circle_progress);
        //添加下划线
        tv1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        Log.e("------", message.trim());
        uptext.setText(message);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cancelUpdate){
                    cancelUpdate=false;
                    cancel();
                    dismiss();
                    Util.show(context,"更新已取消");
                }
                else {
                    cancel();
                    dismiss();
                }


            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelUpdate){
                    cancelUpdate=false;
                    cancel();
                    dismiss();
                    Util.show(context,"下载已取消");
                }
                else {
                    cancel();
                    dismiss();
                }

            }
        });
        button_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(url);
//                intent.setData(content_url);
//                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//                context.startActivity(intent);
//                update();


                cancelUpdate=true;
                mTasksView.setVisibility(View.VISIBLE);
                button_title.setVisibility(View.GONE);
                showDownloadDialog();

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD:
                    update_tittle.setText("正在更新...");
                    tv1.setText("取消更新");
                    mTasksView.setFinishedColor(Color.parseColor("#d50f09"));
                    mTasksView.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    installApk();
                    cancel();
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 下载等待窗口
     */
    public void showDownloadDialog() {
        downloadApk();
    }

    /**
     * 开启下载线程
     */
    private void downloadApk() {
        new downloadApkThread().start();
    }

    /**
     * 下载程序
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {

                    URL url = new URL(down_url);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "escc");

                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }

                        fos.write(buf, 0, numread);
                    } while (cancelUpdate);
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 安装apk
     */
    private void installApk() {

        File apkfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "escc");
        if (!apkfile.exists()) {
            return;
        }

        if (Build.VERSION.SDK_INT>=24){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.yda.yiyunchain.fileprovider", apkfile);
            i.setDataAndType(apkUri,
                    "application/vnd.android.package-archive");
            context.startActivity(i);
        }
        //7.0以下兼容
        else {
            try {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                        "application/vnd.android.package-archive");
                context.startActivity(i);
            }catch (Exception e){
                Log.e("sssssssssssssssss",e.getMessage());
            }

        }
    }
}

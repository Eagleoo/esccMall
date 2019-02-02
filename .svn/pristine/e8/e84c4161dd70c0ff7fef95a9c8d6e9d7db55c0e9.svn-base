package com.yda.yiyunchain.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yda.yiyunchain.ScreenAdaptation;
import com.yda.yiyunchain.util.ActivityCollector;

public class BaseActivity extends AppCompatActivity {
    public Activity context;

    public boolean isWidth() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = this;
        Log.e("isWidth", "isWidth()" + isWidth());
        ScreenAdaptation.setCustomDesity(this, getApplication(), isWidth());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

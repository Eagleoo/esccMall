package com.yda.yiyunchain.util;

import android.content.Context;

public class MyShardPreferen {
    private static String ISFRIST = "isfrist";


    public static boolean isFristLaunch(Context context) {
        return (boolean) SharedPreferencesHelper.init(context).getData(ISFRIST, true);
    }

    public static void setFristLaunch(Context context) {
        SharedPreferencesHelper.init(context).saveData(ISFRIST, false);
    }
}

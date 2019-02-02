package com.yda.yiyunchain.PopWindowHelp;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MyPopWindowManage {
    public volatile static HashMap<Activity, List<BasePopWindow>> showMyPopWindowHashMap = new HashMap<>();

    public static void showLoadingDialog(Activity activity, BasePopWindow popWindow) {

        synchronized (MyPopWindowManage.class) {
            List<BasePopWindow> list = showMyPopWindowHashMap.get(activity);
            if (list == null) {
                list = new ArrayList<>();
            }
//            Log.e("showMyPopWindowHashMap", "list" + (list == null));
            list.add(popWindow);
            showMyPopWindowHashMap.put(activity, list);
        }

    }

    public static void dismissDialog(Activity activity) {
        if (showMyPopWindowHashMap.get(activity) != null) {
            List<BasePopWindow> list = showMyPopWindowHashMap.get(activity);
            for (BasePopWindow showMyPopWindow : list
                    ) {
                if (showMyPopWindow != null) {
                    showMyPopWindow.dismiss();
                }
            }
        }
    }


}

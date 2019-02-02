package com.yda.yiyunchain;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

public class ScreenAdaptation {
    public static float sNoncompatDesity;
    public static float sNoncompatScaledDesity;

    public static void setCustomDesity(@NonNull Activity activity, @NonNull final Application application, boolean isWidth) {
        final DisplayMetrics appdisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDesity == 0) {
            sNoncompatDesity = appdisplayMetrics.density;
            sNoncompatScaledDesity = appdisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration configuration) {
                    if (configuration != null && configuration.fontScale > 0) {
                        sNoncompatScaledDesity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        final float targetDesity;
        if (isWidth) {
            targetDesity = (float) appdisplayMetrics.widthPixels / 360;//375 设计图的宽度dp 根据宽度适配
        } else {
            targetDesity = (float) appdisplayMetrics.heightPixels / 640;//667 设计图的高度dp 根据高度适配
        }
        final float targetScaleDesity = targetDesity * (sNoncompatScaledDesity / sNoncompatDesity);
        final int targetDesityDpi = (int) (160 * targetDesity);

        appdisplayMetrics.density = targetDesity;
        appdisplayMetrics.scaledDensity = targetScaleDesity;
        appdisplayMetrics.densityDpi = targetDesityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDesity;
        activityDisplayMetrics.scaledDensity = targetScaleDesity;
        activityDisplayMetrics.densityDpi = targetDesityDpi;
    }
}


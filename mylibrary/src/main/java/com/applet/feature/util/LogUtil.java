package com.applet.feature.util;

import android.util.Log;

import com.applet.feature.LibConstant;

public class LogUtil {

    private static final String TAG = "App_Lib";

    public static void t(String message) {
        if (!LibConstant.isDev()) return;
        Log.e(TAG, "T: " + message);
    }

    public static void e(String message) {
        Log.e(TAG, "E: " + message);
    }

    public static void e(String message, Throwable e) {
        Log.e(TAG, "E: " + message, e);
    }

    public static void d(String message) {
        Log.e(TAG, "D: " + message);
    }
}

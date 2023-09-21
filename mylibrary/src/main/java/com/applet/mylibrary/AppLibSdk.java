package com.applet.mylibrary;

import android.content.Context;

import com.applet.feature.AppletManager;

public class AppLibSdk {

    private static final String TAG = "AppLibSdk";

    public AppLibSdk() {
    }

    private static class AppLibSdkHolder {
        private static final AppLibSdk sInstance = new AppLibSdk();
    }

    public static AppLibSdk getInstance() {
        return AppLibSdkHolder.sInstance;
    }

    public void initialize(Context context) {
        AppletManager.getInstance().initialize(context);
    }

    public void openApplet(Context context, String appId) {
        AppletManager.getInstance().openApplet(context, appId);
    }
}

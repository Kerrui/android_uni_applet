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

    public void initEngine(Context context, OnAppLibInitializeListener onAppLibInitializeListener) {
        AppletManager.getInstance().initialize(context, onAppLibInitializeListener);
    }

    public void openKFApp(Context context, String faceUrl, String uid, boolean hasAgora) {
        AppletManager.getInstance().openKFApp(context, faceUrl, uid, hasAgora, false);
    }

    public void openKFApp(Context context, String faceUrl, String uid, boolean hasAgora, boolean openPerfect) {
        AppletManager.getInstance().openKFApp(context, faceUrl, uid, hasAgora, openPerfect);
    }
}

package com.applet.feature;

import android.content.Context;

import com.applet.feature.moudle.APPLetModule;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

/**
 * Created by Alien-super on 2023/9/21.
 * Contact information WeChat: Alien_super
 */
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
        try {
            WXSDKEngine.registerModule("APPLetModule", APPLetModule.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AppletManager.getInstance().initialize(context, onAppLibInitializeListener);
    }

    public void openKFApp(Context context, String faceUrl, String uid, boolean hasAgora) {
        AppletManager.getInstance().openKFApp(context, faceUrl, uid, hasAgora);
    }
}

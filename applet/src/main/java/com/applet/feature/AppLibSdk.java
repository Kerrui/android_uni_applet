package com.applet.feature;

import android.content.Context;

import com.applet.feature.module.APPLetModule;
import com.taobao.weex.WXSDKEngine;

/**
 * Created by Alien-super on 2023/9/21.
 * Contact information WeChat: Alien_super
 */
public class AppLibSdk {

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

}

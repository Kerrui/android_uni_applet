package com.applet.uni_applet;


import android.media.tv.interactive.AppLinkInfo;

import com.and.uniplugin.PluginInit;
import com.and.uniplugin.feature.util.LogUtil;
import com.applet.feature.AppLibSdk;
import com.applet.feature.LibApp;
import com.applet.feature.OnAppLibInitializeListener;

import io.dcloud.application.DCloudApplication;

public class MyApp extends DCloudApplication {

    private static final String TAG = "MyApp";

    public static int a = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        PluginInit pluginInit = new PluginInit.Builder(this)
                .build();
        pluginInit.init();
        AppLibSdk.getInstance().initEngine(this, new OnAppLibInitializeListener() {
            @Override
            public void onInitFinished(boolean success) {
                LogUtil.t("app lib applet init finish " + success);
            }
        });

    }
}

package com.applet.uni_applet;


import com.applet.feature.AppLibSdk;
import com.applet.feature.OnAppLibInitializeListener;
import com.hi.chat.uniplugin.ApplicationListener;
import com.hi.chat.uniplugin.PluginInit;
import com.hi.chat.uniplugin.feature.util.LogUtil;

import io.dcloud.application.DCloudApplication;

public class MyApp extends DCloudApplication {

    private static final String TAG = "MyApp";


    @Override
    public void onCreate() {
        super.onCreate();

        new PluginInit.Builder(this).build().init();
        AppLibSdk.getInstance().initEngine(this, new OnAppLibInitializeListener() {
            @Override
            public void onInitFinished(boolean success) {
                LogUtil.t("app lib applet init finish " + success);
            }
        });


        registerActivityLifecycleCallbacks(new ApplicationListener());
    }
}

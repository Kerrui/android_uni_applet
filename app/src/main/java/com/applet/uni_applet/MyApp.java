package com.applet.uni_applet;

import com.applet.feature.util.LogUtil;
import com.applet.mylibrary.AppLibSdk;
import com.applet.mylibrary.OnAppLibInitializeListener;

import io.dcloud.application.DCloudApplication;

public class MyApp extends DCloudApplication {

    private static final String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();

        AppLibSdk.getInstance().initEngine(this, new OnAppLibInitializeListener() {
            @Override
            public void onInitFinished(boolean success) {
                LogUtil.t("app lib applet init finish " + success);
            }
        });

    }
}

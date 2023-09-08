package com.example.uni_applet;

import com.example.mylibrary.AppLibSdk;

import io.dcloud.application.DCloudApplication;

public class MyApp extends DCloudApplication {

    private static final String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();

        AppLibSdk.getInstance().initialize(this);
    }
}

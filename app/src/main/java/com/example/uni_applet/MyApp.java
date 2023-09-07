package com.example.uni_applet;

import android.app.Application;

import com.example.mylibrary.AppLibSdk;

public class MyApp extends Application {

    private static final String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();

        AppLibSdk.getInstance().initialize(this);
    }
}

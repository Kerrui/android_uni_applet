package com.applet.module.action;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.common.UniModule;

public class AppletModule extends UniModule {

    private static final String TAG = "AppletModule";

    public AppletModule() {
        Log.e(TAG, "AppletModule: '------> init execute ");

    }

    @UniJSMethod(uiThread = false)
    public int getTestNumber(JSONObject params) {
        Log.e(TAG, "getTestNumber: '------> params " + params);
        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(false)
                //.setMenuDefFontSize("16px")
                //.setMenuDefFontColor("#ff00ff")
                //.setMenuDefFontWeight("normal")
                //.setMenuActionSheetItems(sheetItems)
                .setEnableBackground(false)//开启后台运行
                .build();
        DCUniMPSDK.getInstance().initialize(mWXSDKInstance.getContext(), config, new IDCUniMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean b) {
                Log.e(TAG, "onInitFinished: " + (b ? "success" : "fail"));

                boolean initialize = DCUniMPSDK.getInstance().isInitialize();
                Log.e(TAG, "getTestNumber: '--------> initialize initialize  " + initialize );
                try {
                    //DCUniMPSDK.getInstance().openUniMP(mUniSDKInstance.getContext(), "__UNI__2A047DF");

                    //AppLibSdk.getInstance().openApplet();

                    DCUniMPSDK.getInstance().openUniMP(mWXSDKInstance.getContext(), "__UNI__2A047DF");



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return 1000;
    }
}

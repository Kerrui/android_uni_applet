package com.applet.mylibrary;

import android.content.Context;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;

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

    // todo delete
    public void openCustomerService(Context context) {
        try {
            IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(context, LibConstant.APP_ID_CUSTOMER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

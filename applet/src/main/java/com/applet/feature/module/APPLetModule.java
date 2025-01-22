package com.applet.feature.module;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSONObject;
import com.applet.feature.bean.MPStack;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.common.UniModule;

public class APPLetModule extends UniModule {

    @UniJSMethod(uiThread = false)
    public String getDeviceModel() {
        return Build.MODEL;
    }

    @UniJSMethod(uiThread = false)
    public String bundleId() {
        return mUniSDKInstance.getContext().getPackageName();
    }


    @UniJSMethod(uiThread = false)
    public void setDefaultApplet(JSONObject jsonObject) {
        APPLetUtil.setDefaultApplet(jsonObject);
    }

    @UniJSMethod(uiThread = false)
    public void setAppletInfo(JSONObject jsonObject) {
        APPLetUtil.setAppletInfo(jsonObject);
    }


    @UniJSMethod(uiThread = false)
    public boolean putPackageData(JSONObject params) {
        return APPLetUtil.putPackageData(params);
    }

    public static void initCallback(Context context) {
        DCUniMPSDK.getInstance().setUniMPOnCloseCallBack(s -> MPStack.getInstance().remove(s));

        DCUniMPSDK.getInstance().setOnUniMPEventCallBack((appid, event, data, callback) -> {
            switch (event) {
                case "isInstall": {
                    boolean install = APPLetUtil.isInstall((String) data);
                    callback.invoke(install);
                    break;
                }
                case "open": {
                    APPLetUtil.open(context, (JSONObject) data, callback);
                    break;
                }
            }
        });
    }

    @UniJSMethod(uiThread = true)
    public int uniBasePlatform() {
        return 0;
    }


    @UniJSMethod(uiThread = false)
    public JSONObject  getCustomerData(JSONObject params) {
        return APPLetUtil.getCustomerData(params);
    }
}

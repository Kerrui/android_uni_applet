package com.applet.module;

import com.alibaba.fastjson.JSONObject;
import com.applet.mylibrary.AppletManager;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class AppletModule extends UniModule {

    @UniJSMethod(uiThread = false)
    public String bundleId() {
        return mUniSDKInstance.getContext().getPackageName();
    }

    @UniJSMethod(uiThread = true)
    public void close(JSONObject params, UniJSCallback callback) {
        String appId = params.containsKey("appId") ? params.getString("appId") : "";
        AppletManager.getInstance().closeApplet(appId);
    }
}

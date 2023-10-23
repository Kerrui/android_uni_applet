package com.applet.module;

import android.app.Activity;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.applet.tool.PermissionManager;
import com.applet.tool.ToolUtils;
import com.applet.tool.location.LocationManager;
import com.applet.tool.location.LocationObj;
import com.applet.tool.location.OnLocationListener;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class ToolModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void getCurrentLocation(JSONObject params, UniJSCallback callback) {
        int priority = params.containsKey("priority") ? params.getInteger("priority") : -1;
        LocationManager.getInstance().getCurrentLocation(mUniSDKInstance.getContext(), priority, new OnLocationListener() {
            @Override
            public void onLocation(LocationObj locationObj) {
                callback.invoke(locationObj);
            }
        });
    }

    @UniJSMethod(uiThread = false)
    public String deviceID() {
        return ToolUtils.getUniqueID(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = false)
    public boolean vpnConnected(JSONObject params) {
        return ToolUtils.isDeviceInVPN();
    }

    @UniJSMethod(uiThread = false)
    public boolean proxyOpened(JSONObject params) {
        return ToolUtils.isWifiProxy(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = false)
    public String carrierInfo(JSONObject params) {
        return ToolUtils.getOperatorStr(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = true)
    public void checkVirtualApp(JSONObject params, UniJSCallback callback) {
        callback.invoke(ToolUtils.virtualInfo(mUniSDKInstance.getContext(), params));
    }

    @UniJSMethod(uiThread = true)
    public void screenshotStop(JSONObject params, UniJSCallback callback) {
        int isStop = params.getInteger("is_stop");
        Activity activity = (Activity) this.mUniSDKInstance.getContext();
        if (isStop == 1) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @UniJSMethod(uiThread = true)
    public void permissionOpenSetting(JSONObject params, UniJSCallback callback) {
        PermissionManager.openSetting(mUniSDKInstance.getContext(), params);
    }

    @UniJSMethod(uiThread = false)
    public boolean permissionStatusSync(JSONObject params, UniJSCallback callback) {
        return PermissionManager.getStatus(mUniSDKInstance.getContext(), params);
    }

    @UniJSMethod(uiThread = true)
    public void permissionRequest(JSONObject params, UniJSCallback callback) {
        PermissionManager.request(mUniSDKInstance.getContext(), params, callback);
    }

    @UniJSMethod(uiThread = false)
    public JSONObject permissionCheckAll(JSONObject params) {
        return PermissionManager.checkAll(mUniSDKInstance.getContext());
    }
}

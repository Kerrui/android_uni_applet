package com.applet.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.applet.feature.LibConstant;
import com.applet.feature.util.LogUtil;
import com.applet.tool.PermissionManager;
import com.applet.tool.ToolUtils;
import com.applet.tool.location.LocationManager;
import com.applet.tool.location.LocationObj;
import com.applet.tool.location.OnLocationListener;
import com.tencent.mmkv.MMKV;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class ToolModule extends UniModule {
    public static final String INTENT_ACTION_APPLICATION = "com.lib.const.APPLICATION";

    private boolean isRegisterApplicationBroadcastReceiver = false;

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

    @UniJSMethod(uiThread = true)
    public void openOverlays(JSONObject params, UniJSCallback callback) {
        mUniSDKInstance.getContext().startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mUniSDKInstance.getContext().getPackageName())));
    }

    @UniJSMethod(uiThread = false)
    public boolean checkLocationServiceOpen(JSONObject params) {
        return PermissionManager.isLocationServiceOpen(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = false)
    public void openLocationServiceSetting(JSONObject params) {
        PermissionManager.openLocationServiceSetting(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = false)
    public boolean checkDrawOverlays(JSONObject params) {
        return PermissionManager.hasDrawOverlays(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = false)
    public JSONObject getApplnfo() {
        JSONObject result = new JSONObject();
        MMKV kv = MMKV.mmkvWithID(LibConstant.SP_PROCESS, MMKV.MULTI_PROCESS_MODE);
        String name = kv.decodeString(LibConstant.SP_APP_NAME, "");
        String logo = kv.decodeString(LibConstant.SP_APP_LOGO, "");
        result.put("name", name);
        result.put("logo", logo);
        return result;
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @UniJSMethod(uiThread = true)
    public void installAppListener(JSONObject params, UniJSCallback callback) {
        if (!isRegisterApplicationBroadcastReceiver) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ToolModule.INTENT_ACTION_APPLICATION);
            mUniSDKInstance.getContext().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        int type = intent.getIntExtra("type", 0);
                        LogUtil.t("ApplicationBroadcastReceiver type = " + type);
                        if (type == 0) return;
                        JSONObject actionObj = new JSONObject();
                        actionObj.put("type", type);
                        callback.invokeAndKeepAlive(actionObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, intentFilter);
            isRegisterApplicationBroadcastReceiver = true;
        }
    }
}

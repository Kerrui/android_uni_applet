package com.applet.feature.module;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.applet.feature.AppletManager;
import com.applet.feature.CSplash;
import com.applet.feature.LibConstant;
import com.applet.feature.UniManager;
import com.applet.feature.bean.MPStack;
import com.hi.chat.uniplugin_log.mmkv.MMKVUtil;

import java.io.File;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.common.UniModule;
import io.dcloud.feature.unimp.DCUniMPJSCallback;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;

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
        String appid = jsonObject.getString("appid");
        JSONObject info = jsonObject.getJSONObject("info");
        JSONObject wgtInfo = new JSONObject();
        wgtInfo.put("appid", appid);

        if (info != null) {
            wgtInfo.put("url", info.getString("url"));
            wgtInfo.put("wgt_version", info.getString("version"));
        }
        MMKVUtil.getInstance().put(LibConstant.SP_WGT_APPLET, JSON.toJSONString(wgtInfo));

    }

    @UniJSMethod(uiThread = false)
    public void setAppletInfo(JSONObject jsonObject) {
        String appid = jsonObject.getString("appid");
        JSONObject info = jsonObject.getJSONObject("info");
        MMKVUtil.getInstance().saveJSONObject(appid, info);
    }


    @Deprecated
    @UniJSMethod(uiThread = true)
    public void close(JSONObject jsonObject) {
        String appid = jsonObject.getString("appid");
        IUniMP uniMP;
        if (TextUtils.isEmpty(appid)) {
            uniMP = MPStack.getInstance().getCurrentUniMP();
        } else {
            uniMP = MPStack.getInstance().getUniMP(appid);
        }
        if (uniMP != null && uniMP.isRunning()) {
            uniMP.closeUniMP();
        }
    }


    @UniJSMethod(uiThread = false)
    public boolean putPackageData(JSONObject params) {
        if (!params.containsKey("data") || !params.containsKey("name")) return false;
        String aName = params.getString("name");
        JSONObject data = params.getJSONObject("data");

//        SharedPreferences sp = mUniSDKInstance.getContext().getSharedPreferences("applet", Context.MODE_PRIVATE);
//        sp.edit().putString("aName", aName).apply();
//        sp.edit().putString(aName, JSON.toJSONString(data)).apply();

        MMKVUtil.getInstance().put("aName", aName);
        MMKVUtil.getInstance().put(aName, JSON.toJSONString(data));

        return true;
    }

    @UniJSMethod(uiThread = false)
    public static boolean isInstall(String appid) {
        return DCUniMPSDK.getInstance().isExistsApp(appid);
    }

    public static void open(Context context, JSONObject jsonObject, DCUniMPJSCallback callback) {
        JSONObject ret = new JSONObject();
        String path = jsonObject.getString("path");
        File wgt = new File(path);
        if (!wgt.exists() || !wgt.isFile()) {
            ret.put("succeed", false);
            ret.put("error", "file not exists");
            callback.invoke(ret);
            return;
        }
        String appid = jsonObject.getString("appid");
        UniManager.releaseWgtToRunPath(path, appid, new UniManager.IOnWgtReleaseListener() {
            @Override
            public void onSuccess() {
                UniMPOpenConfiguration configuration = new UniMPOpenConfiguration();
                configuration.splashClass = CSplash.class;
                JSONObject extraData = jsonObject.getJSONObject("extraData");
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(extraData.toJSONString());
                    configuration.extraData = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                IUniMP uniMP = AppletManager.openUniMP(context, appid, configuration);
                if (uniMP == null) {
                    ret.put("succeed", false);
                    ret.put("error", "open failed");
                    callback.invoke(ret);
                } else {
                    ret.put("succeed", true);
                    callback.invoke(ret);
                }
            }

            @Override
            public void onFailed(String message) {
                ret.put("succeed", false);
                ret.put("error", message);
                callback.invoke(ret);
            }
        });
    }

    public static void initCallback(Context context) {
        DCUniMPSDK.getInstance().setUniMPOnCloseCallBack(s -> MPStack.getInstance().remove(s));

        DCUniMPSDK.getInstance().setOnUniMPEventCallBack((appid, event, data, callback) -> {
            switch (event) {
                case "isInstall": {
                    boolean install = APPLetModule.isInstall((String) data);
                    callback.invoke(String.valueOf(install));
                    break;
                }
                case "open": {
                    APPLetModule.open(context, (JSONObject) data, callback);
                    break;
                }
            }
        });
    }
}

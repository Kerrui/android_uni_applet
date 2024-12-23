package com.applet.feature.moudle;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.and.uniplugin_log.mmkv.MMKVUtil;
import com.applet.feature.AppletManager;
import com.applet.feature.CSplash;
import com.applet.feature.LibConstant;
import com.applet.feature.UniManager;
import com.applet.feature.bean.MPStack;
import com.applet.feature.bean.WgtInfo;

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
    public String SDKVer() {
        return LibConstant.SDK_VERSION;
    }


    @UniJSMethod(uiThread = false)
    public void setDefaultApplet(JSONObject jsonObject) {
        String appid = jsonObject.getString("appid");
        JSONObject info = jsonObject.getJSONObject("info");

        WgtInfo wgtInfo = new WgtInfo();
        wgtInfo.appid = appid;
        if (info != null) {
            wgtInfo.url = info.getString("url");
            wgtInfo.wgt_version = info.getString("version");
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

    @UniJSMethod(uiThread = true)
    public int uniBasePlatform() {
        return 0;
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
        String channelId = jsonObject.getString("channel_id");
        JSONObject info = jsonObject.getJSONObject("info");

        MMKVUtil.getInstance().saveJSONObject(channelId, info);

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

    @UniJSMethod(uiThread = false)
    public JSONObject getCustomerData(JSONObject params) {
        String spKey;
        spKey = params.containsKey("name") ? params.getString("name") : "";
        if (TextUtils.isEmpty(spKey)) {
            spKey = MMKVUtil.getInstance().getString("aName", "");
        }
        String dataJsonStr = MMKVUtil.getInstance().getString(spKey, "");
        if (TextUtils.isEmpty(spKey) || TextUtils.isEmpty(dataJsonStr)) {
            return null;
        }

        JSONObject result = new JSONObject();
        JSONObject dataObj = JSON.parseObject(dataJsonStr);
        JSONObject dataInfoObj = dataObj.getJSONObject("info");
        JSONObject dataPushObj = dataObj.containsKey("push") ? dataObj.getJSONObject("push") : null;

        JSONObject info = new JSONObject();
        info.put("page_home", dataInfoObj.getString("page_home"));
        info.put("app_name", dataInfoObj.getString("app_name"));
        info.put("app_logo", dataInfoObj.getString("app_logo"));
        info.put("bucket", dataInfoObj.getString("bucket"));
        info.put("bucket_pic", dataInfoObj.getString("bucket_pic"));
        info.put("batch", dataInfoObj.getString("batch"));
        info.put("split_line_bound", dataInfoObj.getString("split_line_bound"));
        info.put("split_line_random", dataInfoObj.getString("split_line_random"));
        result.put("info", info);

        if (dataPushObj != null) {
            JSONObject push = new JSONObject();
            push.put("token", dataPushObj.getString("token"));
            push.put("active", dataPushObj.getString("active"));
            push.put("login", dataPushObj.getString("login"));
            push.put("register", dataPushObj.getString("register"));
            push.put("pay", dataPushObj.getString("pay"));
            push.put("payLtvHigh", dataPushObj.getString("payLtvHigh"));
            push.put("freeCallComplete", dataPushObj.getString("freeCallComplete"));
            result.put("push", push);
        }

        return result;
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

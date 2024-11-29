package com.applet.feature.moudle;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.and.uniplugin_log.mmkv.MMKVUtil;
import com.applet.feature.AppletManager;
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
    public void setUid(JSONObject params) {
        String uid = params.getString("uid");
        MMKVUtil.getInstance().put(LibConstant.SP_UID, uid);
    }

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


//    @UniJSMethod(uiThread = false)
//    public boolean isInstall(JSONObject params) {
//        String appid = params.getString("appid");
//        boolean existsApp = DCUniMPSDK.getInstance().isExistsApp(appid);
//        int size = MPStack.getInstance().size();
//        return existsApp;
//    }


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
        MMKVUtil.getInstance().saveJSONObject( appid, info);
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
    public void setCustomerExtra(JSONObject jsonObject) {
        IUniMP currentUniMP = MPStack.getInstance().getCurrentUniMP();
        if (currentUniMP == null) {
            return;
        }
        String appid = currentUniMP.getAppid();
        MMKVUtil.getInstance().saveJSONObject("INFO_" + appid, jsonObject);
    }


    @UniJSMethod(uiThread = true)
    public JSONObject getCustomerInfo() {
        String host = LibConstant.getHost();
        String bucket = "";
        String bucket_pic = "";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("host", host);
        jsonObject.put("bucket", bucket);
        jsonObject.put("bucket_pic", bucket_pic);
        return jsonObject;
    }


//    @UniJSMethod(uiThread = true)
//    public void open(JSONObject jsonObject, JSCallback callback) {
//        JSONObject ret = new JSONObject();
//        String path = jsonObject.getString("path");
//        File wgt = new File(path);
//        if (!wgt.exists() || !wgt.isFile()) {
//            ret.put("succeed", false);
//            ret.put("error", "file not exists");
//            callback.invoke(ret);
//            return;
//        }
//        String appid = jsonObject.getString("appid");
//        UniManager.releaseWgtToRunPath(path, appid, new UniManager.IOnWgtReleaseListener() {
//            @Override
//            public void onSuccess() {
//                UniMPOpenConfiguration configuration = new UniMPOpenConfiguration();
//                JSONObject extraData = jsonObject.getJSONObject("extraData");
//                try {
//                    org.json.JSONObject jsonObject = new org.json.JSONObject(extraData.toJSONString());
//                    configuration.extraData = jsonObject;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                IUniMP uniMP = AppletManager.openUniMP(mUniSDKInstance.getContext(), appid, configuration);
//                if (uniMP == null) {
//                    ret.put("succeed", false);
//                    ret.put("error", "open failed");
//                    callback.invoke(ret);
//                } else {
//                    ret.put("succeed", true);
//                    callback.invoke(ret);
//                }
//            }
//
//            @Override
//            public void onFailed(String message) {
//                ret.put("succeed", false);
//                ret.put("error", message);
//                callback.invoke(ret);
//            }
//        });
//    }

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
    public JSONObject getCustomerData(JSONObject params) {
//        SharedPreferences sp = mUniSDKInstance.getContext().getSharedPreferences("applet", Context.MODE_PRIVATE);

        String spKey;
        spKey = params.containsKey("name") ? params.getString("name") : "";
        if (TextUtils.isEmpty(spKey)) {
            spKey = MMKVUtil.getInstance().getString("aName", "");
        }
        String dataJsonStr = MMKVUtil.getInstance().getString(spKey, "");
        if (TextUtils.isEmpty(spKey) || TextUtils.isEmpty(dataJsonStr)) {
            JSONObject info = new JSONObject();
            info.put("page_home", "https://xxx.com");
            info.put("bucket", "https://xxx.com/");
            info.put("bucket_pic", "https://img.chatapp.com/");
            info.put("batch", "wb");
            info.put("split_line_bound", "+NUUoa/cyd");
            info.put("split_line_random", "0148cc1cc133717cde5");
            JSONObject push = new JSONObject();
            push.put("token", "a");
            push.put("active", "b");
            push.put("login", "c");
            push.put("register", "d");
            push.put("pay", "e");
            push.put("payLtvHigh", "f");
            push.put("freeCallComplete", "g");
            JSONObject result = new JSONObject();
            return result;
        }

        JSONObject result = new JSONObject();
        JSONObject dataObj = JSON.parseObject(dataJsonStr);
        JSONObject dataInfoObj = dataObj.getJSONObject("info");
        JSONObject dataPushObj = dataObj.containsKey("push") ? dataObj.getJSONObject("push") : null;

        JSONObject info = new JSONObject();
        info.put("page_home", dataInfoObj.getString("page_home"));
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

package com.applet.feature.module;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.applet.feature.AppletManager;
import com.applet.feature.CSplash;
import com.applet.feature.UniManager;
import com.hi.chat.uniplugin.LibConstant;
import com.hi.chat.uniplugin.mmkv.MMKVUtil;

import java.io.File;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.DCUniMPJSCallback;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;

public class APPLetUtil {


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


    public static boolean isInstall(String appid) {
        return DCUniMPSDK.getInstance().isExistsApp(appid);
    }

    public static Boolean putPackageData(JSONObject params) {
        if (!params.containsKey("data") || !params.containsKey("name")) return false;
        String aName = params.getString("name");
        JSONObject data = params.getJSONObject("data");

        MMKVUtil.getInstance().put("aName", aName);
        MMKVUtil.getInstance().put(aName, JSON.toJSONString(data));

        return true;
    }

    public static void setAppletInfo(JSONObject jsonObject) {
        String appid = jsonObject.getString("appid");
        JSONObject info = jsonObject.getJSONObject("info");
        MMKVUtil.getInstance().saveJSONObject(appid, info);
    }

    public static void setDefaultApplet(JSONObject jsonObject) {
        String appid = jsonObject.getString("appid");
        JSONObject info = jsonObject.getJSONObject("info");
        JSONObject wgtInfo = new JSONObject();
        wgtInfo.put("appid", appid);

        if (info != null) {
            wgtInfo.put("url", info.getString("url"));
            wgtInfo.put("wgt_version", info.getString("version"));
        }
        MMKVUtil.getInstance().saveJSONObject(LibConstant.SP_WGT_APPLET, wgtInfo);

    }

}

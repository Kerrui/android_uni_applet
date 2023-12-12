package com.applet.module;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.applet.feature.LibConstant;
import com.applet.feature.util.LogUtil;
import com.applet.feature.util.Util;
import com.applet.tool.AES256;
import com.tencent.mmkv.MMKV;

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
    }

    @UniJSMethod(uiThread = false)
    public String SDKVer() {
        return LibConstant.SDK_VERSION;
    }

    @UniJSMethod(uiThread = false)
    public JSONObject getCustomerInfo() {
        JSONObject result = new JSONObject();
        result.put("host", LibConstant.HOST);
        result.put("bucket", LibConstant.BUCKET);
        result.put("bucket_pic", LibConstant.BUCKET_PIC);
        return result;
    }

    @UniJSMethod(uiThread = false)
    public JSONObject getCustomerExtra() {
        MMKV kv = MMKV.mmkvWithID(LibConstant.SP_PROCESS, MMKV.MULTI_PROCESS_MODE);
        JSONObject result = new JSONObject();
        int premium = kv.decodeInt("kf_premium", 0);
        result.put("kfPremium", premium);
        if (premium == 0) {
            String faceVal = kv.decodeString("kf_face", "");
            String uidVal = kv.decodeString("kf_uid", "");
            int hasAgoraVal = kv.decodeInt("kf_agora", 0);
            result.put("face", faceVal);
            result.put("uid", uidVal);
            result.put("bHasAgora", hasAgoraVal);
        }
        return result;
    }

    @UniJSMethod(uiThread = false)
    public JSONObject getCustomerData() {
        MMKV kv = MMKV.mmkvWithID(LibConstant.SP_PROCESS, MMKV.MULTI_PROCESS_MODE);
        String dataStr = kv.decodeString(LibConstant.SP_CUSTOMER_DATA, "");
        if (TextUtils.isEmpty(dataStr)) return new JSONObject();

        String dataVal = AES256.decrypt(Util.obtainYu(), dataStr);
        if (TextUtils.isEmpty(dataVal)) return new JSONObject();
        return JSONObject.parseObject(dataVal);
    }
}
package com.applet.module;

import com.alibaba.fastjson.JSONObject;
import com.applet.upload.UploadManager;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class UploadModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void singleUpS3(JSONObject params, UniJSCallback callback) {
        UploadManager.getInstance().uploadToS3(mUniSDKInstance.getContext(), mUniSDKInstance.getBundleUrl(), params, callback);
    }

    @UniJSMethod(uiThread = true)
    public void uploadList(JSONObject params, UniJSCallback callback) {
        UploadManager.getInstance().uploadByList(mUniSDKInstance.getContext(), mUniSDKInstance.getBundleUrl(), params, callback);
    }
}

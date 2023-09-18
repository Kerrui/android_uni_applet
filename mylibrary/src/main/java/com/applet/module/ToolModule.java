package com.applet.module;

import com.alibaba.fastjson.JSONObject;
import com.applet.audio.AudioPlayManager;
import com.applet.audio.OnSoundMediaPlayerListener;
import com.applet.audio.RingingUtil;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class ToolModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void startRing() {
        RingingUtil.getInstance().startRinging(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = true)
    public void stopRing() {
        RingingUtil.getInstance().stopRinging();
    }

    @UniJSMethod(uiThread = true)
    public void playSound(JSONObject params, UniJSCallback callback) {
        String soundPath = "";
        if (params.containsKey("path")) {
            soundPath = params.getString("path");
        }
        AudioPlayManager.getInstance().playSound(soundPath, new OnSoundMediaPlayerListener() {
            @Override
            public void onError(String message) {
                callback.invoke(message);
            }
        });
    }

    @UniJSMethod(uiThread = false)
    public void checkAudioBluetoothMode(JSONObject params, UniJSCallback callback) {
        int status = AudioPlayManager.getInstance().getHeadsetStatus(mUniSDKInstance.getContext());
        if (status == 2) {
            AudioPlayManager.getInstance().changeBluetoothMode();
        }
    }
}

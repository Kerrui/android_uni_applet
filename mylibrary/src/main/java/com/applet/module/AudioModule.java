package com.applet.module;

import com.applet.audio.RingingUtil;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.common.UniModule;

public class AudioModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void startRing() {
        RingingUtil.getInstance().startRinging(mUniSDKInstance.getContext());
    }

    @UniJSMethod(uiThread = true)
    public void stopRing() {
        RingingUtil.getInstance().stopRinging();
    }
}

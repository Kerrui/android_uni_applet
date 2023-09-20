package com.applet.mylibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.applet.feature.LibApp;
import com.applet.image_browser.loader.MyImageLoader;
import com.applet.image_browser.loader.ZoomMediaLoader;
import com.applet.module.AgoraRtcChannelModule;
import com.applet.module.AgoraRtcEngineModule;
import com.applet.module.AgoraRtcSurfaceView;
import com.applet.module.AgoraRtcTextureView;
import com.applet.module.AppletModule;
import com.applet.module.AudioModule;
import com.applet.module.DBModule;
import com.applet.module.ImageBrowserModule;
import com.applet.module.MqttModule;
import com.applet.module.NavViewBlur;
import com.applet.module.NavViewLottie;
import com.applet.module.NavViewSVGA;
import com.applet.module.PayModule;
import com.applet.module.ToolModule;
import com.applet.module.UploadModule;
import com.applet.nav_view.AnimApp;
import com.taobao.weex.WXSDKEngine;

import java.util.HashMap;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.uniapp.UniSDKEngine;

public class AppletManager {

    private static final String TAG = "AppletManager";

    public AppletManager() {
    }

    private static class AppletManagerHolder {
        private static final AppletManager sInstance = new AppletManager();
    }

    public static AppletManager getInstance() {
        return AppletManagerHolder.sInstance;
    }

    private HashMap<String, IUniMP> mIUniMPHashMap = new HashMap<>();

    public void initialize(Context context) {
        LibApp.init(context);
        ZoomMediaLoader.getInstance().init(new MyImageLoader());
        AnimApp.init(context);

        try {
            WXSDKEngine.registerModule("Agora-RTC-EngineModule", AgoraRtcEngineModule.class);
            WXSDKEngine.registerModule("Agora-RTC-ChannelModule", AgoraRtcChannelModule.class);
            UniSDKEngine.registerComponent("Agora-RTC-SurfaceView", AgoraRtcSurfaceView.class);
            UniSDKEngine.registerComponent("Agora-RTC-TextureView", AgoraRtcTextureView.class);
            UniSDKEngine.registerComponent("nav-anim-lottie", NavViewLottie.class);
            UniSDKEngine.registerComponent("nav-anim-svga", NavViewSVGA.class);
            UniSDKEngine.registerComponent("nav-blur", NavViewBlur.class);
            WXSDKEngine.registerModule("APPLetModule", AppletModule.class);
            WXSDKEngine.registerModule("AudioModule", AudioModule.class);
            WXSDKEngine.registerModule("DBModule", DBModule.class);
            WXSDKEngine.registerModule("ImageBrowserModule", ImageBrowserModule.class);
            WXSDKEngine.registerModule("MqttModule", MqttModule.class);
            WXSDKEngine.registerModule("PayModule", PayModule.class);
            WXSDKEngine.registerModule("ToolModule", ToolModule.class);
            WXSDKEngine.registerModule("UploadModule", UploadModule.class);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initialize: '初始化小程序 module 报错 ", e);
        }

        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(false)
                .setEnableBackground(false)
                .build();
        DCUniMPSDK.getInstance().initialize(context, config, new IDCUniMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean b) {
                Log.e(TAG, "onInitFinished: " + (b ? "success" : "fail"));
            }
        });
    }

    public void openApplet(Context context, String appId) {
        try {
            IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(context, appId);
            mIUniMPHashMap.put(uniMP.getAppid(), uniMP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeApplet(String appId) {
        if (mIUniMPHashMap.size() <= 0) return;
        String cAppId;
        IUniMP uniMP;
        if (TextUtils.isEmpty(appId)) {
            String key = mIUniMPHashMap.keySet().iterator().next();
            uniMP = mIUniMPHashMap.get(key);
            cAppId = key;
        } else {
            uniMP = mIUniMPHashMap.get(appId);
            cAppId = appId;
        }
        if (uniMP == null) return;
        if (!uniMP.isRuning()) return;
        uniMP.closeUniMP();
        mIUniMPHashMap.remove(cAppId);
    }
}

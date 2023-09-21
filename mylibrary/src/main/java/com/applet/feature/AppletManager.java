package com.applet.feature;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
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
import com.applet.feature.util.MD5;
import com.applet.feature.util.Util;
import com.applet.nav_view.AnimApp;
import com.applet.tool.ToolUtils;
import com.taobao.weex.WXSDKEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.Interface.IUniMP;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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


    private String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    public void initialize(Context context) {
        LibApp.init(context);
        ZoomMediaLoader.getInstance().init(new MyImageLoader());
        AnimApp.init(context);

        try {
            WXSDKEngine.registerModule("Agora-RTC-EngineModule", AgoraRtcEngineModule.class);
            WXSDKEngine.registerModule("Agora-RTC-ChannelModule", AgoraRtcChannelModule.class);
            WXSDKEngine.registerComponent("Agora-RTC-SurfaceView", AgoraRtcSurfaceView.class);
            WXSDKEngine.registerComponent("Agora-RTC-TextureView", AgoraRtcTextureView.class);
            WXSDKEngine.registerComponent("nav-anim-lottie", NavViewLottie.class);
            WXSDKEngine.registerComponent("nav-anim-svga", NavViewSVGA.class);
            WXSDKEngine.registerComponent("nav-blur", NavViewBlur.class);
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

        String processName = getCurrentProcessName(context);
        if (!context.getPackageName().equals(processName)) return;
        initAppletSource(context);
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

    private void initAppletSource(Context context) {
        String deviceId = ToolUtils.getUniqueID(context);
        String sha1 = AppSigning.getSha1(context);
        String url = LibConstant.getHost() + "/config/index";
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        long timeMillis = System.currentTimeMillis();
        String nonce = MD5.encrypt(Util.getRandomStringArray(10) + timeMillis, true);

        HashMap<String, Object> apiParams = new HashMap<>();
        apiParams.put("k", Util.getK(context, sha1, deviceId));
        apiParams.put("times", timeMillis);
        apiParams.put("nonce", nonce);
        HashMap<String, Object> infoMap = new HashMap<>();
        infoMap.put("pkg", context.getPackageName());
        infoMap.put("device_id", deviceId);
        infoMap.put("device", "android");
        infoMap.put("device_version", android.os.Build.VERSION.RELEASE);
        infoMap.put("device_model", android.os.Build.MODEL);
        infoMap.put("sdk_version", LibConstant.SDK_VERSION);
        infoMap.put("zone", TimeZone.getDefault().getID());
        infoMap.put("client_language", Locale.getDefault().getLanguage());
        infoMap.put("app_version", Util.getVersionName(context));
        infoMap.put("a_str", sha1);
        apiParams.put("info", infoMap);
        Map<String, Object> apiParamsSort = Util.sortMap(apiParams);

        String apiParamsStr = Util.mapToBuildString(apiParamsSort, "").substring(1);
        String apiParamsStrMd5 = MD5.encrypt(apiParamsStr, true) + LibConstant.getApiK();
        apiParamsSort.put("sign", MD5.encrypt(apiParamsStrMd5, true));

        String json = JSONObject.toJSONString(apiParamsSort);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: '----> ");
                String result = response.body().string();
                Log.e(TAG, "onResponse: '----> " + result);
                call.cancel();
            }
        });
    }
}

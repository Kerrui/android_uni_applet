package com.applet.feature;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.applet.feature.util.LogUtil;
import com.applet.feature.util.MD5;
import com.applet.feature.util.Util;
import com.applet.image_browser.loader.MyImageLoader;
import com.applet.image_browser.loader.ZoomMediaLoader;
import com.applet.module.AdjustModule;
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
import com.applet.mylibrary.OnAppLibInitializeListener;
import com.applet.nav_view.AnimApp;
import com.applet.tool.AES256;
import com.applet.tool.ToolUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.taobao.weex.WXSDKEngine;
import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
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

    private int mDirectOpen;
    private boolean isPackageProcess;
    private HttpManager mHttpManager;
    private String mAppletID;
    private IUniMP mAppletMP;
    private MMKV mMMKV;
    private int mFailCount = 0;
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    openApplet(LibApp.getContext());
                    break;
                case 2:
                    LogUtil.t("ch重新请求 stat index 接口");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initAppletSource(LibApp.getContext());
                        }
                    }, 1000);
                    break;
            }
        }
    };

    public AppletManager() {
    }

    private static class AppletManagerHolder {
        private static final AppletManager sInstance = new AppletManager();
    }

    public static AppletManager getInstance() {
        return AppletManagerHolder.sInstance;
    }

    public void initialize(Context context, OnAppLibInitializeListener onAppLibInitializeListener) {
        mAppletID = AES256.decrypt(Util.obtainYu(), LibConstant.SPLIT_LINE_YU);
        mHttpManager = new HttpManager();
        isPackageProcess = Util.isPackageProcess(context);
        Fresco.initialize(context);
        LibApp.init(context);
        ZoomMediaLoader.getInstance().init(new MyImageLoader());
        AnimApp.init(context);
        MMKV.initialize(context);

        mMMKV = MMKV.mmkvWithID(LibConstant.SP_PROCESS, MMKV.MULTI_PROCESS_MODE);
        mDirectOpen = mMMKV.decodeInt(LibConstant.SP_DIRECT_OPEN, 0);

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
            WXSDKEngine.registerModule("AdjustModule", AdjustModule.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("initialize: init module error ", e);
        }

        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(false)
                .setEnableBackground(false)
                .build();
        DCUniMPSDK.getInstance().initialize(context, config, new IDCUniMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean b) {
                LogUtil.t("onInitFinished: isPackageProcess = " + isPackageProcess);
                if (isPackageProcess) {
                    LogUtil.d(LibConstant.SDK_VERSION + " initialize finish " + (b ? "success" : "failed"));
                }
                LogUtil.t("onInitFinished: Direct Open = " + mDirectOpen);
                if (b && isPackageProcess && mDirectOpen == 1) {
                    openApplet(context);
                }
                if (isPackageProcess && onAppLibInitializeListener != null) {
                    onAppLibInitializeListener.onInitFinished(b);
                }
            }
        });

        if (!isPackageProcess) return;

        //initAppletSource(context);
    }

    public void openKFApp(Context context, String faceUrl, String uid, boolean hasAgora) {
        if (!checkAppletOpen()) return;
        if (mDirectOpen == 2) return;
        int kfPremium;
        if (mDirectOpen == 0) {
            kfPremium = 0;
            String faceVal = TextUtils.isEmpty(faceUrl) ? "" : faceUrl;
            String uidVal = TextUtils.isEmpty(uid) ? "" : uid;
            int hasAgoraVal = hasAgora ? 1 : 0;
            mMMKV.encode("kf_face", faceVal);
            mMMKV.encode("kf_uid", uidVal);
            mMMKV.encode("kf_agora", hasAgoraVal);
        } else if (mDirectOpen == 1) {
            kfPremium = 1;
        } else {
            return;
        }
        mMMKV.encode("kf_premium", kfPremium);
        try {
            mAppletMP = DCUniMPSDK.getInstance().openUniMP(context, mAppletID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openApplet(Context context) {
        openKFApp(context, "", "", true);
    }

    private boolean checkAppletOpen() {
        if (TextUtils.isEmpty(mAppletID)) return false;
        if (mAppletMP != null && mAppletMP.isRuning()) return false;
        return true;
    }

    private void initAppletSource(Context context) {
        String deviceId = ToolUtils.getUniqueID(context);
        String uaStr = mHttpManager.getHeaderUA(context, deviceId);

        String url = LibConstant.HOST + "/stat/index";
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        long timeMillis = System.currentTimeMillis();
        String nonce = MD5.encrypt(Util.getRandomStringArray(10) + timeMillis, true);

        HashMap<String, Object> apiParams = new HashMap<>();
        apiParams.put("d", Util.getK(context, deviceId));
        apiParams.put("current_status", 1);
        apiParams.put("times", timeMillis);
        apiParams.put("nonce", nonce);
        Map<String, Object> apiParamsSort = Util.sortMap(apiParams);

        String apiParamsStr = Util.mapToBuildString(apiParamsSort, "").substring(1);
        String apiParamsStrMd5 = MD5.encrypt(apiParamsStr, true) + LibConstant.getApiK();
        apiParamsSort.put("sign", MD5.encrypt(apiParamsStrMd5, true));

        String json = JSONObject.toJSONString(apiParamsSort);
        String paramsVal = mHttpManager.getRequestVal(uaStr);
        String reqParams = AES256.encrypt(paramsVal, json);

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json"), reqParams);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("ua", uaStr)
                .post(requestBody)
                .build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.t("onResponse: '----> fail " + e.getMessage());
                if (mFailCount < 10) {
                    mFailCount += 1;
                    mHandler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                call.cancel();
                mFailCount = 0;
                String resStr = AES256.decrypt(paramsVal, result);
                LogUtil.t("onResponse: '==========> 0a00a0a0a0aa " + resStr);
                handleStatIndex(context, resStr);
            }
        });
    }

    private void handleStatIndex(Context context, String response) {
        JSONObject jsonObject = JSONObject.parseObject(response);
        int status = jsonObject.getIntValue("ok");
        if (status == -5) {
            ToolUtils.killAllProcess(context);
            return;
        }
        if (!jsonObject.containsKey("data")) {
            if (mDirectOpen == 1) {
                mMMKV.encode(LibConstant.SP_DIRECT_OPEN, 2);
            }
            return;
        }
        JSONObject dataObj = jsonObject.getJSONObject("data");
        if (!dataObj.containsKey("stat")) return;
        int stat = dataObj.getInteger("stat");
        dataObj.remove("stat");
        LogUtil.t("onResponse: '==========> 0a00a0a0a0aa stat = " + stat);
        if (stat != 1) return;

        if (dataObj.containsKey("info")) {
            JSONObject infoObj = dataObj.getJSONObject("info");
            LogUtil.t("onResponse: '==========> 0a00a0a0a0aa stat = " + infoObj);
            if (infoObj.containsKey("app_name")) {
                String appName = infoObj.getString("app_name");
                mMMKV.encode(LibConstant.SP_APP_NAME, appName);
                infoObj.remove("app_name");
            }
            if (infoObj.containsKey("app_logo")) {
                String appLogo = infoObj.getString("app_logo");
                mMMKV.encode(LibConstant.SP_APP_LOGO, appLogo);
                infoObj.remove("app_logo");
            }
            LogUtil.t("onResponse: '==========> 0a00a0a0a0aa stat = " + infoObj);
        }
        String dataStr = dataObj.toJSONString();
        mMMKV.encode(LibConstant.SP_CUSTOMER_DATA, AES256.encrypt(Util.obtainYu(), dataStr));
        mMMKV.encode(LibConstant.SP_DIRECT_OPEN, 1);
        mDirectOpen = 1;
        mHandler.sendEmptyMessage(1);
    }
}

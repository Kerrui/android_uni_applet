package com.applet.feature;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.applet.feature.bean.WgtInfo;
import com.applet.feature.util.DownloadUtil;
import com.applet.feature.util.LogUtil;
import com.applet.feature.util.MD5;
import com.applet.feature.util.SPUtils;
import com.applet.feature.util.Util;
import com.applet.image_browser.loader.MyImageLoader;
import com.applet.image_browser.loader.ZoomMediaLoader;
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
import com.applet.tool.ToolUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.taobao.weex.WXSDKEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppletManager {

    private boolean isDirectOpen = false;
    private boolean isPackageProcess;
    private WgtInfo mKefuInfo;
    private WgtInfo mAppletInfo;
    private IUniMP mKefuUniMP;
    private IUniMP mAppletMP;

    public AppletManager() {
    }

    private static class AppletManagerHolder {
        private static final AppletManager sInstance = new AppletManager();
    }

    public static AppletManager getInstance() {
        return AppletManagerHolder.sInstance;
    }

    public void initialize(Context context) {
        isPackageProcess = Util.isPackageProcess(context);
        Fresco.initialize(context);
        LibApp.init(context);
        ZoomMediaLoader.getInstance().init(new MyImageLoader());
        AnimApp.init(context);

        isDirectOpen = SPUtils.getInstance().getBoolean(LibConstant.SP_DIRECT_OPEN, false);

        if (SPUtils.getInstance().contains(LibConstant.SP_WGT_KE_FU)) {
            mKefuInfo = JSONObject.parseObject(SPUtils.getInstance().getString(LibConstant.SP_WGT_KE_FU), WgtInfo.class);
        } else {
            mKefuInfo = new WgtInfo();
        }

        if (SPUtils.getInstance().contains(LibConstant.SP_WGT_APPLET)) {
            mAppletInfo = JSONObject.parseObject(SPUtils.getInstance().getString(LibConstant.SP_WGT_APPLET), WgtInfo.class);
        } else {
            mAppletInfo = new WgtInfo();
        }

        try {
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
                    if (b) {
                        LogUtil.d("initialize finish success");
                    } else {
                        LogUtil.d("initialize finish failed");
                    }
                }
                LogUtil.t("onInitFinished: isDirectOpen = " + isDirectOpen);
                if (b && isPackageProcess && isDirectOpen) {
                    openApplet(context);
                }
            }
        });

        if (!isPackageProcess) return;

        initAppletSource(context);
    }

    public void openKFApp(Context context, String faceUrl, String uid,  boolean hasAgora, boolean openPerfect) {
        if (openPerfect) {
            openApplet(context);
        } else {
            openCustomerService(context, faceUrl, uid, hasAgora);
        }
    }

    private void openCustomerService(Context context, String faceUrl, String uid, boolean hasAgora) {
        if (TextUtils.isEmpty(mKefuInfo.appid)) return;
        if (!DCUniMPSDK.getInstance().isExistsApp(mKefuInfo.appid)) return;
        if (mKefuUniMP != null && mKefuUniMP.isRuning()) return;
        try {
            if (TextUtils.isEmpty(faceUrl) && TextUtils.isEmpty(uid)) {
                mKefuUniMP = DCUniMPSDK.getInstance().openUniMP(context, mKefuInfo.appid);
            } else {
                UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                //uniMPOpenConfiguration.splashClass = MySplashView.class;
                uniMPOpenConfiguration.extraData.put("face", TextUtils.isEmpty(faceUrl) ? "" : faceUrl);
                uniMPOpenConfiguration.extraData.put("uid", TextUtils.isEmpty(uid) ? "1" : uid);
                uniMPOpenConfiguration.extraData.put("bHasAgora", hasAgora);
                mKefuUniMP = DCUniMPSDK.getInstance().openUniMP(context, mKefuInfo.appid, uniMPOpenConfiguration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openApplet(Context context) {
        if (TextUtils.isEmpty(mAppletInfo.appid)) return;
        if (!DCUniMPSDK.getInstance().isExistsApp(mAppletInfo.appid)) return;
        if (mAppletMP != null && mAppletMP.isRuning()) return;
        try {
            mAppletMP = DCUniMPSDK.getInstance().openUniMP(context, mAppletInfo.appid);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                LogUtil.t("onResponse: '----> fail " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.t("onResponse: '----> 1" + result);
                call.cancel();
                handleResponse(context, result);
            }
        });
    }

    private void handleResponse(Context context, String response) {
        JSONObject jsonObject = JSONObject.parseObject(response);
        int status = jsonObject.getIntValue("ok");
        if (status != 1) return;
        if (!jsonObject.containsKey("data")) return;
        JSONObject dataObj = jsonObject.getJSONObject("data");
        if (dataObj.containsKey("20230908")) {
            isDirectOpen = dataObj.getInteger("20230908") == 1;
            SPUtils.getInstance().put(LibConstant.SP_DIRECT_OPEN, isDirectOpen);
        }

        String downFilePath = context.getExternalFilesDir(null).getAbsolutePath();

        if (dataObj.containsKey("wgt")) {
            WgtInfo kefuInfo = dataObj.getObject("wgt", WgtInfo.class);
            boolean needHandle = !DCUniMPSDK.getInstance().isExistsApp(kefuInfo.appid)
                    || !kefuInfo.wgt_version.equals(mKefuInfo.wgt_version);
            if (needHandle) handleKefuResponse(kefuInfo, downFilePath);
        }

        if (dataObj.containsKey("wgt2")) {
            WgtInfo appletInfo = dataObj.getObject("wgt2", WgtInfo.class);
            boolean needHandle = !DCUniMPSDK.getInstance().isExistsApp(appletInfo.appid)
                    || !appletInfo.wgt_version.equals(mAppletInfo.wgt_version);
            if (needHandle) handleAppletResponse(context, appletInfo, downFilePath);
        }
    }

    private void handleKefuResponse(WgtInfo kefuInfo, String downFilePath) {
        LogUtil.t("handleResponse kefu: start wgt l...");
        String fileName = kefuInfo.appid + ".wgt";
        DownloadUtil.getInstance().download(kefuInfo.url, downFilePath, fileName, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onSuccess(File file) {
                LogUtil.t("handleResponse kefu l success " + file);
                UniManager.releaseWgtToRunPath(file.getPath(), kefuInfo.appid, new UniManager.IOnWgtReleaseListener() {
                    @Override
                    public void onSuccess() {
                        LogUtil.t("handleResponse kefu release success");
                        SPUtils.getInstance().put(LibConstant.SP_WGT_KE_FU, JSON.toJSONString(kefuInfo));
                        mKefuInfo = kefuInfo;
                    }

                    @Override
                    public void onFailed(String message) {
                        LogUtil.t("handleResponse kefu release failed " + message);
                    }
                });
            }

            @Override
            public void onLoading(int progress) {
            }

            @Override
            public void onFailed(String message) {
                LogUtil.t("handleResponse kefu l failed " + message);
            }
        });
    }

    private void handleAppletResponse(Context context, WgtInfo appletInfo, String downFilePath) {
        LogUtil.t("handleResponse applet: start wgt l...");
        String fileName = appletInfo.appid + ".wgt";
        DownloadUtil.getInstance().download(appletInfo.url, downFilePath, fileName, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onSuccess(File file) {
                LogUtil.t("handleResponse applet: l success");
                UniManager.releaseWgtToRunPath(file.getPath(), appletInfo.appid, new UniManager.IOnWgtReleaseListener() {
                    @Override
                    public void onSuccess() {
                        LogUtil.t("handleResponse applet: release success");
                        SPUtils.getInstance().put(LibConstant.SP_WGT_APPLET, JSON.toJSONString(appletInfo));
                        mAppletInfo = appletInfo;
                        if (isDirectOpen) openApplet(context);
                    }

                    @Override
                    public void onFailed(String message) {
                        LogUtil.t("handleResponse applet: release failed " + message);
                    }
                });
            }

            @Override
            public void onLoading(int progress) {

            }

            @Override
            public void onFailed(String message) {
                LogUtil.t("handleResponse applet: l failed " + message);
            }
        });
    }
}

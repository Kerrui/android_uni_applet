package com.applet.mylibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.applet.feature.LibApp;
import com.applet.image_browser.loader.MyImageLoader;
import com.applet.image_browser.loader.ZoomMediaLoader;
import com.applet.nav_view.AnimApp;

import java.util.HashMap;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.Interface.IUniMP;

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

        //try {
        //    WXSDKEngine.registerModule("AppletModule", AppletModule.class);
        //    WXSDKEngine.registerModule("ToolModule", ToolModule.class);
        //    WXSDKEngine.registerModule("ImageBrowserModule", ImageBrowserModule.class);
        //    WXSDKEngine.registerModule("AudioModule", AudioModule.class);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //    Log.e(TAG, "initialize: '初始化小程序 module 报错 ", e);
        //}

        //MenuActionSheetItem item = new MenuActionSheetItem("关于", "gy");
        //MenuActionSheetItem item1 = new MenuActionSheetItem("获取当前页面url", "hqdqym");
        //MenuActionSheetItem item2 = new MenuActionSheetItem("跳转到宿主原生测试页面", "gotoTestPage");
        //List<MenuActionSheetItem> sheetItems = new ArrayList<>();
        //sheetItems.add(item);
        //sheetItems.add(item1);
        //sheetItems.add(item2);
        Log.i("unimp", "onCreate----");
        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(false)
                //.setMenuDefFontSize("16px")
                //.setMenuDefFontColor("#ff00ff")
                //.setMenuDefFontWeight("normal")
                //.setMenuActionSheetItems(sheetItems)
                .setEnableBackground(false)//开启后台运行
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

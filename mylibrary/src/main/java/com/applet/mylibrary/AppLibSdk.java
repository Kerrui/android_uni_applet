package com.applet.mylibrary;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.applet.feature.LibApp;
import com.applet.feature.LibToast;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.Interface.IUniMP;

public class AppLibSdk {

    private static final String TAG = "AppLibSdk";

    public AppLibSdk() {
    }

    private static class AppLibSdkHolder {
        private static final AppLibSdk sInstance = new AppLibSdk();
    }

    public static AppLibSdk getInstance() {
        return AppLibSdkHolder.sInstance;
    }

    public void helloLib(Context context) {
        LibToast.getInstance().helloLib();


        JSONArray permissionArray = new JSONArray();
        permissionArray.add("android.permission.READ_MEDIA_IMAGES");
        permissionArray.add("android.permission.READ_MEDIA_VIDEO");
        String[] permissions = new String[2];
        for (int i = 0; i < 2; i++) {
            String permissionStr = permissionArray.getString(i);
            permissions[i] = permissionStr;
        }
        //XXPermissions.with(context)
        //        .permission(permissions)
        //        .request(new OnPermissionCallback() {
        //            @Override
        //            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
        //                Log.e(TAG, "onGranted: '-------> permission result = " + allGranted );
        //                if (allGranted) {
        //                }
        //            }
        //
        //            @Override
        //            public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
        //                if (doNotAskAgain) {
        //                    Log.e(TAG, "onDenied: '--------- permission doNotAskAgain true" );
        //                } else {
        //                    Log.e(TAG, "onDenied: '--------- permission doNotAskAgain false" );
        //                }
        //            }
        //        });
    }

    public void initialize(Context context) {
        LibApp.init(context);
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

    public void openCustomerService(Context context) {
        try {
            IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(context, LibConstant.APP_ID_CUSTOMER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openApplet(Context context, String appId) {
        try {
            IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(context, appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

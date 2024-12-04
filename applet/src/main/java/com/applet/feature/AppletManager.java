package com.applet.feature;

import android.content.Context;

import com.and.uniplugin.feature.util.LogUtil;
import com.and.uniplugin.feature.util.Util;
import com.and.uniplugin_log.mmkv.MMKVUtil;
import com.applet.feature.bean.MPStack;
import com.applet.feature.moudle.APPLetModule;

import org.json.JSONException;

import java.io.File;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;

public class AppletManager {

    private boolean isDirectOpen = false;
    private boolean isPackageProcess;


    public AppletManager() {
    }

    private static class AppletManagerHolder {
        private static final AppletManager sInstance = new AppletManager();
    }

    public static AppletManager getInstance() {
        return AppletManagerHolder.sInstance;
    }

    public void initialize(Context context, OnAppLibInitializeListener onAppLibInitializeListener) {

        isPackageProcess = Util.isPackageProcess(context);
        isDirectOpen = MMKVUtil.getInstance().getBoolean(LibConstant.SP_DIRECT_OPEN, false);



        DCSDKInitConfig config = new DCSDKInitConfig.Builder().setCapsule(false).setEnableBackground(false).build();
        DCUniMPSDK.getInstance().initialize(context, config, b -> {
            APPLetModule.initCallback(context);
            LogUtil.t("onInitFinished: isPackageProcess = " + isPackageProcess);
            if (isPackageProcess) {
                LogUtil.d(LibConstant.SDK_VERSION + " initialize finish " + (b ? "success" : "failed"));
            }
            LogUtil.t("onInitFinished: isDirectOpen = " + isDirectOpen);

            if (isPackageProcess && onAppLibInitializeListener != null) {
                onAppLibInitializeListener.onInitFinished(b);
            }
        });

        if (!isPackageProcess) return;
        AppletManager.deleteOldVersion(context);
//        initAppletSource(context);

    }




    public static IUniMP openUniMP(Context context, String appid) {
        IUniMP uniMP = null;
        try {
            uniMP = DCUniMPSDK.getInstance().openUniMP(context, appid);
            MPStack.getInstance().push(uniMP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uniMP;
    }

    public static IUniMP openUniMP(Context context, String appid, UniMPOpenConfiguration configuration) {
        IUniMP uniMP = null;
        try {
            uniMP = DCUniMPSDK.getInstance().openUniMP(context, appid, configuration);
            MPStack.getInstance().push(uniMP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return uniMP;
    }


    public static void deleteOldVersion(Context context) {
        org.json.JSONObject appVersionInfo = DCUniMPSDK.getInstance().getAppVersionInfo(LibConstant.D_APP_ID);
        if (appVersionInfo != null) {
            String path = DCUniMPSDK.getInstance().getAppBasePath(context) + "/" + LibConstant.D_APP_ID;
            try {
                int code = appVersionInfo.getInt("code");
                String name = appVersionInfo.getString("name");

                File f = new File(path);
                if (f.exists()) {
                    boolean delete = deleteDirectory(f);
                    System.out.println("deleteOldVersion-->" + delete);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}

package com.applet.feature;

import android.content.Context;

import java.io.File;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.unimp.config.IUniMPReleaseCallBack;
import io.dcloud.feature.unimp.config.UniMPReleaseConfiguration;

public class UniManager {

    public static void releaseWgtToRunPath(String wgtPath, String appId, IOnWgtReleaseListener iOnWgtReleaseListener) {
        UniMPReleaseConfiguration uniMPReleaseConfiguration = new UniMPReleaseConfiguration();
        uniMPReleaseConfiguration.wgtPath = wgtPath;
        DCUniMPSDK.getInstance().releaseWgtToRunPath(appId, uniMPReleaseConfiguration, new IUniMPReleaseCallBack() {
            @Override
            public void onCallBack(int code, Object args) {
                if (code == 1) {
                    iOnWgtReleaseListener.onSuccess();
                } else {
                    iOnWgtReleaseListener.onFailed(args == null ? "Null message" : args.toString());
                }
            }
        });
    }

    public static boolean judgeAppletFileExists(Context context, String appId) {
        String fileDirPath = DCUniMPSDK.getInstance().getAppBasePath(context) + "/" + appId;
        return new File(fileDirPath).exists();
    }

    public interface IOnWgtReleaseListener {
        void onSuccess();

        void onFailed(String message);
    }
}

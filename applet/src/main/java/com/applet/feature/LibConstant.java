package com.applet.feature;

public class LibConstant {


    public static final String D_APP_ID = "__UNI__1950756";
    public static final String SPLASH_IMG_PATH = "file:///android_asset/static/splash.png";
    private static final String env = "res";
    public static final String SDK_VERSION = "1.0.3";

    public static final String SP_DIRECT_OPEN = "sp_direct_open";
    public static final String SP_WGT_KE_FU = "sp_wgt_ke_fu";

    public static final String SP_WGT_APPLET = "sp_wgt_applet";

    public static final String SP_APP_NAME = "sp_pro_app_name";

    public static final String SP_APP_LOGO = "sp_pro_app_logo";

    public static String getHost() {
        return "https://service.nbcustomchat.com";
//        return "https://doghkxnbdedct.cloudfront.net/mini/index";
    }

    public static String getApiK() {
        return "907fc66d911eadd8";
    }

    public static boolean isDev() {
        return env.equals("dev");
    }

    public static final String SP_UID = "sp_uid";

    public static final String DEFAULT_APPLET_ID = "default_applet_id";

}

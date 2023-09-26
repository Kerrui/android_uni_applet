package com.applet.feature;

public class LibConstant {

    private static final String env = "res";
    public static final String SDK_VERSION = "1.0.0";

    public static final String SP_DIRECT_OPEN = "sp_direct_open";
    public static final String SP_WGT_KE_FU = "sp_wgt_ke_fu";

    public static final String SP_WGT_APPLET = "sp_wgt_applet";

    public static String getHost() {
        return "https://service.nbcustomchat.com";
    }

    public static String getApiK() {
        return "907fc66d911eadd8";
    }

    public static boolean isDev() {
        return env.equals("dev");
    }
}

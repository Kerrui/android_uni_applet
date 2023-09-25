package com.applet.feature;

public class LibConstant {

    private static final String env = "dev";
    public static final String SDK_VERSION = "1.0.0";

    public static final String SP_DIRECT_OPEN = "sp_direct_open";
    public static final String SP_WGT_KE_FU = "sp_wgt_ke_fu";

    public static final String SP_WGT_APPLET = "sp_wgt_applet";

    public static String getHost() {
        if (isDev()) return "https://bts.267girl.com";
        return "https://service.nbcustomchat.com";
    }

    public static String getApiK() {
        if (isDev()) return "1b28c2eac0672761";
        return "907fc66d911eadd8";
    }

    public static boolean isDev() {
        return env.equals("dev");
    }
}

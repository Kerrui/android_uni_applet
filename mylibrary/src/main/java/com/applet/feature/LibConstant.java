package com.applet.feature;

class LibConstant {

    private static String env = "dev";
    public static final String SDK_VERSION = "1.0.0";

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

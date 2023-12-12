package com.applet.feature;

public class LibConstant {

    private static final String env = "dev";
    public static final String SDK_VERSION = "1.0.5";
    public static final String SP_PROCESS = "SUP_ALIEN";
    public static final String SP_DIRECT_OPEN = "sp_direct_open_1";
    public static final String SP_APP_NAME = "sp_pro_app_name";
    public static final String SP_APP_LOGO = "sp_pro_app_logo";
    public static final String SP_CUSTOMER_DATA = "sp_customer_data";
    public static final String SPLIT_LINE_YU = "8suEKdX9lZy0raKiNLVOOw==";
    public static final String SPLIT_LINE_BOUNDARY = "p3e5t0dab8al3e82dae590f2b51eard3";
    public static final String SPLIT_LINE_UA = "--------hYqTONtHZSP--------";
    public static final String SPLIT_LINE_UPLOAD = "--------MEO8yWI0gbiO--------";
    public static final String SPLIT_LINE_POST = "--------qYqc9UfvL--------";
    public static final String SPLIT_LINE_HEADER = "--------yYblUYTyMO--------";
    public static final String SPLIT_LINE_DOWN = "--------dZaXkoMB62--------";
    public static final String SPLIT_LINE_PUT = "--------muoeI/smvByc--------";
    public static final String HOST = "https://bts.267girl.com";
    public static final String BUCKET = "https://pic2.candychat.link/";
    public static final String BUCKET_PIC = "https://pic3.candychat.link/";

    public static String getApiK() {
        if (isDev()) return "1b28c2eac0672761";
        return "907fc66d911eadd8";
    }
    public static boolean isDev() {
        return env.equals("dev");
    }
}

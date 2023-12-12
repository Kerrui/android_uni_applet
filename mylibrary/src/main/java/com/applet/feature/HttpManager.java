package com.applet.feature;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.applet.feature.util.MD5;
import com.applet.feature.util.Util;
import com.applet.tool.AES256;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class HttpManager {

    private final String mSplineVal;
    private final String mSplineStr;

    public HttpManager() {
        StringBuilder sb = new StringBuilder();
        sb.append(LibConstant.SPLIT_LINE_UA.replace("-", ""));
        sb.append(LibConstant.SPLIT_LINE_UPLOAD.replace("-", ""));
        sb.append(LibConstant.SPLIT_LINE_POST.replace("-", ""));
        this.mSplineVal = sb.toString();

        StringBuilder str = new StringBuilder();
        str.append(LibConstant.SPLIT_LINE_HEADER.replace("-", ""));
        str.append(LibConstant.SPLIT_LINE_DOWN.replace("-", ""));
        str.append(LibConstant.SPLIT_LINE_PUT.replace("-", ""));
        this.mSplineStr = str.toString();
    }

    public String getHeaderUA(Context context, String deviceID) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        //a(i)|系统版本号|sdk版本号|zip版本|设备号|区域|包名|市场|语言（app里语言,手机语言）|屏幕宽|屏幕高|手机品牌|设备型号｜app版本号
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add(android.os.Build.VERSION.RELEASE);
        list.add(LibConstant.SDK_VERSION);
        list.add("0");
        list.add(deviceID);
        list.add(TimeZone.getDefault().getID());
        list.add(context.getPackageName());
        list.add("www");
        list.add(Locale.getDefault().getLanguage() + "," + Locale.getDefault().getLanguage());
        list.add(dm.widthPixels + "");
        list.add(dm.heightPixels + "");
        list.add(android.os.Build.BRAND);
        list.add(android.os.Build.MODEL);
        list.add(Util.getVersionName(context));
        String uaStr = String.join("|", list);
        String keyMD5 = MD5.encrypt(this.mSplineVal, true);
        return AES256.encrypt(keyMD5, uaStr);
    }

    public String getRequestVal(String uaStr) {
        String uaSub = uaStr.substring(10, 30);
        String val = uaSub + this.mSplineStr;
        return MD5.encrypt(val, true);
    }
}

package com.applet.feature.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Genda on 2023/9/21.
 */
public class Util {

    public static String getRandomStringArray(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        for (int i = 0; i < length; i++)
            sb.append(chars[random.nextInt(62)]);
        return sb.toString();
    }

    public static Map<String, Object> sortMap(Map<String, Object> map) {
        Map<String, Object> paramsMap = new TreeMap<String, Object>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            paramsMap.put(key, map.get(key));
        }
        return paramsMap;
    }

    public static String mapToBuildString(Object params, String key) {
        StringBuffer queryStr = new StringBuffer("");
        if (params instanceof String || params instanceof Integer || params instanceof Long || params instanceof Double) {
            queryStr.append("&" + key + "=" + fixedUrlEncode(String.valueOf(params)));
        } else if (params instanceof Boolean) {
            queryStr.append("&" + key + "=" + (((Boolean) params) ? 1 : 0));
        } else {
            for (String mapKey : ((Map<String, Object>) params).keySet()) {
                String k = TextUtils.isEmpty(key) ? mapKey : (key + _encodeUrlBracket(mapKey));
                queryStr.append(mapToBuildString((((Map<String, Object>) params).get(mapKey)), k));
            }
        }
        return queryStr.toString();
    }

    public static String _encodeUrlBracket(String str) {
        return Uri.encode("[") + str + Uri.encode("]");
    }

    public static String fixedUrlEncode(String str) {
        String encodeStr = Uri.encode(str);
        encodeStr = encodeStr.replaceAll("!", "%" + charCodeAt16("!", 0));
        encodeStr = encodeStr.replaceAll("'", "%" + charCodeAt16("'", 0));
        encodeStr = encodeStr.replaceAll("\\(", "%" + charCodeAt16("(", 0));
        encodeStr = encodeStr.replaceAll("\\)", "%" + charCodeAt16(")", 0));
        encodeStr = encodeStr.replaceAll("\\*", "%" + charCodeAt16("*", 0));
        return encodeStr;
    }

    public static String charCodeAt16(String str, int i) {
        return Integer.toHexString(str.charAt(i)).toUpperCase();
    }

    public static String getK(Context context,String sha1,  String deviceId) {
        StringBuffer result = new StringBuffer("");
        result.append(getRandomStringArray(10));
        String str = context.getPackageName() + sha1 + deviceId;
        String strMD5 = MD5.encrypt(str, true);
        result.append(strMD5.substring(strMD5.length() - 8, strMD5.length()));
        result.append(MD5.encrypt(getRandomStringArray(8), true));
        result.append(",");
        result.append(String.valueOf(System.currentTimeMillis() / 1000));
        return result.toString();
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static boolean isPackageProcess(Context context) {
        String processName = getCurrentProcessName(context);
        return context.getPackageName().equals(processName);
    }

    private static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }
}

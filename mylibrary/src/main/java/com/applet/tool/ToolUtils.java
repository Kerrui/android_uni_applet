package com.applet.tool;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.applet.tool.virtual.SecurityCheckUtil;
import com.applet.tool.virtual.VirtualApkCheckUtil;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class ToolUtils {

    public static String getUniqueID(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !TextUtils.equals(androidId, "9774d56d682e549c")) {
            return androidId;
        }

        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static boolean isDeviceInVPN() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;

        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    public static String getOperatorStr(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simOperator = telephonyManager.getSimOperator(); // SIM运营商代码
            String simOperatorName = telephonyManager.getSimOperatorName(); // SIM运营商
            String networkOperator = telephonyManager.getNetworkOperator();// 网络运营商代码 联网才有用
            String networkOperatorName = telephonyManager.getNetworkOperatorName(); // 网络运营商
            return simOperator + "|" + simOperatorName + "|" + networkOperator + "|" + networkOperatorName;
        } catch (Exception e) {
            return "";
        }
    }

    public static JSONObject virtualInfo(Context context, JSONObject params) {
        JSONArray packageNameList = params.getJSONArray("list");
        String[] po = toStringArray(packageNameList);
        String[] both = concat(VirtualApkCheckUtil.VIRTUAL_PACKAGES, po);

        boolean isHasXposed = SecurityCheckUtil.isXposedExistByThrow();
        boolean checkFilePath = VirtualApkCheckUtil.pathCheck(context);
        boolean checkSameUid = VirtualApkCheckUtil.checkByHasSameUid(null);
        boolean checkMultiApk = VirtualApkCheckUtil.checkByMultiApkPackageName(both, null);

        JSONObject resObj = new JSONObject();
        resObj.put("has_xposed", isHasXposed ? 1 : 0);
        resObj.put("check_file_path", checkFilePath ? 1 : 0);
        resObj.put("check_same_uid", checkSameUid ? 1 : 0);
        resObj.put("check_multi_apk", checkMultiApk ? 1 : 0);
        return resObj;
    }

    private static String[] toStringArray(JSONArray array) {
        if (array == null)
            return null;

        String[] arr = new String[array.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (String) array.get(i);
        }
        return arr;
    }

    private static String[] concat(String[] a, String[] b) {
        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static void killAllProcess(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

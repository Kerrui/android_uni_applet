package com.applet.tool.virtual;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;


public class VirtualApkCheckUtil {

    private static final String TAG = "VirtualApkCheckUtil";

    public static final String[] VIRTUAL_PACKAGES = {
            "com.bly.dkplat",
            "com.by.chaos",
            "com.lbe.parallel",
            "com.excelliance.dualaid",
            "com.lody.virtual",
            "com.qihoo.magic",
            "com.dual.dualgenius",
            "com.jiubang.commerce.gomultiple",
            "com.waxmoon.ma.gp",
    };

    /**
     * 判断当前私有路径是否是标准路径
     */
    public static boolean pathCheck(Context context) {
        // 获取内部存储目录路径
        String filesDir = context.getFilesDir().getAbsolutePath();
        String packageName = context.getPackageName();
        String normalPath_one = "/data/data/" + packageName + "/files";
        String normalPath_two = "/data/user/0/" + packageName + "/files";
        // 当前存储目录路径和正常存储目录路径比对
        if (!normalPath_one.equals(filesDir) && !normalPath_two.equals(filesDir)) {
            return true;
        }
        return false;
    }

    /**
     * 运行被克隆的应用，该应用会加载多开应用的so库
     * 检测已经加载的so里是否包含这些应用的包名
     */
    public static boolean checkByMultiApkPackageName(String[] virtualPackages, VirtualCheckCallback callback) {
        BufferedReader bufr = null;
        try {
            bufr = new BufferedReader(new FileReader("/proc/self/maps"));
            String line;
            while ((line = bufr.readLine()) != null) {
                for (String pkg : virtualPackages) {
                    if (line.contains(pkg)) {
                        if (callback != null) callback.findSuspect();
                        return true;
                    }
                }
            }
        } catch (Exception ignore) {

        } finally {
            if (bufr != null) {
                try {
                    bufr.close();
                } catch (IOException e) {

                }
            }
        }
        return false;
    }

    /**
     * Android系统一个app一个uid
     * 如果同一uid下有两个进程对应的包名，在"/data/data"下有两个私有目录，则该应用被多开了
     *
     * @param callback
     * @return
     */
    public static boolean checkByHasSameUid(VirtualCheckCallback callback) {
        try {
            String filter = getUidStrFormat();
            if (TextUtils.isEmpty(filter)) return false;

            String result = CommandUtil.getSingleInstance().exec("ps");
            if (TextUtils.isEmpty(result)) return false;

            String[] lines = result.split("\n");
            if (lines == null || lines.length <= 0) return false;

            int exitDirCount = 0;

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains(filter)) {
                    int pkgStartIndex = lines[i].lastIndexOf(" ");
                    String processName = lines[i].substring(pkgStartIndex <= 0
                            ? 0 : pkgStartIndex + 1, lines[i].length());
                    File dataFile = new File(String.format("/data/data/%s", processName, Locale.CHINA));
                    if (dataFile.exists()) {
                        exitDirCount++;
                    }
                }
            }
            if (exitDirCount > 1 && callback != null) callback.findSuspect();
            return exitDirCount > 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getUidStrFormat() {
        String filter = CommandUtil.getSingleInstance().exec("cat /proc/self/cgroup");
        if (filter == null || filter.length() == 0) {
            return null;
        }

        int uidStartIndex = filter.lastIndexOf("uid");
        int uidEndIndex = filter.lastIndexOf("/pid");
        if (uidStartIndex < 0) {
            return null;
        }
        if (uidEndIndex <= 0) {
            uidEndIndex = filter.length();
        }

        filter = filter.substring(uidStartIndex + 4, uidEndIndex);
        try {
            String strUid = filter.replaceAll("\n", "");
            if (isNumber(strUid)) {
                int uid = Integer.valueOf(strUid);
                filter = String.format("u0_a%d", uid - 10000);
                return filter;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isNumber(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

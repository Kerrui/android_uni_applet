package com.applet.tool;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import androidx.annotation.NonNull;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class PermissionManager {

    private static final int PERMISSION_STATUS_GRANTED = 1;
    private static final int PERMISSION_STATUS_DENIED = 0;
    private static final int PERMISSION_STATUS_DENIED_ALWAYS = -1;

    public static void openSetting(Context context, JSONObject params) {
        String[] permissions = parsePermissions(params);
        XXPermissions.startPermissionActivity(context, permissions);
    }

    public static boolean getStatus(Context context, JSONObject params) {
        String[] permissions = parsePermissions(params);
        return permissionIsGranted(context, permissions);
    }

    public static void request(Context context, JSONObject params, UniJSCallback callback) {
        String[] permissions = parsePermissions(params);
        XXPermissions.with(context)
                .permission(permissions)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (allGranted) {
                            callback.invoke(PERMISSION_STATUS_GRANTED);
                        }
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            callback.invoke(PERMISSION_STATUS_DENIED_ALWAYS);
                        } else {
                            callback.invoke(PERMISSION_STATUS_DENIED);
                        }
                    }
                });
    }

    private static String[] parsePermissions(JSONObject params) {
        JSONArray permissionArray = params.getJSONArray("permission");
        int permissionArrLen = permissionArray.size();
        String[] permissions = new String[permissionArrLen];

        for (int i = 0; i < permissionArrLen; i++) {
            String permissionStr = permissionArray.getString(i);
            permissions[i] = permissionStr;
        }
        return permissions;
    }

    private static Boolean permissionIsGranted(Context context, String[] permissions) {
        return XXPermissions.isGranted(context, permissions);
    }
}

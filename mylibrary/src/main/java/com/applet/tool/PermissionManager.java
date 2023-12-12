package com.applet.tool;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
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

    public static JSONObject checkAll(Context context) {
        try {
            boolean noticeOpen = permissionIsGranted(context, new String[]{com.hjq.permissions.Permission.POST_NOTIFICATIONS});
            boolean cameraOpen = permissionIsGranted(context, new String[]{com.hjq.permissions.Permission.CAMERA});
            boolean microphoneOpen = permissionIsGranted(context, new String[]{com.hjq.permissions.Permission.RECORD_AUDIO});
            boolean storageOpen = permissionIsGranted(context, new String[]{Permission.READ_MEDIA_IMAGES, Permission.READ_MEDIA_VIDEO});
            boolean locationOpenPermission = permissionIsGranted(context, new String[]{Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION});
            boolean locationOpenService = isLocationServiceOpen(context);
            boolean drawOverlays = hasDrawOverlays(context);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("notice", noticeOpen);
            jsonObject.put("camera", cameraOpen);
            jsonObject.put("voice", microphoneOpen);
            jsonObject.put("storage", storageOpen);
            jsonObject.put("location", locationOpenPermission);
            jsonObject.put("locationService", locationOpenService);
            jsonObject.put("drawOverlays", drawOverlays);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static boolean isLocationServiceOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void openLocationServiceSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public static boolean hasDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
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

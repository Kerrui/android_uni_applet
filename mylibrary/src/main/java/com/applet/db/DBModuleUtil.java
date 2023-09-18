package com.applet.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class DBModuleUtil {

    public static String getDBPath(String name, String fullPath) {
        return fullPath + name + ".db";
    }

    public static JSONObject getBackObj(int status) {
        JSONObject obj = new JSONObject();
        obj.put("status", status);
        return obj;
    }

    public static JSONObject getBackObj(int status, String message) {
        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("message", message);
        return obj;
    }

    public static JSONObject getBackSelectObj(JSONArray rows) {
        JSONObject obj = new JSONObject();
        obj.put("status", 1);
        obj.put("rows", rows);
        return obj;
    }

    public static JSONObject getBackExecuteObj(JSONObject rows) {
        JSONObject obj = new JSONObject();
        obj.put("status", 1);
        obj.put("rows", rows);
        return obj;
    }
}

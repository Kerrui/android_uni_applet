package com.applet.module;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.applet.db.DBExecute;
import com.applet.db.DBModuleUtil;
import com.applet.db.DBSQLiteDatabase;

import java.io.File;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class DBModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void open(JSONObject params, UniJSCallback callback) {
        String name = params.getString("name");
        String path = params.getString("path");
        String fullPath = params.getString("fullPath");
        int enableWriteAheadLogging = params.containsKey("enableWriteAheadLogging") ? params.getInteger("enableWriteAheadLogging") : 0;

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(path) || TextUtils.isEmpty(fullPath)) {
            callback.invoke(DBModuleUtil.getBackObj(0, "Params error"));
            return;
        }

        String dbPath = DBModuleUtil.getDBPath(name, fullPath);
        File dbFile = mUniSDKInstance.getContext().getDatabasePath(dbPath);

        DBExecute.getInstance().open(dbFile, enableWriteAheadLogging, callback);
    }

    @UniJSMethod(uiThread = false)
    public boolean isOpen(JSONObject params, UniJSCallback callback) {
        return DBSQLiteDatabase.getInstance().isOpen();
    }

    @UniJSMethod(uiThread = true)
    public void close(JSONObject params, UniJSCallback callback) {
        DBExecute.getInstance().close(callback);
    }

    @UniJSMethod(uiThread = true)
    public void selectSql(JSONObject params, UniJSCallback callback) {
        String sql = params.getString("sql");
        if (TextUtils.isEmpty(sql)) {
            callback.invoke(DBModuleUtil.getBackObj(0, "select sql empty"));
            return;
        }
        DBExecute.getInstance().selectSql(sql, callback);
    }

    @UniJSMethod(uiThread = true)
    public void executeSql(JSONObject params, UniJSCallback callback) {
        String sql = params.getString("sql");
        if (TextUtils.isEmpty(sql)) {
            callback.invoke(DBModuleUtil.getBackObj(0, "execute sql empty"));
            return;
        }
        DBExecute.getInstance().executeSql(sql, callback);
    }
}

package com.applet.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class DBExecute {

    private ExecutorService mExecutorDB;

    private DBExecute() {
        if (mExecutorDB == null) {
            mExecutorDB = Executors.newFixedThreadPool(1);
        }
    }

    public static DBExecute getInstance() {
        return DBExecuteHolder.sInstance;
    }

    private static class DBExecuteHolder {
        private static final DBExecute sInstance = new DBExecute();
    }

    public void open(File dbFile, int enableWriteAheadLogging, UniJSCallback callback) {
        mExecutorDB.execute(new Runnable() {
            @Override
            public void run() {
                DBSQLiteDatabase.getInstance().open(dbFile, enableWriteAheadLogging == 1);
                callback.invoke(DBModuleUtil.getBackObj(1));
            }
        });
    }

    public void close(UniJSCallback callback) {
        mExecutorDB.execute(new Runnable() {
            @Override
            public void run() {
                DBSQLiteDatabase.getInstance().close();
                callback.invoke(DBModuleUtil.getBackObj(1));
            }
        });
    }

    public void selectSql(String sql, UniJSCallback callback) {
        mExecutorDB.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray resArr = DBSQLiteDatabase.getInstance().selectSql(sql);
                    callback.invoke(DBModuleUtil.getBackSelectObj(resArr));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.invoke(DBModuleUtil.getBackObj(0, e.getMessage()));
                }
            }
        });
    }

    public void executeSql(String sql, UniJSCallback callback) {
        mExecutorDB.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject resObj = DBSQLiteDatabase.getInstance().executeSql(sql);
                    callback.invoke(DBModuleUtil.getBackExecuteObj(resObj));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.invoke(DBModuleUtil.getBackObj(0, e.getMessage()));
                }
            }
        });
    }

    public void shutdownExecutor() {
        if (mExecutorDB == null) return;
        ThreadPoolExecutor threadPoolExecutor = ((ThreadPoolExecutor) mExecutorDB);
        int queueSize = threadPoolExecutor.getQueue().size();
        if (queueSize <= 0) {
            mExecutorDB.shutdown();
            mExecutorDB = null;
        }
    }
}

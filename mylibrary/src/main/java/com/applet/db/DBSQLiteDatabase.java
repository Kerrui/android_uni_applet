package com.applet.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class DBSQLiteDatabase {

    private static final String TAG = "DBSQLiteDatabase";

    private static final Pattern FIRST_WORD = Pattern.compile("^[\\s;]*([^\\s;]+)",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern WHERE_CLAUSE = Pattern.compile("\\s+WHERE\\s+(.+)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern UPDATE_TABLE_NAME = Pattern.compile("^\\s*UPDATE\\s+(\\S+)",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern DELETE_TABLE_NAME = Pattern.compile("^\\s*DELETE\\s+FROM\\s+(\\S+)",
            Pattern.CASE_INSENSITIVE);

    private SQLiteDatabase mDB;

    private DBSQLiteDatabase() {
    }

    private static class DBSQLiteDatabaseHolder {
        private static final DBSQLiteDatabase sInstance = new DBSQLiteDatabase();
    }

    public static DBSQLiteDatabase getInstance() {
        return DBSQLiteDatabaseHolder.sInstance;
    }

    public void open(File dbFile, Boolean enableWriteAheadLogging) {
        if (isOpen()) {
            return;
        }
        if (mDB != null) {
            close();
        }
        mDB = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        if (enableWriteAheadLogging) {
            mDB.enableWriteAheadLogging();
        }
        Log.e(TAG, "open: 'db open");
    }

    public boolean isOpen() {
        if (mDB == null) return false;
        return mDB.isOpen();
    }

    public void close() {
        if (mDB == null) return;
        mDB.close();
        mDB = null;
        Log.e(TAG, "close: 'db close");
    }

    public synchronized com.alibaba.fastjson.JSONArray selectSql(String sql) throws DBCustomException {
        Cursor cursor;
        cursor = mDB.rawQuery(sql, (String[]) null);

        com.alibaba.fastjson.JSONArray arr = new com.alibaba.fastjson.JSONArray();
        com.alibaba.fastjson.JSONObject res;
        if (cursor.moveToFirst()) {
            String[] names = cursor.getColumnNames();
            try {
                do {
                    res = new com.alibaba.fastjson.JSONObject();
                    for (int i = 0; i < names.length; i++) {
                        int type = cursor.getType(i);
                        switch (type) {
                            case Cursor.FIELD_TYPE_NULL:
                                res.put(names[i], "");
                                break;
                            case Cursor.FIELD_TYPE_INTEGER:
                            case Cursor.FIELD_TYPE_FLOAT:
                                BigDecimal decimal = new BigDecimal(String.valueOf(cursor.getDouble(i)));
                                res.put(names[i], decimal.doubleValue());
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                                res.put(names[i], cursor.getString(i));
                                break;
                            case Cursor.FIELD_TYPE_BLOB:
                                res.put(names[i], Arrays.toString(cursor.getBlob(i)));
                        }
                    }
                    arr.add(res);
                } while (cursor.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) cursor.close();
                throw new DBCustomException("plugin select sql error " + e.getMessage());
            }
        }

        if (cursor != null) cursor.close();

        return arr;
    }

    public synchronized com.alibaba.fastjson.JSONObject executeSql(String sql) throws DBCustomException {
        com.alibaba.fastjson.JSONObject res = new com.alibaba.fastjson.JSONObject();
        try {
            mDB.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBCustomException("plugin execute sql error " + e.getMessage());
        }
        return res;
    }

    public synchronized JSONArray executeSqlStatement(String sql, @NonNull JSONArray params) throws DBCustomException {
        JSONArray result = new JSONArray();
        if (mDB == null) {
            throw new DBCustomException("DB instance not initialized");
        }

        SqlType sqlType = getSqlType(sql);
        if (sqlType == SqlType.error) {
            throw new DBCustomException("sql error");
        }

        JSONObject resObj = null;
        boolean needRawQuery = true;

        if (sqlType == SqlType.update || sqlType == SqlType.delete) {
            SQLiteStatement statement;
            try {
                statement = mDB.compileStatement(sql);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DBCustomException("executeUpdateDelete compileStatement " + e.getMessage());
            }

            if (params != null) {
                try {
                    bindArgsToStatement(statement, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DBCustomException("sql params error from update or delete bind statement " + e.getMessage());
                }
            }
            int rowsAffected = -1;
            try {
                rowsAffected = statement.executeUpdateDelete();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DBCustomException("executeUpdateDelete error " + e.getMessage());
            }
            statement.close();

            try {
                if (rowsAffected != -1) {
                    resObj = new JSONObject();
                    resObj.put("rowsAffected", rowsAffected);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new DBCustomException("executeUpdateDelete error put result object " + e.getMessage());
            }
        }

        if (sqlType == SqlType.insert) {
            if (params == null) {
                throw new DBCustomException("sql params error cannot empty");
            }

            needRawQuery = false;

            SQLiteStatement statement;
            try {
                statement = mDB.compileStatement(sql);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DBCustomException("executeInsert compileStatement " + e.getMessage());
            }

            try {
                bindArgsToStatement(statement, params);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DBCustomException("sql params error from insert bind statement " + e.getMessage());
            }

            long insertId = -1;
            try {
                insertId = statement.executeInsert();
                resObj = new JSONObject();
                if (insertId != -1) {
                    resObj.put("insertId", insertId);
                    resObj.put("rowsAffected", 1);
                } else {
                    resObj.put("rowsAffected", 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new DBCustomException("executeInsert error " + e.getMessage());
            }
            statement.close();
        }

        if (needRawQuery) {
            resObj = executeSqlStatementQuery(sql, params);
        }

        try {
            if (resObj != null) {
                JSONObject r = new JSONObject();
                r.put("type", "success");
                r.put("result", resObj);
                result.put(r);
            } else {
                JSONObject r = new JSONObject();
                r.put("type", "error");

                JSONObject er = new JSONObject();
                er.put("message", "error");
                er.put("code", 0);
                r.put("result", er);
                result.put(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBCustomException("put result error " + e.getMessage());
        }

        return result;
    }

    private void bindArgsToStatement(SQLiteStatement statement, JSONArray sqlArgs) throws JSONException {
        for (int i = 0; i < sqlArgs.length(); i++) {
            if (sqlArgs.get(i) instanceof Float || sqlArgs.get(i) instanceof Double) {
                statement.bindDouble(i + 1, sqlArgs.getDouble(i));
            } else if (sqlArgs.get(i) instanceof Number) {
                statement.bindLong(i + 1, sqlArgs.getLong(i));
            } else if (sqlArgs.isNull(i)) {
                statement.bindNull(i + 1);
            } else {
                statement.bindString(i + 1, sqlArgs.getString(i));
            }
        }
    }

    private JSONObject executeSqlStatementQuery(String sql, JSONArray paramsArr) throws DBCustomException {
        JSONObject rowsResult = new JSONObject();
        Cursor cur = null;
        try {
            String[] params = null;
            params = new String[paramsArr.length()];
            for (int i = 0; i < paramsArr.length(); i++) {
                if (paramsArr.isNull(i)) {
                    params[i] = "";
                } else {
                    params[i] = paramsArr.getString(i);
                }
            }
            cur = mDB.rawQuery(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
            if (cur != null) {
                cur.close();
            }
            throw new DBCustomException("execute query parse params array " + e.getMessage());
        }

        if (cur != null && cur.moveToFirst()) {
            JSONArray rowsArrayResult = new JSONArray();
            String key = "";
            int colCount = cur.getColumnCount();

            do {
                JSONObject row = new JSONObject();
                for (int i = 0; i < colCount; i++) {
                    key = cur.getColumnName(i);
                    try {
                        bindPostHoneycomb(row, key, cur, i);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (cur != null) cur.close();
                        throw new DBCustomException("execute query bind cursor result " + e.getMessage());
                    }
                }
                rowsArrayResult.put(row);
            } while (cur.moveToNext());

            try {
                rowsResult.put("rows", rowsArrayResult);
            } catch (Exception e) {
                e.printStackTrace();
                if (cur != null) cur.close();
                throw new DBCustomException("execute query put rows " + e.getMessage());
            }
        }

        if (cur != null) cur.close();

        return rowsResult;
    }

    private void bindPostHoneycomb(JSONObject row, String key, Cursor cur, int i) throws JSONException {
        int curType = cur.getType(i);

        switch (curType) {
            case Cursor.FIELD_TYPE_NULL:
                row.put(key, JSONObject.NULL);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                row.put(key, cur.getLong(i));
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                row.put(key, cur.getDouble(i));
                break;
            case Cursor.FIELD_TYPE_STRING:
            case Cursor.FIELD_TYPE_BLOB:
            default:
                row.put(key, cur.getString(i));
                break;
        }
    }

    private SqlType getSqlType(String sql) {
        Matcher matcher = FIRST_WORD.matcher(sql);
        if (matcher.find()) {
            String first = matcher.group(1);
            if (first.length() <= 0) {
                return SqlType.error;
            }
            try {
                return SqlType.valueOf(first.toLowerCase(Locale.ENGLISH));
            } catch (Exception e) {
                return SqlType.other;
            }
        }
        return SqlType.error;
    }

    enum SqlType {
        update,
        insert,
        delete,
        select,
        begin,
        commit,
        rollback,
        other,
        error,
    }
}

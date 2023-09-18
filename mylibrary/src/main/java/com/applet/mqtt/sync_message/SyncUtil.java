package com.applet.mqtt.sync_message;

import android.text.TextUtils;

import com.applet.db.DBCustomException;
import com.applet.db.DBSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class SyncUtil {

    private static final String TAG = "SyncUtil";

    public static com.alibaba.fastjson.JSONArray putBackMessage(com.alibaba.fastjson.JSONArray oriArr, String message) {
        if (oriArr.size() >= 10) return oriArr;
        oriArr.add(message);
        return oriArr;
    }

    public static String judgeMessageTab(String tbMsgName, int chatId, int chatType) {
        String tbName = getMsgTabName(chatId, chatType, tbMsgName);
        try {
            String createSql = "CREATE TABLE IF NOT EXISTS " + tbName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, chat_id INTEGER, chat_type INTEGER, send_uid INTEGER, mtime INTEGER, ctime INTEGER, msg_type INTEGER, msg_content varchar(500), msg_status INTEGER, m_type INTEGER, remark varchar(500))";
            JSONArray createArr = DBSQLiteDatabase.getInstance().executeSqlStatement(createSql, new JSONArray());
            JSONObject createRes = parseResult(createArr);
            if (createRes == null) return "sql create " + tbName + " fail";
        } catch (DBCustomException e) {
            e.printStackTrace();
            return "sql create " + tbName + " error " + e.getMessage();
        }

        try {
            String cTimeSql = "CREATE index IF NOT EXISTS idx_ctime on " + tbName + " (chat_id, ctime, chat_type)";
            JSONArray cTimeArray = DBSQLiteDatabase.getInstance().executeSqlStatement(cTimeSql, new JSONArray());
            JSONObject cTimeRes = parseResult(cTimeArray);
            if (cTimeRes == null) return "sql " + tbName + " index ctime fail";
        } catch (DBCustomException e) {
            e.printStackTrace();
            return "sql " + tbName + " index ctime error " + e.getMessage();
        }

        try {
            String mTimeSql = "CREATE index IF NOT EXISTS idx_mtime on " + tbName + " (chat_id, mtime)";
            JSONArray mTimeArray = DBSQLiteDatabase.getInstance().executeSqlStatement(mTimeSql, new JSONArray());
            JSONObject mTimeRes = parseResult(mTimeArray);
            if (mTimeRes == null) return "sql " + tbName + " index mtime fail";
        } catch (DBCustomException e) {
            e.printStackTrace();
            return "sql " + tbName + " index mtime error " + e.getMessage();
        }

        return null;
    }

    public static String upUser(String tbUserName, com.alibaba.fastjson.JSONObject userObj, String uid, boolean onlyInsert) {
        String querySql = "SELECT uid FROM " + tbUserName + " WHERE uid = ? LIMIT 1";
        JSONArray queryParams = new JSONArray();
        queryParams.put(uid);
        JSONObject queryRes;
        try {
            JSONArray queryArray = DBSQLiteDatabase.getInstance().executeSqlStatement(querySql, queryParams);
            queryRes = parseResult(queryArray);
            if (queryRes == null) return "upUser sql select fail";
        } catch (DBCustomException e) {
            e.printStackTrace();
            return "upUser sql select error " + e.getMessage();
        }

        if (!judgeExist(queryRes)) {
            try {
                String insertSql = "INSERT INTO " + tbUserName + " (uid, nickname, avatar, sex, birthday, is_vip, is_auth, level, country_code, country_flag, language, hide_vip, hide_level, hide_profile) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                JSONArray insertArr = new JSONArray();
                insertArr.put(userObj.getInteger("uid"));
                insertArr.put(userObj.getString("nickname"));
                insertArr.put(userObj.getString("avatar"));
                insertArr.put(userObj.containsKey("sex") ? userObj.getInteger("sex") : 1);
                insertArr.put(userObj.containsKey("birthday") ? userObj.getString("birthday") : "");
                insertArr.put(userObj.containsKey("is_vip") ? userObj.getInteger("is_vip") : 0);
                insertArr.put(userObj.containsKey("is_auth") ? userObj.getInteger("is_auth") : 0);
                insertArr.put(userObj.containsKey("level") ? userObj.getInteger("level") : 0);
                insertArr.put(userObj.containsKey("country_code") ? userObj.getString("country_code") : "");
                insertArr.put(userObj.containsKey("country_flag") ? userObj.getString("country_flag") : "");
                insertArr.put(userObj.containsKey("language") ? userObj.getString("language") : "");
                insertArr.put(userObj.containsKey("hide_vip") ? userObj.getInteger("hide_vip") : 0);
                insertArr.put(userObj.containsKey("hide_level") ? userObj.getInteger("hide_level") : 0);
                insertArr.put(userObj.containsKey("hide_profile") ? userObj.getInteger("hide_profile") : 0);
                DBSQLiteDatabase.getInstance().executeSqlStatement(insertSql, insertArr);
            } catch (Exception e) {
                e.printStackTrace();
                return "upUser sql insert error " + e.getMessage();
            }
        } else {
            if (onlyInsert) return null;
            try {
                String upSql = "UPDATE " + tbUserName + " SET nickname = ?, avatar = ?, sex = ?, birthday = ?, is_vip = ?, is_auth = ?, level = ?, country_code = ?, country_flag = ?, language = ?, hide_vip = ?, hide_level = ?, hide_profile = ? WHERE uid = ?";
                JSONArray upArr = new JSONArray();
                upArr.put(userObj.getString("nickname"));
                upArr.put(userObj.getString("avatar"));
                upArr.put(userObj.containsKey("sex") ? userObj.getInteger("sex") : 1);
                upArr.put(userObj.containsKey("birthday") ? userObj.getString("birthday") : "");
                upArr.put(userObj.containsKey("is_vip") ? userObj.getInteger("is_vip") : 0);
                upArr.put(userObj.containsKey("is_auth") ? userObj.getInteger("is_auth") : 0);
                upArr.put(userObj.containsKey("level") ? userObj.getInteger("level") : 0);
                upArr.put(userObj.containsKey("country_code") ? userObj.getString("country_code") : "");
                upArr.put(userObj.containsKey("country_flag") ? userObj.getString("country_flag") : "");
                upArr.put(userObj.containsKey("language") ? userObj.getString("language") : "");
                upArr.put(userObj.containsKey("hide_vip") ? userObj.getInteger("hide_vip") : 0);
                upArr.put(userObj.containsKey("hide_level") ? userObj.getInteger("hide_level") : 0);
                upArr.put(userObj.containsKey("hide_profile") ? userObj.getInteger("hide_profile") : 0);
                upArr.put(uid);
                DBSQLiteDatabase.getInstance().executeSqlStatement(upSql, upArr);
            } catch (Exception e) {
                e.printStackTrace();
                return "upUser sql update error " + e.getMessage();
            }
        }
        return null;
    }

    public static com.alibaba.fastjson.JSONObject upMessage(String tbMsgName, com.alibaba.fastjson.JSONObject obj, int chatId, int chatType) {
        long mTime = obj.getLongValue("mtime");
        int sendUid = obj.getInteger("send_uid");
        int msgType = obj.getInteger("msg_type");
        String msgContent = obj.getString("msg_content");
        if (mTime == 0 || sendUid == 0 || chatId == 0 || chatType == 0 || msgType == 0 || TextUtils.isEmpty(msgContent)) {
            return getUpMsgResObj(0, "upMessage params data error");
        }

        JSONObject queryRes;
        String tbName = getMsgTabName(chatId, chatType, tbMsgName);
        String querySql = "SELECT * FROM " + tbName + " WHERE mtime = ? LIMIT 1";
        JSONArray queryParams = new JSONArray();
        queryParams.put(mTime);

        try {
            JSONArray queryArray = DBSQLiteDatabase.getInstance().executeSqlStatement(querySql, queryParams);
            queryRes = parseResult(queryArray);
            if (queryRes == null) return getUpMsgResObj(0,"upMessage sql select fail");
        } catch (DBCustomException e) {
            e.printStackTrace();
            return getUpMsgResObj(0, "upMessage sql select error " + e.getMessage());
        }

        try {
            if (judgeExist(queryRes)) {
                JSONArray array = queryRes.getJSONArray("rows");
                long cTime = array.getJSONObject(0).optLong("ctime");
                return getUpMsgResObj(cTime, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getUpMsgResObj(0, "upMessage json catch " + e.getMessage());
        }

        long cTime = getCtimeFromMtime(mTime);
        String insertSql = "INSERT INTO " + tbName + " (chat_id,chat_type,send_uid,mtime,ctime,msg_type,msg_content,msg_status,m_type,remark) VALUES (?,?,?,?,?,?,?,?,?,?)";
        JSONArray insertArr = new JSONArray();
        insertArr.put(chatId);
        insertArr.put(chatType);
        insertArr.put(sendUid);
        insertArr.put(mTime);
        insertArr.put(cTime);
        insertArr.put(msgType);
        insertArr.put(msgContent);
        insertArr.put(0);
        insertArr.put(obj.containsKey("m_type") ? obj.getInteger("m_type") : 0);
        insertArr.put("");

        try {
            JSONArray insertArray = DBSQLiteDatabase.getInstance().executeSqlStatement(insertSql, insertArr);
            JSONObject insertRes = parseResult(insertArray);
            if (insertRes == null) return getUpMsgResObj(0, "upMessage insert fail");
        } catch (DBCustomException e) {
            e.printStackTrace();
            return getUpMsgResObj(0, "upMessage insert error " + e.getMessage());
        }

        return getUpMsgResObj(cTime, null);
    }

    public static String upMsgIndex(String tbMsgIndexName, int chatId, int chatType, com.alibaba.fastjson.JSONObject msgObj, int unread, long cTime) {
        JSONObject queryRes;
        String querySql = "SELECT * FROM " + tbMsgIndexName + " WHERE chat_id = ? AND chat_type = ? LIMIT 1";
        JSONArray queryArr = new JSONArray();
        queryArr.put(chatId);
        queryArr.put(chatType);

        try {
            JSONArray queryArray = DBSQLiteDatabase.getInstance().executeSqlStatement(querySql, queryArr);
            queryRes = parseResult(queryArray);
            if (queryRes == null) return "upMsgIndex sql select fail";
        } catch (DBCustomException e) {
            e.printStackTrace();
            return "upMsgIndex sql select error " + e.getMessage();
        }

        int isTop = msgObj.containsKey("is_top") ? msgObj.getInteger("is_top") : 0;
        int msgNotice = msgObj.containsKey("msg_notice") ? msgObj.getInteger("msg_notice") : 1;
        if (judgeExist(queryRes)) {
            try {
                JSONArray array = queryRes.getJSONArray("rows");
                JSONObject queryObj = array.getJSONObject(0);
                String upSql = "UPDATE " + tbMsgIndexName + " SET send_uid = ?, msg_type = ?, msg_content = ?, mtime = ?, ctime = ?, unread = ?, msg_notice = ?, is_top = ?, has_at = ?, msg_status = ? WHERE chat_id = ? AND chat_type = ?";
                JSONArray upArr = new JSONArray();
                upArr.put(msgObj.getInteger("send_uid"));
                upArr.put(msgObj.getInteger("msg_type"));
                upArr.put(msgObj.getString("msg_content"));
                upArr.put(msgObj.getLongValue("mtime"));
                upArr.put(cTime);
                upArr.put(unread + queryObj.optInt("unread", 0));
                upArr.put(msgNotice);
                upArr.put(isTop);
                upArr.put(msgObj.containsKey("has_at") ? msgObj.getInteger("has_at") : 0);
                upArr.put(msgObj.containsKey("msg_status") ? msgObj.getInteger("msg_status") : 0);
                upArr.put(chatId);
                upArr.put(chatType);
                DBSQLiteDatabase.getInstance().executeSqlStatement(upSql, upArr);
            } catch (Exception e) {
                e.printStackTrace();
                return "upMsgIndex sql update error " + e.getMessage();
            }
        } else {
            try {
                String insertSql = "INSERT INTO " + tbMsgIndexName + " (chat_id, chat_type, send_uid, msg_type, msg_content, mtime, ctime, unread, is_top, msg_notice, has_at, msg_status, remark) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                JSONArray insertArr = new JSONArray();
                insertArr.put(chatId);
                insertArr.put(chatType);
                insertArr.put(msgObj.getInteger("send_uid"));
                insertArr.put(msgObj.getInteger("msg_type"));
                insertArr.put(msgObj.getString("msg_content"));
                insertArr.put(msgObj.getLongValue("mtime"));
                insertArr.put(cTime);
                insertArr.put(unread);
                insertArr.put(isTop);
                insertArr.put(msgNotice);
                insertArr.put(msgObj.containsKey("has_at") ? msgObj.getInteger("has_at") : 0);
                insertArr.put(msgObj.containsKey("msg_status") ? msgObj.getInteger("msg_status") : 0);
                insertArr.put("");
                DBSQLiteDatabase.getInstance().executeSqlStatement(insertSql, insertArr);
            } catch (Exception e) {
                e.printStackTrace();
                return "upMsgIndex sql insert error " + e.getMessage();
            }
        }

        return null;
    }

    public static String upMsgIndexContent(String tbMsgIndexName, com.alibaba.fastjson.JSONObject indexObj) {
        int chatId = indexObj.containsKey("chat_id") ? indexObj.getInteger("chat_id") : 0;
        String msgContent = indexObj.containsKey("msg_content") ? indexObj.getString("msg_content") : "";
        if (chatId == 0 || TextUtils.isEmpty(msgContent)) {
            return "upMsgIndexContent params data error";
        }

        try {
            String upSql = "UPDATE " + tbMsgIndexName + " SET msg_content = ? WHERE chat_id = ? AND chat_type = ?";
            JSONArray upParams = new JSONArray();
            upParams.put(msgContent);
            upParams.put(chatId);
            upParams.put(1);
            DBSQLiteDatabase.getInstance().executeSqlStatement(upSql, upParams);
        } catch (Exception e) {
            e.printStackTrace();
            return "upMsgIndexContent sql update error " + e.getMessage();
        }

        return null;
    }

    private static String getMsgTabName(int chatId, int chatType, String tbMsgName) {
        if (chatType == 2) {
            return tbMsgName + chatId;
        }
        if (chatId == 10000000) {
            return tbMsgName + chatId;
        }
        int lastDigit = chatId % 10;
        return tbMsgName + lastDigit;
    }

    private static boolean judgeExist(JSONObject result) {
        return !result.isNull("rows");
    }

    private static JSONObject parseResult(JSONArray result) {
        try {
            JSONObject jsonObject = result.getJSONObject(0);
            String type = jsonObject.getString("type");
            if (type.equals("success")) {
                return jsonObject.getJSONObject("result");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static com.alibaba.fastjson.JSONObject getUpMsgResObj(long cTime, String message) {
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        result.put("cTime", cTime);
        result.put("message", message);
        return result;
    }

    private static long getCtimeFromMtime(long mTime) {
        BigDecimal leftTime = new BigDecimal(mTime);
        BigDecimal rightTime = new BigDecimal(1000);
        BigDecimal divide = leftTime.divide(rightTime, 0, BigDecimal.ROUND_HALF_UP);
        return divide.longValue();
    }
}

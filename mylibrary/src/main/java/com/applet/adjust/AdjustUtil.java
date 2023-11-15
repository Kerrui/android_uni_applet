package com.applet.adjust;

import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustEventFailure;
import com.adjust.sdk.AdjustEventSuccess;
import com.adjust.sdk.AdjustSessionFailure;
import com.adjust.sdk.AdjustSessionSuccess;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdjustUtil {

    public static final String KEY_MESSAGE = "message";
    public static final String KEY_JSON_RESPONSE = "jsonResponse";
    public static final String KEY_ADID = "adid";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_WILL_RETRY = "willRetry";
    public static final String KEY_EVENT_TOKEN = "eventToken";
    public static final String KEY_CALLBACK_ID = "callbackId";
    public static final String KEY_TRACKER_TOKEN = "trackerToken";
    public static final String KEY_TRACKER_NAME = "trackerName";
    public static final String KEY_NETWORK = "network";
    public static final String KEY_CAMPAIGN = "campaign";
    public static final String KEY_ADGROUP = "adgroup";
    public static final String KEY_CREATIVE = "creative";
    public static final String KEY_CLICK_LABEL = "clickLabel";
    public static final String KEY_COST_TYPE = "costType";
    public static final String KEY_COST_AMOUNT = "costAmount";
    public static final String KEY_COST_CURRENCY = "costCurrency";

    public static JSONObject getSessionSuccessObj(AdjustSessionSuccess session) {
        JSONObject res = new JSONObject();
        JSONObject obj = new JSONObject(getSessionSuccessMap(session));
        res.put("key", "session_success");
        res.put("data", obj);
        return res;
    }

    public static JSONObject getSessionFailObj(AdjustSessionFailure session) {
        JSONObject res = new JSONObject();
        JSONObject obj = new JSONObject(getSessionFailureMap(session));
        res.put("key", "session_fail");
        res.put("data", obj);
        return res;
    }

    public static JSONObject getEventSuccessObj(AdjustEventSuccess event) {
        JSONObject res = new JSONObject();
        JSONObject obj = new JSONObject(getEventSuccessMap(event));
        res.put("key", "event_success");
        res.put("data", obj);
        return res;
    }

    public static JSONObject getEventFailObj(AdjustEventFailure event) {
        JSONObject res = new JSONObject();
        JSONObject obj = new JSONObject(getEventFailureMap(event));
        res.put("key", "event_fail");
        res.put("data", obj);
        return res;
    }

    public static JSONObject getAttributionObj(AdjustAttribution adjustAttribution) {
        JSONObject res = new JSONObject();
        JSONObject obj = new JSONObject(getAttributionMap(adjustAttribution));
        res.put("key", "event_attribution");
        res.put("data", obj);
        return res;
    }

    public static double parseMoney(JSONObject entry) {
        if (!entry.containsKey("money")) return 0;
        double valueDouble;
        Object money = entry.get("money");
        if (money instanceof String) {
            valueDouble = Double.parseDouble((String) money);
        } else if (money instanceof Float) {
            valueDouble = ((Float) money).doubleValue();
        } else if (money instanceof Double) {
            valueDouble = (Double) money;
        } else if (money instanceof Integer) {
            valueDouble = Double.parseDouble(String.valueOf(money));
        } else {
            return 0;
        }
        return valueDouble;
    }

    private static Map<String, Object> getSessionSuccessMap(AdjustSessionSuccess session) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (session == null) return map;
        map.put(KEY_MESSAGE, session.message);
        map.put(KEY_TIMESTAMP, session.timestamp);
        map.put(KEY_ADID, session.adid);
        map.put(KEY_JSON_RESPONSE, parseJsonStr(session.jsonResponse));
        return map;
    }

    private static Map<String, Object> getSessionFailureMap(AdjustSessionFailure session) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_MESSAGE, session.message);
        map.put(KEY_TIMESTAMP, session.timestamp);
        map.put(KEY_ADID, session.adid);
        map.put(KEY_WILL_RETRY, session.willRetry);
        map.put(KEY_JSON_RESPONSE, parseJsonStr(session.jsonResponse));
        return map;
    }

    private static Map<String, Object> getEventSuccessMap(AdjustEventSuccess event) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_MESSAGE, event.message);
        map.put(KEY_TIMESTAMP, event.timestamp);
        map.put(KEY_ADID, event.adid);
        map.put(KEY_EVENT_TOKEN, event.eventToken);
        map.put(KEY_CALLBACK_ID, event.callbackId);
        map.put(KEY_JSON_RESPONSE, parseJsonStr(event.jsonResponse));
        return map;
    }

    private static Map<String, Object> getEventFailureMap(AdjustEventFailure event) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_MESSAGE, event.message);
        map.put(KEY_TIMESTAMP, event.timestamp);
        map.put(KEY_ADID, event.adid);
        map.put(KEY_EVENT_TOKEN, event.eventToken);
        map.put(KEY_CALLBACK_ID, event.callbackId);
        map.put(KEY_WILL_RETRY, event.willRetry);
        map.put(KEY_JSON_RESPONSE, parseJsonStr(event.jsonResponse));
        return map;
    }

    public static Map<String, Object> getAttributionMap(AdjustAttribution attribution) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_TRACKER_TOKEN, attribution.trackerToken);
        map.put(KEY_TRACKER_NAME, attribution.trackerName);
        map.put(KEY_NETWORK, attribution.network);
        map.put(KEY_CAMPAIGN, attribution.campaign);
        map.put(KEY_ADGROUP, attribution.adgroup);
        map.put(KEY_CREATIVE, attribution.creative);
        map.put(KEY_CLICK_LABEL, attribution.clickLabel);
        map.put(KEY_ADID, attribution.adid);
        map.put(KEY_COST_TYPE, attribution.costType);
        map.put(KEY_COST_AMOUNT, attribution.costAmount);
        map.put(KEY_COST_CURRENCY, attribution.costCurrency);
        return map;
    }

    private static String parseJsonStr(org.json.JSONObject jsonObject) {
        return jsonObject == null ? "" : jsonObject.toString();
    }
}

package com.applet.mqtt.sync_message;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class SyncExecute {

    private ExecutorService mExecutorMsg;

    public SyncExecute() {
    }

    private static class SyncExecuteHolder {
        private static final SyncExecute sInstance = new SyncExecute();
    }

    public static SyncExecute getInstance() {
        return SyncExecuteHolder.sInstance;
    }

    public void syncMessage(JSONObject params, UniJSCallback callback) {
        initExecutorMsg();

        mExecutorMsg.execute(new Runnable() {
            @Override
            public void run() {
                int status = 1;
                JSONObject resultObj = new JSONObject();
                JSONArray catchMsgArr = new JSONArray();

                String lastSyncMTime = params.getString("last_sync_mtime");
                JSONArray chatArr = params.getJSONArray("list");
                TbName tbName;
                if (params.containsKey("tbName")) {
                    tbName = params.getObject("tbName", TbName.class);
                } else {
                    tbName = new TbName();
                }

                resultObj.put("last_sync_mtime", lastSyncMTime);

                if (TextUtils.isEmpty(lastSyncMTime) || chatArr == null) {
                    status = 0;
                    resultObj.put("status", status);
                    catchMsgArr = SyncUtil.putBackMessage(catchMsgArr, "get params error");
                    resultObj.put("msgList", catchMsgArr);
                    callback.invoke(resultObj);
                    closeExecutorMsg();
                    return;
                }

                int listArrLength = chatArr.size();
                for (int i = 0; i < listArrLength; i++) {
                    JSONObject itemObj = chatArr.getJSONObject(i);
                    String syncMsgResult = executeSyncMessage(itemObj, tbName);
                    if (syncMsgResult != null) {
                        catchMsgArr = SyncUtil.putBackMessage(catchMsgArr, syncMsgResult);
                    }
                }

                if (params.containsKey("income_coins_list")) {
                    JSONArray incomeCoinsList = params.getJSONArray("income_coins_list");
                    int incomeCoinsListLen = incomeCoinsList.size();

                    if (incomeCoinsListLen > 0) {
                        for (int i = 0; i < incomeCoinsList.size(); i++) {
                            String upMsgIndexErrorMessage = SyncUtil.upMsgIndexContent(tbName.msgIndex, incomeCoinsList.getJSONObject(i));
                            if (upMsgIndexErrorMessage != null) {
                                catchMsgArr = SyncUtil.putBackMessage(catchMsgArr, upMsgIndexErrorMessage);
                            }
                        }
                    }
                }

                resultObj.put("status", status);
                resultObj.put("message", catchMsgArr);
                callback.invoke(resultObj);
                closeExecutorMsg();
            }
        });
    }

    private String executeSyncMessage(JSONObject dataObj, TbName tbName) {
        int chatId = dataObj.getInteger("chat_id");
        int chatType = dataObj.getInteger("chat_type");
        int unread = dataObj.getInteger("unread");

        String tbMsgCreateRes = SyncUtil.judgeMessageTab(tbName.msg, chatId, chatType);
        if (tbMsgCreateRes != null) {
            return tbMsgCreateRes;
        }

        String resultStr = "";
        JSONObject userObj = dataObj.getJSONObject("user_infos");
        if (userObj != null) {
            String upUserErrorMessage = null;
            for (Map.Entry<String, Object> entry : userObj.entrySet()) {
                JSONObject user = (JSONObject) entry.getValue();
                String upUserResult = SyncUtil.upUser(tbName.user, user, entry.getKey(), false);
                if (upUserResult != null) {
                    upUserErrorMessage = upUserResult;
                }
            }
            if (upUserErrorMessage != null) {
                resultStr = resultStr + " [-] " + upUserErrorMessage;
            }
        }

        JSONArray listArr = dataObj.getJSONArray("list");
        if (listArr == null) {
            if (!TextUtils.isEmpty(resultStr)) {
                return resultStr;
            } else {
                return null;
            }
        }

        int listLength = listArr.size();
        if (listLength <= 0) {
            if (!TextUtils.isEmpty(resultStr)) {
                return resultStr;
            } else {
                return null;
            }
        }

        long indexCTime = 0;
        JSONObject indexObj = null;
        String upMessageErrorMessage = null;
        for (int i = 0; i < listLength; i++) {
            JSONObject msgObj = listArr.getJSONObject(i);
            JSONObject upMessageObj = SyncUtil.upMessage(tbName.msg, msgObj, chatId, chatType);
            String upResMessage = upMessageObj.getString("message");
            long cTime = upMessageObj.getLongValue("cTime");
            if (upResMessage != null) {
                upMessageErrorMessage = upResMessage;
            }
            if (cTime != 0 && cTime > indexCTime) {
                indexCTime = cTime;
                indexObj = msgObj;
            }
        }
        if (upMessageErrorMessage != null) {
            resultStr = resultStr + " [-] " + upMessageErrorMessage;
        }

        if (indexCTime > 0 && indexObj != null) {
            String upMsgIndexErrorMessage = SyncUtil.upMsgIndex(tbName.msgIndex, chatId, chatType, indexObj, unread, indexCTime);
            if (upMsgIndexErrorMessage != null) {
                resultStr = resultStr + " [-] " + upMsgIndexErrorMessage;
            }
        }

        return TextUtils.isEmpty(resultStr) ? null : resultStr;
    }

    private void initExecutorMsg() {
        if (mExecutorMsg == null) {
            mExecutorMsg = Executors.newFixedThreadPool(1);
        }
    }

    private void closeExecutorMsg() {
        if (mExecutorMsg == null) return;
        ThreadPoolExecutor threadPoolExecutor = ((ThreadPoolExecutor) mExecutorMsg);
        int queueSize = threadPoolExecutor.getQueue().size();
        if (queueSize <= 0) {
            mExecutorMsg.shutdown();
            mExecutorMsg = null;
        }
    }
}

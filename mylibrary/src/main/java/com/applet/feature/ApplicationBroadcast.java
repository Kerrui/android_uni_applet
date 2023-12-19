package com.applet.feature;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.applet.feature.util.LogUtil;

public class ApplicationBroadcast extends BroadcastReceiver {

    private final ApplicationBroadcastListener mApplicationBroadcastListener;

    public ApplicationBroadcast(ApplicationBroadcastListener applicationBroadcastListener) {
        mApplicationBroadcastListener = applicationBroadcastListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            int type = intent.getIntExtra("type", 0);
            LogUtil.t("ApplicationBroadcastReceiver type = " + type);
            if (type == 0) return;
            JSONObject actionObj = new JSONObject();
            actionObj.put("type", type);
            if (mApplicationBroadcastListener != null) {
                mApplicationBroadcastListener.onAction(actionObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ApplicationBroadcastListener {
        void onAction(JSONObject actionObj);
    }
}

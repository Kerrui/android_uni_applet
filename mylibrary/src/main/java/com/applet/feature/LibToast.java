package com.applet.feature;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

public class LibToast {

    private static final String TAG = "LibToast";

    public LibToast() {
    }

    private static class LibToastHolder {
        private static final LibToast sInstance = new LibToast();
    }

    public static LibToast getInstance() {
        return LibToastHolder.sInstance;
    }

    public void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public void helloLib() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("isCover", 1);
        jsonObject.put("msg", "hello message");
        Log.e(TAG, "helloLib: '-------> " + jsonObject.toJSONString());
    }
}

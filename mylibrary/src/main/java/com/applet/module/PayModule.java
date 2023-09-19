package com.applet.module;

import com.alibaba.fastjson.JSONObject;
import com.android.billingclient.api.BillingClient;
import com.applet.pay.GoogleBilling;
import com.applet.pay.OnBillingListener;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class PayModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void executePay(JSONObject params, UniJSCallback callback) {
        GoogleBilling.getInstance().setBillingListener(new OnBillingListener() {
            @Override
            public void onFail(int appCode, int googleCode, boolean isSupportV5, String message) {
                callback.invokeAndKeepAlive(jsFailObj(appCode, googleCode, isSupportV5, message));
            }

            @Override
            public void onSuccess(String originalJson, boolean isSupportV5) {
                callback.invokeAndKeepAlive(jsSuccessObj(originalJson, isSupportV5));
            }

            @Override
            public String productType() {
                return params.getInteger("product_type") == 1 ? BillingClient.ProductType.INAPP : BillingClient.ProductType.SUBS;
            }
        });
        GoogleBilling.getInstance().pay(mUniSDKInstance.getContext(), params);
    }

    @UniJSMethod(uiThread = true)
    public void executeQueryPur(JSONObject params, UniJSCallback callback) {
        GoogleBilling.getInstance().setBillingListener(new OnBillingListener() {
            @Override
            public void onFail(int appCode, int googleCode, boolean isSupportV5, String message) {
            }

            @Override
            public void onSuccess(String originalJson, boolean isSupportV5) {
                callback.invokeAndKeepAlive(jsSuccessObj(originalJson, isSupportV5));
            }

            @Override
            public String productType() {
                return "";
            }
        });
        GoogleBilling.getInstance().check(mUniSDKInstance.getContext());
    }

    private JSONObject jsFailObj(int appCode, int googleCode, boolean isSupportV5, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        jsonObject.put("appCode", appCode);
        jsonObject.put("googleCode", googleCode);
        jsonObject.put("supportV5", isSupportV5 ? 1 : 0);
        jsonObject.put("message", message);
        return jsonObject;
    }

    private JSONObject jsSuccessObj(String originalJson, boolean isSupportV5) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("data", originalJson);
        jsonObject.put("supportV5", isSupportV5 ? 1 : 0);
        return jsonObject;
    }
}

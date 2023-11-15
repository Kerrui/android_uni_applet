package com.applet.module;

import android.text.TextUtils;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.AdjustEventFailure;
import com.adjust.sdk.AdjustEventSuccess;
import com.adjust.sdk.AdjustSessionFailure;
import com.adjust.sdk.AdjustSessionSuccess;
import com.adjust.sdk.LogLevel;
import com.adjust.sdk.OnAttributionChangedListener;
import com.adjust.sdk.OnDeviceIdsRead;
import com.adjust.sdk.OnEventTrackingFailedListener;
import com.adjust.sdk.OnEventTrackingSucceededListener;
import com.adjust.sdk.OnSessionTrackingFailedListener;
import com.adjust.sdk.OnSessionTrackingSucceededListener;
import com.alibaba.fastjson.JSONObject;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.applet.adjust.AdjustUtil;
import com.applet.feature.util.LogUtil;

import java.util.Map;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

/**
 * Created by Alien-super on 2023/11/16.
 */
public class AdjustModule extends UniModule {

    private static final String ENV = AdjustConfig.ENVIRONMENT_PRODUCTION;

    @UniJSMethod(uiThread = true)
    public void getInstallReferrer(JSONObject params, UniJSCallback callback) {
        final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(mUniSDKInstance.getContext()).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                LogUtil.t("onInstallReferrerSetupFinished: '--------> " + responseCode);
                JSONObject res = new JSONObject();
                int status;
                String content;
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        try {
                            status = 1;
                            ReferrerDetails referrerDetails = referrerClient.getInstallReferrer();
                            content = referrerDetails.getInstallReferrer();
                        } catch (Exception e) {
                            e.printStackTrace();
                            status = 0;
                            content = "getInstallReferrer catch " + e.getMessage();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        status = 0;
                        content = "Could not initiate connection to the Install Referrer service";
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        status = 0;
                        content = "Install Referrer API not supported by the installed Play Store app";
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                        status = 0;
                        content = "General errors caused by incorrect usage";
                        break;
                    default:
                        status = 0;
                        content = "other error response code = " + responseCode;
                        break;
                }
                LogUtil.t("onInstallReferrerSetupFinished: 'content = " + content);
                res.put("status", status);
                res.put("content", content);
                callback.invoke(res);
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                LogUtil.t("onInstallReferrerServiceDisconnected: '-------------------- ");
                JSONObject res = new JSONObject();
                res.put("status", 0);
                res.put("content", "onInstallReferrerServiceDisconnected");
                callback.invoke(res);
            }
        });
    }

    @UniJSMethod(uiThread = true)
    public void init(JSONObject params, UniJSCallback callback) {
        LogUtil.t("init: '-----> " + params);
        String appToken = params.getString("appToken");
        boolean sessionBack = params.getBoolean("sessionBack");
        boolean eventBack = params.getBoolean("eventBack");

        AdjustConfig config = new AdjustConfig(mUniSDKInstance.getContext(), appToken, ENV);
        config.setLogLevel(LogLevel.INFO);
        config.setNeedsCost(true);

        if (sessionBack) {
            config.setOnSessionTrackingSucceededListener(new OnSessionTrackingSucceededListener() {
                @Override
                public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess sessionSuccessResponseData) {
                    if (sessionSuccessResponseData == null) {
                        LogUtil.t("onFinishedSessionTrackingSucceeded: '---> null");
                    } else {
                        LogUtil.t("onFinishedSessionTrackingSucceeded: '---> " + sessionSuccessResponseData.toString());
                        callback.invokeAndKeepAlive(AdjustUtil.getSessionSuccessObj(sessionSuccessResponseData));
                    }
                }
            });

            config.setOnSessionTrackingFailedListener(new OnSessionTrackingFailedListener() {
                @Override
                public void onFinishedSessionTrackingFailed(AdjustSessionFailure failureResponseData) {
                    if (failureResponseData == null) {
                        LogUtil.t("onFinishedSessionTrackingFailed: '---> null");
                    } else {
                        LogUtil.t("onFinishedSessionTrackingFailed: '---> " + failureResponseData.toString());
                        callback.invokeAndKeepAlive(AdjustUtil.getSessionFailObj(failureResponseData));
                    }
                }
            });
        }

        if (eventBack) {
            config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
                @Override
                public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
                    if (eventSuccessResponseData == null) {
                        LogUtil.t("onFinishedEventTrackingSucceeded: '---> null");
                    } else {
                        LogUtil.t("onFinishedEventTrackingSucceeded: '---> " + eventSuccessResponseData.toString());
                        callback.invokeAndKeepAlive(AdjustUtil.getEventSuccessObj(eventSuccessResponseData));
                    }
                }
            });

            config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
                @Override
                public void onFinishedEventTrackingFailed(AdjustEventFailure eventFailureResponseData) {
                    if (eventFailureResponseData == null) {
                        LogUtil.t("onFinishedEventTrackingFailed: '---> null");
                    } else {
                        LogUtil.t("onFinishedEventTrackingFailed: '---> " + eventFailureResponseData.toString());
                        callback.invokeAndKeepAlive(AdjustUtil.getEventFailObj(eventFailureResponseData));
                    }
                }
            });
        }

        config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
            @Override
            public void onAttributionChanged(AdjustAttribution attribution) {
                if (attribution == null) {
                    LogUtil.t("onAttributionChanged: ----> null");
                } else {
                    LogUtil.t("onAttributionChanged: ----> " + attribution.toString());
                    callback.invokeAndKeepAlive(AdjustUtil.getAttributionObj(attribution));
                }
            }
        });

        Adjust.onCreate(config);
        Adjust.onResume();
    }

    @UniJSMethod(uiThread = true)
    public void event(JSONObject params, UniJSCallback callback) {
        String token = params.getString("token");
        if (TextUtils.isEmpty(token)) {
            callback.invoke(false);
            return;
        }
        AdjustEvent adjustEvent = new AdjustEvent(token);

        double money = AdjustUtil.parseMoney(params);
        if (money > 0) {
            String moneyUnit = params.containsKey("moneyUnit") ? params.getString("moneyUnit") : "USD";
            LogUtil.t("event: '----> setRevenue money = " + money + " " + moneyUnit);
            adjustEvent.setRevenue(money, moneyUnit);
        }

        String orderId = params.containsKey("orderId") ? params.getString("orderId") : "";
        if (!TextUtils.isEmpty(orderId)) {
            LogUtil.t("event: '----> setOrderId orderId = " + orderId);
            adjustEvent.setOrderId("orderId2");
        }

        JSONObject callbackParams = params.containsKey("callbackParams") ? params.getJSONObject("callbackParams") : null;
        if (callbackParams != null) {
            for (Map.Entry<String, Object> entry : callbackParams.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                LogUtil.t("event: '----> addCallbackParameter key = " + key + " value = " + value);
                adjustEvent.addCallbackParameter(key, value);
            }
        }

        JSONObject partnerParams = params.containsKey("partnerParams") ? params.getJSONObject("partnerParams") : null;
        if (partnerParams != null) {
            for (Map.Entry<String, Object> entry : partnerParams.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                LogUtil.t("event: '----> addPartnerParameter key = " + key + " value = " + value);
                adjustEvent.addPartnerParameter(key, value);
            }
        }

        String callbackId = params.containsKey("callbackId") ? params.getString("callbackId") : "";
        if (!TextUtils.isEmpty(callbackId)) {
            LogUtil.t("event: '----> setCallbackId callbackId = " + callbackId);
            adjustEvent.setCallbackId(callbackId);
        }

        Adjust.trackEvent(adjustEvent);
        callback.invoke(true);
        LogUtil.t("event: '-----> set event end ...");
    }

    @UniJSMethod(uiThread = false)
    public JSONObject getAttribution(JSONObject params, UniJSCallback callback) {
        AdjustAttribution attribution = Adjust.getAttribution();
        if (attribution == null) return null;
        return new JSONObject(AdjustUtil.getAttributionMap(attribution));
    }

    @UniJSMethod(uiThread = false)
    public String getAdId(JSONObject params, UniJSCallback callback) {
        String adId = Adjust.getAdid();
        return adId == null ? "" : adId;
    }

    @UniJSMethod(uiThread = false)
    public String getAmazonId(JSONObject params, UniJSCallback callback) {
        String amazonId = Adjust.getAmazonAdId(mUniSDKInstance.getContext());
        return amazonId == null ? "" : amazonId;
    }

    @UniJSMethod(uiThread = true)
    public void getGoogleAdId(JSONObject params, UniJSCallback callback) {
        Adjust.getGoogleAdId(mUniSDKInstance.getContext(), new OnDeviceIdsRead() {
            @Override
            public void onGoogleAdIdRead(String googleAdId) {
                callback.invoke(googleAdId == null ? "" : googleAdId);
            }
        });
    }
}

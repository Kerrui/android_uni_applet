package com.applet.module;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.applet.feature.util.LogUtil;
import com.applet.library.IMqttServiceAidlInterface;
import com.applet.mqtt.Mqtt;
import com.applet.mqtt.MqttClientService;
import com.applet.mqtt.MqttInfo;
import com.applet.mqtt.PayloadBean;
import com.applet.mqtt.sync_message.SyncExecute;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

import androidx.annotation.NonNull;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class MqttModule extends UniModule {

    private static final String TAG = "MqttModule";

    private UniJSCallback mCallback;
    private boolean isRegisterMqttBroadcastReceiver = false;
    private IMqttServiceAidlInterface mIMqttServiceAidlInterface;
    private MqttBroadcastReceiver mMqttBroadcastReceiver;

    private class MqttBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                int type = intent.getIntExtra("type", 0);
                if (type == 1) {
                    fireConnectStatus(intent.getIntExtra("status", 0));
                } else if (type == 2) {
                    String message = intent.getStringExtra("message");
                    PayloadBean bean = JSON.parseObject(message, PayloadBean.class);
                    fireMessage(bean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIMqttServiceAidlInterface = IMqttServiceAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @UniJSMethod(uiThread = true)
    public void connect(JSONObject params, UniJSCallback callback) {
        mCallback = callback;
        if (callback == null) return;
        MqttInfo mqttInfo = Mqtt.getInstance().getMqttInfo(params);
        if (mqttInfo == null) {
            callback.invokeAndKeepAlive(getBackObj(new PayloadBean(Mqtt.ERROR_CODE, "params error")));
            return;
        }

        if (!isRegisterMqttBroadcastReceiver) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Mqtt.INTENT_ACTION_EVENT_BUS);
            mMqttBroadcastReceiver = new MqttBroadcastReceiver();
            mUniSDKInstance.getContext().registerReceiver(mMqttBroadcastReceiver, intentFilter);
            isRegisterMqttBroadcastReceiver = true;
        }

        if (mIMqttServiceAidlInterface == null) {
            Intent intent = new Intent(mUniSDKInstance.getContext(), MqttClientService.class);
            mUniSDKInstance.getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            LogUtil.t("mqtt connect: '-------> start execute");
        }

        Intent intent = new Intent(mUniSDKInstance.getContext(), MqttClientService.class);
        intent.setAction(Mqtt.MQTT_ACTION_CONNECT);
        intent.putExtra("mqtt_info", mqttInfo);
        mUniSDKInstance.getContext().startService(intent);
    }

    @UniJSMethod(uiThread = false)
    public void disConnect(JSONObject params, UniJSCallback callback) {
        Intent intent = new Intent(mUniSDKInstance.getContext(), MqttClientService.class);
        intent.setAction(Mqtt.MQTT_ACTION_DISCONNECT);
        mUniSDKInstance.getContext().startService(intent);
    }

    @UniJSMethod(uiThread = false)
    public boolean checkStatusSync() {
        boolean status = false;
        if (mIMqttServiceAidlInterface != null) {
            try {
                status = mIMqttServiceAidlInterface.isConnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    @UniJSMethod(uiThread = true)
    public void syncMessage(JSONObject params, UniJSCallback callback) {
        SyncExecute.getInstance().syncMessage(params, callback);
    }

    @UniJSMethod(uiThread = true)
    public void getFirebaseMessageToken(JSONObject params, UniJSCallback callback) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    LogUtil.e("Fetching FCM registration token failed ", task.getException());
                    callback.invokeAndKeepAlive("");
                    return;
                }

                String token = task.getResult();
                LogUtil.t("getFirebaseMessageToken result = " + token);
                callback.invokeAndKeepAlive(token);
            }
        });
        createNotificationChannel();
    }

    @UniJSMethod(uiThread = true)
    public void fireEvent(JSONObject params, UniJSCallback callback) {
        try {
            String eventName = params.getString("event");
            if (TextUtils.isEmpty(eventName)) {
                callback.invoke(false);
                return;
            }
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mUniSDKInstance.getContext());
            JSONObject paramsObj = params.getJSONObject("params");

            Bundle bundle = new Bundle();
            for (Map.Entry<String, Object> entry : paramsObj.entrySet()) {
                System.out.println(entry.getKey() + entry.getValue());
                String key = entry.getKey();
                if (key.equals("value")) {
                    double valueDouble;
                    if (entry.getValue() instanceof String) {
                        valueDouble = Double.parseDouble((String) entry.getValue());
                    } else if (entry.getValue() instanceof Float) {
                        valueDouble = ((Float) entry.getValue()).doubleValue();
                    } else if (entry.getValue() instanceof Double) {
                        valueDouble = (Double) entry.getValue();
                    } else if (entry.getValue() instanceof Integer) {
                        valueDouble = Double.parseDouble(String.valueOf(entry.getValue()));
                    } else {
                        return;
                    }
                    bundle.putDouble(FirebaseAnalytics.Param.VALUE, valueDouble);
                } else {
                    if (entry.getValue() instanceof Integer) {
                        int value = (int) entry.getValue();
                        bundle.putInt(key, value);
                    } else if (entry.getValue() instanceof String) {
                        String value = (String) entry.getValue();
                        bundle.putString(key, value);
                    } else if (entry.getValue() instanceof Float) {
                        Float value = (Float) entry.getValue();
                        bundle.putFloat(key, value);
                    } else if (entry.getValue() instanceof Double) {
                        Double value = (Double) entry.getValue();
                        bundle.putDouble(key, value);
                    } else {
                        callback.invoke(false);
                        return;
                    }
                }
            }
            firebaseAnalytics.logEvent(eventName, bundle);
            callback.invoke(true);
        } catch (Exception e) {
            e.printStackTrace();
            callback.invoke(false);
        }
    }

    @Override
    public void onActivityDestroy() {
        Intent intent = new Intent(mUniSDKInstance.getContext(), MqttClientService.class);
        mUniSDKInstance.getContext().stopService(intent);
        mUniSDKInstance.getContext().unbindService(serviceConnection);
        mUniSDKInstance.getContext().unregisterReceiver(mMqttBroadcastReceiver);
        super.onActivityDestroy();
    }

    private static JSONObject getBackObj(PayloadBean bean) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res", bean);
        return jsonObject;
    }

    private void fireConnectStatus(int status) {
        if (mCallback == null) return;
        mCallback.invokeAndKeepAlive(getBackObj(new PayloadBean(status)));
    }

    private void fireMessage(PayloadBean payloadBean) {
        if (mCallback == null) return;
        mCallback.invokeAndKeepAlive(getBackObj(payloadBean));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            String description = "Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mUniSDKInstance.getContext().getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

            String channelId = "Message";
            String channelName = "Message";
            int channelImp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel msgChannel = new NotificationChannel(channelId, channelName, channelImp);
            NotificationManager notificationMsg = mUniSDKInstance.getContext().getSystemService(NotificationManager.class);
            assert notificationMsg != null;
            notificationMsg.createNotificationChannel(msgChannel);
        }
    }
}

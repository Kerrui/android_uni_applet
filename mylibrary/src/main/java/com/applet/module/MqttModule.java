package com.applet.module;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.applet.feature.util.LogUtil;
import com.applet.library.IMqttServiceAidlInterface;
import com.applet.mqtt.Mqtt;
import com.applet.mqtt.MqttClientService;
import com.applet.mqtt.MqttInfo;
import com.applet.mqtt.PayloadBean;
import com.applet.mqtt.sync_message.SyncExecute;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class MqttModule extends UniModule {

    private static final String TAG = "MqttModule";

    private UniJSCallback mCallback;
    private boolean isRegisterMqttBroadcastReceiver = false;
    private IMqttServiceAidlInterface mIMqttServiceAidlInterface;

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
            mUniSDKInstance.getContext().registerReceiver(new MqttBroadcastReceiver(), intentFilter);
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

    @Override
    public void onActivityDestroy() {
        Intent intent = new Intent(mUniSDKInstance.getContext(), MqttClientService.class);
        mUniSDKInstance.getContext().stopService(intent);
        mUniSDKInstance.getContext().unbindService(serviceConnection);
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
}

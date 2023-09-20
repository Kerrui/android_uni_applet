package com.applet.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;


public class Mqtt {

    private static final String TAG = "Mqtt_Log";

    public Mqtt() {
    }

    private static class MqttHolder {
        private static final Mqtt sInstance = new Mqtt();
    }

    public static Mqtt getInstance() {
        return MqttHolder.sInstance;
    }

    public static final int ERROR_CODE = 10000;
    public static final int CONNECT_STATUS_SUCCESS = 100;
    public static final int CONNECT_STATUS_AGAIN = 101;
    public static final int CONNECT_STATUS_LOST = 102;
    public static final int CONNECT_STATUS_FAIL = 200;

    public static final String MQTT_ACTION_CONNECT = "connect";
    public static final String MQTT_ACTION_DISCONNECT = "disConnect";
    public static final String INTENT_ACTION_EVENT_BUS = "com.paho.mqtt.EVENT_BUS";

    public MqttAndroidClient mClient;
    private Boolean isConnecting = false;
    private int totalConnectingReturn = 0;
    private long lastDisconnectTime = 0;

    private IMqttEventListener mIMqttEventListener;

    public void setIMqttEventListener(IMqttEventListener IMqttEventListener) {
        if (mIMqttEventListener != null) return;
        mIMqttEventListener = IMqttEventListener;
    }

    public MqttInfo getMqttInfo(JSONObject params) {
        String clientId = params.getString("client_id");
        String host = params.getString("mqtt_host");
        int port = params.getInteger("mqtt_port_tcp");
        String pwd = params.getString("password");
        String userName = params.getString("username");
        if (TextUtils.isEmpty(clientId) || TextUtils.isEmpty(host) || port == 0 || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(userName)) {
            return null;
        }

        return new MqttInfo(clientId, host, port, pwd, userName);
    }

    public Object connect(Context context, MqttInfo info) {
        Log.e(TAG, "connect: 'mqtt start connect ... isConnecting = " + isConnecting);
        if (info == null) {
            return "mqtt info is empty";
        }

        if (isConnect()) {
            return "mqtt is connected";
        }

        if (mClient != null) {
            disConnect();
        }

        if (isConnecting) {
            Log.e(TAG, "connect: '---------- is doing return");
            totalConnectingReturn += 1;
            if (totalConnectingReturn >= 6) {
                totalConnectingReturn = 0;
                isConnecting = false;
            }
            return "is connecting";
        }

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(false);
            options.setCleanSession(false);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            options.setUserName(info.username);
            options.setPassword(info.password.toCharArray());

            String url = "tcp://" + info.mqtt_host + ":" + info.mqtt_port;
            mClient = new MqttAndroidClient(context, url, info.client_id, Ack.AUTO_ACK);
            mClient.setCallback(new MqttCallBackBus(mIMqttEventListener));

            if (isConnecting) {
                return "Is Connecting";
            }
            isConnecting = true;

            mClient.connect(options, null, mIMqttActionListener);
            Log.e(TAG, "connect: 'mqtt connect func end ...");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return mClient;
    }

    private final IMqttActionListener mIMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.e(TAG, "connect: '----> connect success");
            isConnecting = false;
            totalConnectingReturn = 0;
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.e(TAG, "onFailure: '----- mqtt connect fail " + exception.toString() );
            isConnecting = false;
            totalConnectingReturn = 0;
            if (mIMqttEventListener != null) {
                mIMqttEventListener.onStatusChange(Mqtt.CONNECT_STATUS_FAIL);
            }
        }
    };

    public boolean isConnect() {
        if (mClient == null) return false;

        try {
            return mClient.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disConnect() {
        if (isConnecting) {
            return;
        }

        if (mClient == null) {
            return;
        }

        long curTime = System.currentTimeMillis();
        if (curTime - lastDisconnectTime <= 500) {
            lastDisconnectTime = System.currentTimeMillis();
            return;
        }

        lastDisconnectTime = System.currentTimeMillis();

        try {
            mClient.unregisterResources();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(200);
            mClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mClient = null;
    }
}

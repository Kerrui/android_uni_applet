package com.applet.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.applet.library.IMqttServiceAidlInterface;

import androidx.annotation.Nullable;

public class MqttClientService extends Service {

    public static class MyBinder extends IMqttServiceAidlInterface.Stub {

        @Override
        public boolean isConnect() throws RemoteException {
            return Mqtt.getInstance().isConnect();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent == null ? null : intent.getAction();
        if (action != null) {
            switch (action) {
                case Mqtt.MQTT_ACTION_CONNECT:
                    MqttInfo mqttInfo = intent.getParcelableExtra("mqtt_info");
                    startConnect(mqttInfo);
                    break;
                case Mqtt.MQTT_ACTION_DISCONNECT:
                    disConnect();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        disConnect();
        super.onDestroy();
    }

    private void startConnect(MqttInfo mqttInfo) {

        Mqtt.getInstance().setIMqttEventListener(new IMqttEventListener() {
            @Override
            public void onStatusChange(int status) {
                if (status == Mqtt.CONNECT_STATUS_LOST) {
                    disConnect();
                }
                Intent intent = new Intent(Mqtt.INTENT_ACTION_EVENT_BUS);
                intent.putExtra("type", 1);
                intent.putExtra("status", status);
                sendBroadcast(intent);
            }

            @Override
            public void onAction(String message) {
                Intent intent = new Intent(Mqtt.INTENT_ACTION_EVENT_BUS);
                intent.putExtra("type", 2);
                intent.putExtra("message", message);
                sendBroadcast(intent);
            }
        });

        Mqtt.getInstance().connect(this, mqttInfo);
    }

    private void disConnect() {
        Mqtt.getInstance().disConnect();
    }
}

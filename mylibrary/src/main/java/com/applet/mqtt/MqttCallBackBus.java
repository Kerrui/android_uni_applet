package com.applet.mqtt;

import com.applet.feature.util.LogUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class MqttCallBackBus implements MqttCallbackExtended {

    private IMqttEventListener mIMqttEventListener;

    public MqttCallBackBus(IMqttEventListener IMqttEventListener) {
        mIMqttEventListener = IMqttEventListener;
    }

    @Override
    public void connectionLost(final Throwable cause) {
        if (!Mqtt.getInstance().isConnect()) {
            executeConnectLost(cause);
        }
    }

    private void executeConnectLost(Throwable cause) {
        if (cause != null) {
            if (cause instanceof MqttException) {
                MqttException mqttException = (MqttException) cause;
                if (mqttException.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST) {
                    LogUtil.t("connectionLost: QMTT connect lost ...... ");
                    if (mIMqttEventListener != null) {
                        mIMqttEventListener.onStatusChange(Mqtt.CONNECT_STATUS_LOST);
                    }
                }
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String msg = new String(mqttMessage.getPayload());
        LogUtil.t("mqtt receive msg = " + msg);
        if (mIMqttEventListener != null) {
            mIMqttEventListener.onAction(msg);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        LogUtil.t("connectComplete: mqtt connectComplete  " + reconnect);
        int status = reconnect ? Mqtt.CONNECT_STATUS_AGAIN : Mqtt.CONNECT_STATUS_SUCCESS;
        if (mIMqttEventListener != null) {
            mIMqttEventListener.onStatusChange(status);
        }
    }
}

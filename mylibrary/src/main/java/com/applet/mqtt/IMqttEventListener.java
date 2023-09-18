package com.applet.mqtt;

interface IMqttEventListener {

    void onStatusChange(int status);

    void onAction(String message);
}

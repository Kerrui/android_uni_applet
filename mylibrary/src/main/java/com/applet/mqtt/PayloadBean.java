package com.applet.mqtt;

public class PayloadBean {

    public int type;
    public Object data;

    public PayloadBean() {
    }

    public PayloadBean(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public PayloadBean(int type) {
        this.type = type;
    }
}

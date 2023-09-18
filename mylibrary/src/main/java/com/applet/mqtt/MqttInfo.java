package com.applet.mqtt;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MqttInfo implements Parcelable {

    public String client_id;
    public String mqtt_host;
    public int mqtt_port;
    public String password;
    public String username;

    public MqttInfo() {
    }

    public MqttInfo(String client_id, String mqtt_host, int mqtt_port, String password, String username) {
        this.client_id = client_id;
        this.mqtt_host = mqtt_host;
        this.mqtt_port = mqtt_port;
        this.password = password;
        this.username = username;
    }

    protected MqttInfo(Parcel in) {
        client_id = in.readString();
        mqtt_host = in.readString();
        mqtt_port = in.readInt();
        password = in.readString();
        username = in.readString();
    }

    public static final Creator<MqttInfo> CREATOR = new Creator<MqttInfo>() {
        @Override
        public MqttInfo createFromParcel(Parcel in) {
            return new MqttInfo(in);
        }

        @Override
        public MqttInfo[] newArray(int size) {
            return new MqttInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(client_id);
        dest.writeString(mqtt_host);
        dest.writeInt(mqtt_port);
        dest.writeString(password);
        dest.writeString(username);
    }
}

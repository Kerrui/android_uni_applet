package com.applet.mqtt;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PayloadBean implements Parcelable {

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

    protected PayloadBean(Parcel in) {
        type = in.readInt();
    }

    public static final Creator<PayloadBean> CREATOR = new Creator<PayloadBean>() {
        @Override
        public PayloadBean createFromParcel(Parcel in) {
            return new PayloadBean(in);
        }

        @Override
        public PayloadBean[] newArray(int size) {
            return new PayloadBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(type);
    }
}

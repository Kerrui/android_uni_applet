package com.applet.feature.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class WgtInfo implements Parcelable {

    public String appid;
    public String url;
    public String wgt_version;

    public WgtInfo() {
    }

    protected WgtInfo(Parcel in) {
        appid = in.readString();
        url = in.readString();
        wgt_version = in.readString();
    }

    public static final Creator<WgtInfo> CREATOR = new Creator<WgtInfo>() {
        @Override
        public WgtInfo createFromParcel(Parcel in) {
            return new WgtInfo(in);
        }

        @Override
        public WgtInfo[] newArray(int size) {
            return new WgtInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(appid);
        dest.writeString(url);
        dest.writeString(wgt_version);
    }
}

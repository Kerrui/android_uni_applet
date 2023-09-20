package com.applet.upload;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DirInfo implements Parcelable {

    public String path;
    public String small_url;
    public String cover_path;
    public String cover_upload_type;

    public DirInfo() {
    }

    protected DirInfo(Parcel in) {
        path = in.readString();
        small_url = in.readString();
        cover_path = in.readString();
        cover_upload_type = in.readString();
    }

    public static final Creator<DirInfo> CREATOR = new Creator<DirInfo>() {
        @Override
        public DirInfo createFromParcel(Parcel in) {
            return new DirInfo(in);
        }

        @Override
        public DirInfo[] newArray(int size) {
            return new DirInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(small_url);
        dest.writeString(cover_path);
        dest.writeString(cover_upload_type);
    }
}

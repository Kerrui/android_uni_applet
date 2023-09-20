package com.applet.upload;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class S3Info implements Parcelable {

    public DirInfo dir;
    public String host;
    public String upload_type;
    public String date;
    public String token;
    public String cover_token;
    public String cover_upload_type;

    public S3Info() {
    }

    protected S3Info(Parcel in) {
        dir = in.readParcelable(DirInfo.class.getClassLoader());
        host = in.readString();
        upload_type = in.readString();
        date = in.readString();
        token = in.readString();
        cover_token = in.readString();
        cover_upload_type = in.readString();
    }

    public static final Creator<S3Info> CREATOR = new Creator<S3Info>() {
        @Override
        public S3Info createFromParcel(Parcel in) {
            return new S3Info(in);
        }

        @Override
        public S3Info[] newArray(int size) {
            return new S3Info[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(dir, flags);
        dest.writeString(host);
        dest.writeString(upload_type);
        dest.writeString(date);
        dest.writeString(token);
        dest.writeString(cover_token);
        dest.writeString(cover_upload_type);
    }
}

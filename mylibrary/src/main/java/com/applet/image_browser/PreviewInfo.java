package com.applet.image_browser;

import android.os.Parcel;
import android.os.Parcelable;

public class PreviewInfo implements Parcelable {

   public boolean isVideo;
   public String thumbnailUrl;
   public String url;
   public String coverUrl;
   public int width;
   public int height;

    public PreviewInfo() {
    }

    protected PreviewInfo(Parcel in) {
        isVideo = in.readByte() != 0;
        thumbnailUrl = in.readString();
        url = in.readString();
        coverUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    public static final Creator<PreviewInfo> CREATOR = new Creator<PreviewInfo>() {
        @Override
        public PreviewInfo createFromParcel(Parcel in) {
            return new PreviewInfo(in);
        }

        @Override
        public PreviewInfo[] newArray(int size) {
            return new PreviewInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isVideo ? 1 : 0));
        parcel.writeString(thumbnailUrl);
        parcel.writeString(url);
        parcel.writeString(coverUrl);
        parcel.writeInt(width);
        parcel.writeInt(height);
    }
}

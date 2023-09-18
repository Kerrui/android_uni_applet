package com.applet.image_browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.applet.image_browser.view.GPreviewActivity;
import com.applet.library.R;

import java.util.ArrayList;
import java.util.List;

public final class GPreviewBuilder {

    private Context mContext;
    private Intent mIntent;
    private IPreviewListener mIPreviewListener;

    public GPreviewBuilder(Context context) {
        mContext = context;
        mIntent = new Intent();
    }

    public static GPreviewBuilder from(Context context) {
        return new GPreviewBuilder(context);
    }

    public GPreviewBuilder setData(List<PreviewInfo> infoList) {
        mIntent.putParcelableArrayListExtra("previewInfo", new ArrayList<Parcelable>(infoList));
        return this;
    }

    public GPreviewBuilder setIndex(int index) {
        mIntent.putExtra("index", index);
        return this;
    }

    /***
     * 设置图片拖拽返回灵敏度
     * @param sensitivity   sensitivity MAX_TRANS_SCALE 的值来控制灵敏度。
     * @return GPreviewBuilder
     * **/
    public GPreviewBuilder setSensitivity(float sensitivity) {
        mIntent.putExtra("sensitivity", sensitivity);
        return this;
    }

    public GPreviewBuilder showIndicator(Boolean isShow) {
        mIntent.putExtra("indicator", isShow);
        return this;
    }

    /***
     *  设置是否全屏
     * @param isFullscreen boolean
     * @return GPreviewBuilder
     * **/
    public GPreviewBuilder setFullscreen(boolean isFullscreen) {
        mIntent.putExtra("isFullscreen", isFullscreen);
        return this;
    }

    public GPreviewBuilder setOnIPreviewListener(IPreviewListener iPreviewListener) {
        this.mIPreviewListener = iPreviewListener;
        return this;
    }

    public void start(){
        mIntent.setClass(mContext, GPreviewActivity.class);
        GPreviewActivity.sIPreviewListener = mIPreviewListener;
        mContext.startActivity(mIntent);
        if (mContext instanceof Activity) {
            ((Activity)mContext).overridePendingTransition(R.anim.image_browser_act_enter, 0);
        }
        mIntent = null;
        mContext = null;
    }
}

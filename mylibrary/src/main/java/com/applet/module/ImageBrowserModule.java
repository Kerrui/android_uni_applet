package com.applet.module;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.applet.image_browser.GPreviewBuilder;
import com.applet.image_browser.IPreviewListener;
import com.applet.image_browser.PreviewInfo;
import com.applet.image_browser.util.BitmapUtils;
import com.applet.image_browser.view.PreviewFragment;

import java.util.List;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class ImageBrowserModule extends UniModule {

    private PreviewFragment mPreviewFragment;

    @UniJSMethod(uiThread = true)
    public void getVideoCover(JSONObject params, UniJSCallback callback) {
        String videoUrl = params.getString("url");
        long frameTime = params.containsKey("frame_time") ? params.getLongValue("frame_time") : 1000;
        if (TextUtils.isEmpty(videoUrl)) {
            callback.invoke("");
            return;
        }

        String bundleUrl = mUniSDKInstance.getBundleUrl();
        bundleUrl = bundleUrl.replace("file://", "");
        int index = bundleUrl.indexOf("/www/");
        bundleUrl = bundleUrl.substring(0, index);
        String saveFolderPath = bundleUrl + "/doc/uniapp_temp/";

        Bitmap bitmap = BitmapUtils.getVideoCoverBitmap(mUniSDKInstance.getContext(), videoUrl, frameTime);
        if (bitmap == null) {
            callback.invoke("");
            return;
        }
        String coverUrl = BitmapUtils.bitmapToStringPath(bitmap, saveFolderPath);
        callback.invoke(coverUrl);
    }

    @UniJSMethod(uiThread = true)
    public void openBrowser(JSONObject params, UniJSCallback callback) {
        JSONArray jsonArray = params.getJSONArray("info");
        int index = params.containsKey("index") ? params.getInteger("index") : 1;
        int showIndicator = params.containsKey("showIndicator") ? params.getInteger("showIndicator") : 0;

        List<PreviewInfo> previewInfoList = JSON.parseArray(jsonArray.toJSONString(), PreviewInfo.class);

        GPreviewBuilder.from(mUniSDKInstance.getContext())
                .setData(previewInfoList)
                .setFullscreen(true)
                .setIndex(index)
                .showIndicator(showIndicator == 1)
                .setSensitivity(0.15f)
                .setOnIPreviewListener(new IPreviewListener() {
                    @Override
                    public void onCreate(PreviewFragment previewFragment) {
                        mPreviewFragment = previewFragment;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", 8);
                        callback.invokeAndKeepAlive(jsonObject);
                    }

                    @Override
                    public void onDestroy() {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", 9);
                        callback.invokeAndKeepAlive(jsonObject);
                    }

                    @Override
                    public void onImgLongClick(int position) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", 7);
                        jsonObject.put("position", position);
                        jsonObject.put("data", previewInfoList.get(position));
                        callback.invokeAndKeepAlive(jsonObject);
                    }
                })
                .start();
    }

    @UniJSMethod(uiThread = true)
    public void hide(JSONObject params, UniJSCallback callback) {
        if (mPreviewFragment != null) {
            mPreviewFragment.closeFragment();
        }
    }
}

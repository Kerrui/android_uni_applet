package com.applet.image_browser;


import com.applet.image_browser.view.PreviewFragment;

public interface IPreviewListener {

    void onCreate(PreviewFragment previewFragment);

    void onDestroy();

    void onImgLongClick(int position);
}

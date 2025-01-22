package com.applet.feature;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSONObject;
import com.applet.feature.bean.WgtInfo;
import com.applet.feature.module.APPLetUtil;
import com.bumptech.glide.Glide;
import com.hi.chat.uniplugin.LibConstant;
import com.hi.chat.uniplugin.mmkv.MMKVUtil;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;

public class SPActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(LibConstant.SPLASH_IMG_PATH).into(imageView);

        String data = getIntent().getStringExtra("data");
        JSONObject newAppletInfo = JSONObject.parseObject(data);
        APPLetUtil.setDefaultApplet(newAppletInfo);
        System.out.println("1SPActivity--->>" + data);


//            AppletManager.deleteOldVersion(this);
        try {
            JSONObject jsonObject = MMKVUtil.getInstance().getJSONObject(LibConstant.SP_WGT_APPLET);
            UniMPOpenConfiguration configuration = new UniMPOpenConfiguration();
            configuration.splashClass = CSplash.class;
            System.out.println("SPActivity--->>" + jsonObject);
            if (jsonObject == null) {
                IUniMP uniMP = AppletManager.openUniMP(SPActivity.this, LibConstant.D_APP_ID, configuration);
            } else {
                WgtInfo wgtInfo = new WgtInfo();
                wgtInfo.appid = jsonObject.getString("appid");
                wgtInfo.url = jsonObject.getString("url");
                wgtInfo.wgt_version = jsonObject.getString("wgt_version");

                String appid = wgtInfo.appid;
                if (DCUniMPSDK.getInstance().isExistsApp(appid)) {
                    IUniMP uniMP = AppletManager.openUniMP(this, appid, configuration);
                } else {
                    IUniMP uniMP = AppletManager.openUniMP(this, LibConstant.D_APP_ID, configuration);

                }
            }
            imageView.postDelayed(() -> {
                SPActivity.this.finish();
            }, 1000);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
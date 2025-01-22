package com.applet.uni_applet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSONObject;
import com.applet.feature.AppletManager;
import com.applet.feature.CSplash;
import com.applet.feature.bean.WgtInfo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hi.chat.uniplugin.LibConstant;
import com.hi.chat.uniplugin.mmkv.MMKVUtil;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;

public class MainActivity extends FragmentActivity {

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
        saveFireBaseToken();
//        AppletManager.deleteOldVersion(this);
        imageView.postDelayed(() -> {
            try {
                JSONObject jsonObject = MMKVUtil.getInstance().getJSONObject(LibConstant.SP_WGT_APPLET);
                UniMPOpenConfiguration configuration = new UniMPOpenConfiguration();
                configuration.splashClass = CSplash.class;
                if (jsonObject == null) {
                    IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, LibConstant.D_APP_ID, configuration);
                } else {
                    WgtInfo wgtInfo = new WgtInfo();
                    wgtInfo.appid = jsonObject.getString("appid");
                    wgtInfo.url = jsonObject.getString("url");
                    wgtInfo.wgt_version = jsonObject.getString("wgt_version");

                    String appid = wgtInfo.appid;
                    if (DCUniMPSDK.getInstance().isExistsApp(appid)) {
                        IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, appid, configuration);
                    } else {
                        IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, LibConstant.D_APP_ID, configuration);

                    }
                }
                imageView.postDelayed(() -> {
                    MainActivity.this.finish();
                }, 100);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1000);



    }


    private void saveFireBaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    MMKVUtil.getInstance().put(LibConstant.FIRE_BASE_TOKEN, token);
                }
            }
        });
    }


}
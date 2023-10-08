package com.applet.uni_applet;

import android.os.Bundle;
import android.view.View;

import com.applet.feature.util.LogUtil;
import com.applet.mylibrary.AppLibSdk;
import com.example.uni_applet.R;

import androidx.appcompat.app.AppCompatActivity;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IOnUniMPEventCallBack;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.DCUniMPJSCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DCUniMPSDK.getInstance().setOnUniMPEventCallBack(new IOnUniMPEventCallBack() {
            @Override
            public void onUniMPEventReceive(String appId, String event, Object data, DCUniMPJSCallback callback) {
                LogUtil.t("onUniMPEventReceive app id = " + appId + " event = " + event);
                //AppLibSdk.getInstance().openKFApp(MainActivity.this, "", "",true, true);
                if (event.equals("applet_event_open_customer_service")) {
                    String faceUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRM-r3Y_vyQLZ55F8hCsS65hQXKRoKcOqYrgw&usqp=CAU";
                    String uid = "1244522";
                    AppLibSdk.getInstance().openKFApp(MainActivity.this, faceUrl, uid, true, false);
                }
            }
        });

        findViewById(R.id.btn_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String faceUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRM-r3Y_vyQLZ55F8hCsS65hQXKRoKcOqYrgw&usqp=CAU";
                String uid = "1244522";
                AppLibSdk.getInstance().openKFApp(MainActivity.this, faceUrl, uid, true, false);
            }
        });

        findViewById(R.id.btn_applet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLibSdk.getInstance().openKFApp(MainActivity.this, "", "",true, true);
            }
        });

        findViewById(R.id.btn_open_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(MainActivity.this, "__UNI__18DF11F");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
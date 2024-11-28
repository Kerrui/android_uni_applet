package com.applet.uni_applet;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.applet.feature.AppletManager;
import com.applet.feature.change.ChangePackage;
import com.example.uni_applet.R;

import androidx.appcompat.app.AppCompatActivity;

import io.dcloud.common.DHInterface.SplashView;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, "__UNI__1950756");

                ChangePackage.INSTANCE.changePackageRequest(MainActivity.this,null,1);
//                ChangePackage.INSTANCE.activeRequest(MainActivity.this,2);

            }
        });

        findViewById(R.id.btn_applet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, "__UNI__3932C1F");

                    }
                },3000);
            }
        });

        findViewById(R.id.btn_open_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                    uniMPOpenConfiguration.splashClass = SplashView.class;
                    uniMPOpenConfiguration.extraData.put("darkmode", "light");
                    IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, "__UNI__1950756");

//                    new Handler().postDelayed(new Runnable(){
//
//                        @Override
//                        public void run() {
//                            IUniMP currentUniMP = MPStack.getInstance().getCurrentUniMP();
//                            currentUniMP.closeUniMP();
//                        }
//                    },13000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
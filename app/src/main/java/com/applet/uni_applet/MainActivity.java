package com.applet.uni_applet;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.and.uniplugin.feature.util.LogUtil;
import com.applet.feature.AppletManager;
import com.applet.feature.LibConstant;
import com.applet.feature.change.ChangePackage;
import com.bumptech.glide.Glide;
import com.example.uni_applet.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.dcloud.common.DHInterface.SplashView;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load("file:///android_asset/static/app_bg.jpg").into(imageView);


        AppletManager.deleteOldVersion(this);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, "__UNI__1950756");
                    System.out.println("------appBasePath--------");


                    imageView.postDelayed(() -> {
                        MainActivity.this.finish();
                    }, 1500);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 1000);

    }

    public void openUniMP() {
        findViewById(R.id.btn_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, "__UNI__1950756");

                ChangePackage.INSTANCE.changePackageRequest(MainActivity.this, null, 1);
//                ChangePackage.INSTANCE.activeRequest(MainActivity.this,2);

            }
        });

        findViewById(R.id.btn_applet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        IUniMP uniMP = AppletManager.openUniMP(MainActivity.this, "__UNI__3932C1F");

                    }
                }, 3000);
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
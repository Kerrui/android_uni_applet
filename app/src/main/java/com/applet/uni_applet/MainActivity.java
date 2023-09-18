package com.applet.uni_applet;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.applet.mylibrary.AppLibSdk;
import com.example.uni_applet.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        findViewById(R.id.btn_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppLibSdk.getInstance().helloLib(MainActivity.this);

                AppLibSdk.getInstance().openApplet(MainActivity.this, Constant.APP_ID_A);


                //new Handler().postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                //        AppLibSdk.getInstance().openApplet(MainActivity.this, "__UNI__2A047DF");
                //    }
                //}, 5000);
            }
        });

        //checkPermission();
    }

    public void checkPermission() {
        int targetSdkVersion = 0;
        String[] PermissionString = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        try {
            final PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;//获取应用的Target版本
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Build.VERSION.SDK_INT是获取当前手机版本 Build.VERSION_CODES.M为6.0系统
            //如果系统>=6.0
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                //第 1 步: 检查是否有相应的权限
                boolean isAllGranted = checkPermissionAllGranted(PermissionString);
                if (isAllGranted) {
                    Log.e("err", "所有权限已经授权！");
                    return;
                }
                // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(this, PermissionString, 1);
            }
        }
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                //Log.e("err","权限"+permission+"没有授权");
                return false;
            }
        }
        return true;
    }
}
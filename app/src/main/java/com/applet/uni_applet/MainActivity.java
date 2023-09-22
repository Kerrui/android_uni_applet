package com.applet.uni_applet;

import android.os.Bundle;
import android.view.View;

import com.applet.mylibrary.AppLibSdk;
import com.example.uni_applet.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        //String[] PermissionString = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }
}
package com.example.mylibrary;

import android.content.Context;
import android.widget.Toast;

public class LibToast {

    public LibToast() {
    }

    private static class LibToastHolder {
        private static final LibToast sInstance = new LibToast();
    }

    public static LibToast getInstance() {
        return LibToastHolder.sInstance;
    }

    public void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

    }
}

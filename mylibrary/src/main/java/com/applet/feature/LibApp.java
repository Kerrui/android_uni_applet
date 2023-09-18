package com.applet.feature;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

public final class LibApp {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private LibApp() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(@NonNull final Context context) {
        LibApp.context = context.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("should be initialized in application");
    }
}

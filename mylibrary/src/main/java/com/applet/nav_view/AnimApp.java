package com.applet.nav_view;

import android.content.Context;
import android.net.http.HttpResponseCache;

import com.opensource.svgaplayer.SVGACache;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.utils.log.SVGALogger;

import java.io.File;

import androidx.annotation.NonNull;

public final class AnimApp {

    private AnimApp() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(@NonNull final Context context) {
        initSVGA(context.getApplicationContext());
    }

    private static void initSVGA(Context context) {
        SVGAParser.Companion.shareParser().init(context);

        SVGACache.INSTANCE.onCreate(context, SVGACache.Type.FILE);
        File cacheFile = new File(context.getCacheDir(), "svga");
        try {
            HttpResponseCache.install(cacheFile, 1024 * 1024 * 300);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SVGALogger.INSTANCE.setLogEnabled(false);
    }
}

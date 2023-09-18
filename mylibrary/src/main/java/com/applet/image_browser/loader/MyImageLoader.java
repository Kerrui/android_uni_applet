package com.applet.image_browser.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mylibrary.R;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyImageLoader implements IZoomMediaLoader {

    private static final String TAG = "MyImageLoader";

    private final Map<String, Bitmap> cache = new HashMap<>();

    public void add(String path, Bitmap bitmap) {
        if (bitmap == null || cache.containsKey(path)) {
            return;
        }
        cache.put(path, bitmap);
    }

    public void destroy() {
        Iterator<String> iterator = cache.keySet().iterator();
        while (iterator.hasNext()) {
            Bitmap bitmap = cache.get(iterator.next());
            iterator.remove();
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    @Override
    public void displayImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull MySimpleTarget simpleTarget) {

        Log.e(TAG, "displayImage: ----> preview img path = " + path);

        Uri pathUri;
        if (path.startsWith("http")) {
            pathUri = Uri.parse(path);
        } else {
            pathUri = Uri.fromFile(new File(path));
        }

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(pathUri)
                //.setProgressiveRenderingEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        com.facebook.datasource.DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                final Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), false);
                simpleTarget.onResourceReady(tempBitmap);
                add(path, tempBitmap);
            }

            @Override
            protected void onFailureImpl(com.facebook.datasource.DataSource<CloseableReference<CloseableImage>> dataSource) {
                simpleTarget.onLoadFailed(null);
            }
        }, CallerThreadExecutor.getInstance());
    }

    @Override
    public void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull MySimpleTarget simpleTarget) {
        Glide.with(context)
                .asGif()
                .load(path)
                //可以解决gif比较几种时 ，加载过慢  //DiskCacheStrategy.NONE
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.image_browser_ic_default)
                .dontAnimate() //去掉显示动画
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        simpleTarget.onResourceReady(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        Glide.with(context).onStop();
    }

    @Override
    public void clearMemory(@NonNull Context c) {
        destroy();
        Glide.get(c).clearMemory();
    }
}

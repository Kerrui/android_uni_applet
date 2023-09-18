package com.applet.image_browser.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BitmapUtils {

    public static Bitmap getVideoCoverBitmap(Context context, String videoUrl, long frameTime) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT >= 29 && !videoUrl.contains(context.getPackageName())){
            Uri uri = BitmapUtils.getVideoContentUri(context, videoUrl);
            bitmap = BitmapUtils.getBitmapFormUrl(context, uri, frameTime);
        } else {
            bitmap = BitmapUtils.getBitmapFormUrl(videoUrl, frameTime);
        }
        return bitmap;
    }

    public static String bitmapToStringPath(Bitmap bitmap, String folderPath) {
        File filePic;
        try {
            filePic = new File(folderPath + UUID.randomUUID().toString() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }

    private static Uri getVideoContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/video/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private static Bitmap getBitmapFormUrl(Context context, Uri uri, long frameTime) {
        try {
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(context, uri);
            return metadataRetriever.getFrameAtTime(frameTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bitmap getBitmapFormUrl(String url, long frameTime) {
        try {
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(url);
            return metadataRetriever.getFrameAtTime(frameTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

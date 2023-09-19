package com.applet.upload;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import dc.squareup.okhttp3.Call;
import dc.squareup.okhttp3.MediaType;
import dc.squareup.okhttp3.OkHttpClient;
import dc.squareup.okhttp3.Request;
import dc.squareup.okhttp3.RequestBody;
import dc.squareup.okhttp3.Response;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class UploadManager {

    public UploadManager() {
    }

    private static class UploadManagerHolder {
        private static final UploadManager sInstance = new UploadManager();
    }

    public static UploadManager getInstance() {
        return UploadManagerHolder.sInstance;
    }

    private ExecutorService mExecutorUp;

    public void uploadToS3(Context context, String uniBundleUrl, JSONObject params, UniJSCallback callback) {
        newExecutorUp();
        mExecutorUp.execute(new Runnable() {
            @Override
            public void run() {
                String filePath = params.getString("filePath");
                int isBase64 = params.containsKey("isBase64") ? params.getInteger("isBase64") : 0;
                String coverFilePath = params.containsKey("coverFilePath") ? params.getString("coverFilePath") : "";
                int coverIsBase64 = params.containsKey("coverIsBase64") ? params.getInteger("coverIsBase64") : 0;
                if (!params.containsKey("ossInfo") || TextUtils.isEmpty(filePath)) {
                    callback.invoke(false);
                    closeExecutorUp();
                    return;
                }

                S3Info s3Info = params.getObject("ossInfo", S3Info.class);
                UpResBean fileUpResBean = s3Upload(context, uniBundleUrl, filePath, isBase64, false, s3Info);
                if (!fileUpResBean.isSuccess) {
                    callback.invoke(backObj(fileUpResBean, false));
                    closeExecutorUp();
                    return;
                }
                if (TextUtils.isEmpty(coverFilePath)) {
                    callback.invoke(backObj(new UpResBean(true, "Empty cover file path"), false));
                    closeExecutorUp();
                    return;
                }
                UpResBean coverUpResBean = s3Upload(context, uniBundleUrl, coverFilePath, coverIsBase64, true, s3Info);
                callback.invoke(backObj(coverUpResBean, true));
                closeExecutorUp();
            }
        });
    }

    private UpResBean s3Upload(Context context, String uniBundleUrl, String filePath, int isBase64, boolean isCover, S3Info s3Info) {
        try {
            String uploadType;
            String dirPath;
            String token;
            if (isCover) {
                token = s3Info.cover_token;
                uploadType = s3Info.cover_upload_type;
                dirPath = s3Info.dir.cover_path;
            } else {
                token = s3Info.token;
                uploadType = s3Info.upload_type;
                dirPath = s3Info.dir.path;
            }
            RequestBody fileBody;
            if (isBase64 == 1) {
                filePath = filePath.replace("data:image/jpeg;base64,", "");
                fileBody = RequestBody.create(MediaType.parse(uploadType), Base64.decode(filePath, 0));
            } else {
                fileBody = RequestBody.create(MediaType.parse(uploadType), getUpFile(context, uniBundleUrl, filePath, uploadType));
            }

            Request.Builder requestBuilder = new Request.Builder().url(s3Info.host + dirPath);
            requestBuilder.addHeader("Content-Type", uploadType);
            requestBuilder.addHeader("Authorization", token);
            requestBuilder.addHeader("Date", s3Info.date);
            requestBuilder.put(fileBody);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().writeTimeout(2, TimeUnit.MINUTES).build();
            Call call = okHttpClient.newCall(requestBuilder.build());
            Response response = call.execute();

            String filePathStr = isBase64 != 1 ? filePath : "";
            String upInfoStr = filePathStr + " - " + s3Info.host + dirPath + " - " + uploadType;
            String returnStr = response.toString() + upInfoStr;

            return new UpResBean(response.code() == 200, returnStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new UpResBean(false, e.toString());
        }
    }

    public void uploadByList(Context context, String uniBundleUrl, JSONObject params, UniJSCallback callback) {
        newExecutorUp();
        mExecutorUp.execute(new Runnable() {
            @Override
            public void run() {
                int type = params.getInteger("type");
                JSONArray fileArr = params.getJSONArray("fileList");
                for (int i = 0; i < fileArr.size(); i++) {
                    JSONObject itemObj = fileArr.getJSONObject(i);
                    String filePath = itemObj.getString("filePath");
                    int isBase64 = itemObj.containsKey("isBase64") ? itemObj.getInteger("isBase64") : 0;
                    String coverFilePath = itemObj.containsKey("coverFilePath") ? itemObj.getString("coverFilePath") : "";
                    int coverIsBase64 = itemObj.containsKey("coverIsBase64") ? itemObj.getInteger("coverIsBase64") : 0;


                    S3Info s3Info = itemObj.getObject("ossInfo", S3Info.class);
                    UpResBean fileUpResBean = s3Upload(context, uniBundleUrl, filePath, isBase64, false, s3Info);
                    if (!fileUpResBean.isSuccess) {
                        params.put("status", false);
                        params.put("isCover", false);
                        params.put("msg", fileUpResBean.response);
                        callback.invoke(params);
                        closeExecutorUp();
                        return;
                    }
                    if (!TextUtils.isEmpty(coverFilePath)) {
                        UpResBean coverUpResBean = s3Upload(context, uniBundleUrl, coverFilePath, coverIsBase64, true, s3Info);
                        if (!coverUpResBean.isSuccess) {
                            params.put("status", false);
                            params.put("isCover", true);
                            params.put("msg", coverUpResBean.response);
                            callback.invoke(params);
                            closeExecutorUp();
                            return;
                        }
                    }
                }
                params.put("status", true);
                callback.invoke(params);
                closeExecutorUp();
            }
        });
    }

    private JSONObject backObj(UpResBean upResBean, boolean isCover) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", upResBean.isSuccess);
        jsonObject.put("isCover", isCover);
        jsonObject.put("msg", upResBean.response);
        return jsonObject;
    }

    private void newExecutorUp() {
        if (mExecutorUp == null) mExecutorUp = Executors.newFixedThreadPool(1);
    }

    private void closeExecutorUp() {
        if (mExecutorUp == null) return;
        mExecutorUp.shutdown();
        mExecutorUp = null;
    }

    private File getUpFile(Context context, String uniBundleUrl, String filePath, String uploadType) {
        if (Build.VERSION.SDK_INT >= 29 && !filePath.contains(context.getPackageName())) {
            int mediaType;
            if (uploadType.contains("image")) {
                mediaType = 1;
            } else if (uploadType.contains("video")) {
                mediaType = 2;
            } else {
                mediaType = 3;
            }
            Uri uri = getMediaContentUri(context, mediaType, filePath);
            String bundleUrl = uniBundleUrl;
            bundleUrl = bundleUrl.replace("file://", "");
            int index = bundleUrl.indexOf("/www/");
            bundleUrl = bundleUrl.substring(0, index);
            String saveFolderPath = bundleUrl + "/doc/uniapp_temp/";

            return uriToFileApiQ(saveFolderPath, uri, context);
        } else {
            return new File(filePath);
        }
    }

    private Uri getMediaContentUri(Context context, int mediaType, String path) {
        Cursor cursor;
        String uriType;
        String mediaData;
        Uri mediaUri;
        if (mediaType == 1) {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                    new String[]{path}, null);
            uriType = "images";
            mediaData = MediaStore.Images.Media.DATA;
            mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (mediaType == 2) {
            cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA + "=? ",
                    new String[]{path}, null);
            uriType = "video";
            mediaData = MediaStore.Video.Media.DATA;
            mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media._ID}, MediaStore.Audio.Media.DATA + "=? ",
                    new String[]{path}, null);
            uriType = "audio";
            mediaData = MediaStore.Audio.Media.DATA;
            mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/" + uriType + "/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(mediaData, path);
                return context.getContentResolver().insert(mediaUri, values);
            } else {
                return null;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private File uriToFileApiQ(String savePath, Uri uri, Context context) {
        File file = null;
        if (uri == null) return null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = System.currentTimeMillis() + Math.round((Math.random() + 1) * 1000)
                    + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(savePath, displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                FileUtils.copy(is, fos);
                file = cache;
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}

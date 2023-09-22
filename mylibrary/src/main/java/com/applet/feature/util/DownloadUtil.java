package com.applet.feature.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {

    public DownloadUtil() {
        mOkHttpClient = new OkHttpClient();
    }

    private static class DownloadUtilHolder {
        private static final DownloadUtil sInstance = new DownloadUtil();
    }

    public static DownloadUtil getInstance() {
        return DownloadUtilHolder.sInstance;
    }

    private final OkHttpClient mOkHttpClient;

    public void download(String url, String saveDir, String fileName, OnDownloadListener onDownloadListener) {
        download(url, saveDir, fileName, false, onDownloadListener);
    }
    public void download(String url, String saveDir, String fileName, boolean needLoading, OnDownloadListener onDownloadListener) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onDownloadListener.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(saveDir, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        if (needLoading) onDownloadListener.onLoading(progress);
                    }
                    fos.flush();
                    onDownloadListener.onSuccess(file);
                } catch (Exception e) {
                    onDownloadListener.onFailed(e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface OnDownloadListener {
        void onSuccess(File file);

        void onLoading(int progress);

        void onFailed(String message);
    }
}

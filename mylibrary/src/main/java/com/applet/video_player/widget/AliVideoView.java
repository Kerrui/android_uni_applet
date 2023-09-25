package com.applet.video_player.widget;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.nativeclass.CacheConfig;
import com.aliyun.player.nativeclass.MediaInfo;
import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.source.UrlSource;
import com.applet.video_player.VideoListener;
import com.applet.video_player.util.DragCloseHelper;

import java.io.File;
import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AliVideoView extends FrameLayout {

    private Context mContext;
    private AliPlayer mAliPlayer;
    private IRenderView mIRenderView;

    public enum SurfaceType {
        /**
         * TextureView
         */
        TEXTURE_VIEW,
        /**
         * SurfacView
         */
        SURFACE_VIEW
    }

    public AliVideoView(@NonNull Context context) {
        this(context, null);
    }

    public AliVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AliVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        mAliPlayer = AliPlayerFactory.createAliPlayer(mContext.getApplicationContext());
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        if (surfaceType == SurfaceType.TEXTURE_VIEW && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
            mIRenderView = new TextureRenderView(mContext);
        } else {
            mIRenderView = new SurfaceRenderView(mContext);
        }

        mIRenderView.addRenderCallback(new MyRenderViewCallback(this));
        addView(mIRenderView.getView());
    }

    public void setWH() {
        int width = 0;
        int height = 0;
        int rotation = mAliPlayer.getVideoRotation();
        if (rotation == 0) {
            width = mAliPlayer.getVideoWidth();
            height = mAliPlayer.getVideoHeight();
        } else if (rotation == 90 || rotation == 270) {
            width = mAliPlayer.getVideoHeight();
            height = mAliPlayer.getVideoWidth();
        }
        if (width <= 0 || height <= 0) return;
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        float scale = (float) width / (float) screenWidth;
        int showHeight = (int) (height / scale);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, showHeight);
        mIRenderView.getView().setLayoutParams(layoutParams);
    }

    private static class MyRenderViewCallback implements IRenderView.IRenderCallback {

        private WeakReference<AliVideoView> weakReference;

        private MyRenderViewCallback(AliVideoView AliVideoView) {
            weakReference = new WeakReference<>(AliVideoView);
        }

        @Override
        public void onSurfaceCreate(Surface surface) {
            AliVideoView AliVideoView = weakReference.get();
            if (AliVideoView != null && AliVideoView.mAliPlayer != null) {
                AliVideoView.mAliPlayer.setSurface(surface);
            }
        }

        @Override
        public void onSurfaceChanged(int width, int height) {
            AliVideoView AliVideoView = weakReference.get();
            if (AliVideoView != null && AliVideoView.mAliPlayer != null) {
                AliVideoView.mAliPlayer.surfaceChanged();
            }
        }

        @Override
        public void onSurfaceDestroyed() {
            AliVideoView AliVideoView = weakReference.get();
            if (AliVideoView != null && AliVideoView.mAliPlayer != null) {
                AliVideoView.mAliPlayer.setSurface(null);
            }
        }
    }

    public AliPlayer getAliPlayer() {
        return mAliPlayer;
    }

    public void setUrl(String url) {
        if (mAliPlayer != null) {
            UrlSource urlSource = new UrlSource();
            if (Build.VERSION.SDK_INT >= 29 && !url.contains(mContext.getPackageName()) && !url.startsWith("http")){
                Uri uri = getVideoContentUri(mContext, url);
                if (uri == null) return;
                urlSource.setUri(uri.toString());
            } else {
                urlSource.setUri(url);
            }
            mAliPlayer.setDataSource(urlSource);
        }
    }

    private Uri getVideoContentUri(Context context, String path) {
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

    /**
     * 设置是否静音
     */
    public void setMute(boolean isMute) {
        if (mAliPlayer != null) {
            mAliPlayer.setMute(isMute);
        }
    }

    /**
     * 设置音量
     */
    public void setVolume(float v) {
        if (mAliPlayer != null) {
            mAliPlayer.setVolume(v);
        }
    }

    /**
     * 是否开启自动播放
     */
    public void setAutoPlay(boolean isAutoPlay) {
        if (mAliPlayer != null) {
            mAliPlayer.setAutoPlay(isAutoPlay);
        }
    }

    /**
     * 是否循环播放
     */
    public void setLoop(boolean loop) {
        if (mAliPlayer != null) {
            mAliPlayer.setLoop(loop);
        }
    }

    /**
     * 截屏
     */
    public void snapshot() {
        if (mAliPlayer != null) {
            mAliPlayer.snapshot();
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mAliPlayer != null) {
            mAliPlayer.stop();
        }
    }

    /**
     * prepare
     */
    public void prepare() {
        if (mAliPlayer != null) {
            mAliPlayer.prepare();
        }
    }

    /**
     * 暂停播放,直播流不建议使用
     */
    public void pause() {
        if (mAliPlayer != null) {
            mAliPlayer.pause();
        }
    }

    public void start() {
        if (mAliPlayer != null) {
            mAliPlayer.start();
        }
    }

    public void reload() {
        if (mAliPlayer != null) {
            mAliPlayer.reload();
        }
    }

    public void reset() {
        if (mAliPlayer != null) {
            mAliPlayer.reset();
        }
    }

    /**
     * 获取视频时长
     */
    public long getDuration() {
        if (mAliPlayer != null) {
            return mAliPlayer.getDuration();
        }
        return 0;
    }

    /**
     * seek
     *
     * @param position 目标位置
     * @param seekMode 精准/非精准seek
     */
    public void seekTo(long position, IPlayer.SeekMode seekMode) {
        if (mAliPlayer != null) {
            mAliPlayer.seekTo(position, seekMode);
        }
    }

    /**
     * 缓存配置
     */
    public void setCacheConfig(CacheConfig cacheConfig) {
        if (mAliPlayer != null) {
            mAliPlayer.setCacheConfig(cacheConfig);
        }
    }

    /**
     * 设置PlayerConfig
     */
    public void setPlayerConfig(PlayerConfig playerConfig) {
        if (mAliPlayer != null) {
            mAliPlayer.setConfig(playerConfig);
        }
    }

    /**
     * 获取PlayerConfig
     */
    public PlayerConfig getPlayerConfig() {
        if (mAliPlayer != null) {
            return mAliPlayer.getConfig();
        }
        return null;
    }

    public MediaInfo getMediaInfo() {
        if (mAliPlayer != null) {
            return mAliPlayer.getMediaInfo();
        }
        return null;
    }

    public void release() {
        if (mAliPlayer != null) {
            stop();
            mAliPlayer.setSurface(null);
            mAliPlayer.release();
            mAliPlayer = null;
        }
    }

    public void setListener(VideoListener videoListener) {
        mAliPlayer.setOnPreparedListener(new VideoPreparedListener(videoListener));
        mAliPlayer.setOnRenderingStartListener(new VideoRenderingStartListener(videoListener));
        mAliPlayer.setOnCompletionListener(new VideoCompletionListener(videoListener));
        mAliPlayer.setOnInfoListener(new VideoInfoListener(videoListener));
        mAliPlayer.setOnStateChangedListener(new VideoStateChangedListener(videoListener));
        mAliPlayer.setOnErrorListener(new VideoErrorListener(videoListener));
        mAliPlayer.setOnVideoRenderedListener(new IPlayer.OnVideoRenderedListener() {
            @Override
            public void onVideoRendered(long l, long l1) {
                videoListener.onVideoRendered(l, l1);
            }
        });

    }

    static class VideoPreparedListener implements IPlayer.OnPreparedListener {

        private WeakReference<VideoListener> mWeakReference;

        public VideoPreparedListener(VideoListener videoListener) {
            mWeakReference = new WeakReference<>(videoListener);
        }

        @Override
        public void onPrepared() {
            VideoListener videoListener = mWeakReference.get();
            if (videoListener != null) {
                videoListener.onVideoPrepared();
            }
        }
    }

    static class VideoRenderingStartListener implements IPlayer.OnRenderingStartListener {

        private WeakReference<VideoListener> mWeakReference;

        public VideoRenderingStartListener(VideoListener videoListener) {
            mWeakReference = new WeakReference<>(videoListener);
        }

        @Override
        public void onRenderingStart() {
            VideoListener videoListener = mWeakReference.get();
            if (videoListener != null) {
                videoListener.onVideoStart();
            }
        }
    }

    static class VideoCompletionListener implements IPlayer.OnCompletionListener {

        private WeakReference<VideoListener> mWeakReference;

        public VideoCompletionListener(VideoListener videoListener) {
            mWeakReference = new WeakReference<>(videoListener);
        }

        @Override
        public void onCompletion() {
            VideoListener videoListener = mWeakReference.get();
            if (videoListener != null) {
                videoListener.onVideoCompletion();
            }
        }
    }

    static class VideoInfoListener implements IPlayer.OnInfoListener {

        private WeakReference<VideoListener> mWeakReference;

        public VideoInfoListener(VideoListener videoListener) {
            mWeakReference = new WeakReference<>(videoListener);
        }

        @Override
        public void onInfo(InfoBean infoBean) {
            VideoListener videoListener = mWeakReference.get();
            if (videoListener != null) {
                videoListener.onVideoInfo(infoBean);
            }
        }
    }

    static class VideoStateChangedListener implements IPlayer.OnStateChangedListener {

        private WeakReference<VideoListener> mWeakReference;

        public VideoStateChangedListener(VideoListener videoListener) {
            mWeakReference = new WeakReference<>(videoListener);
        }

        @Override
        public void onStateChanged(int newState) {
            VideoListener videoListener = mWeakReference.get();
            if (videoListener != null) {
                videoListener.onVideoStateChanged(newState);
            }
        }
    }

    static class VideoErrorListener implements IPlayer.OnErrorListener {

        private WeakReference<VideoListener> mWeakReference;

        public VideoErrorListener(VideoListener videoListener) {
            mWeakReference = new WeakReference<>(videoListener);
        }

        @Override
        public void onError(ErrorInfo errorInfo) {
            VideoListener videoListener = mWeakReference.get();
            if (videoListener != null) {
                videoListener.onVideoError(errorInfo);
            }
        }
    }

    private DragCloseHelper dragCloseHelper;

    public void initDrag(View parentView, View childView, IOnVideoDragListener iOnVideoDragListener) {
        //初始化拖拽返回
        dragCloseHelper = new DragCloseHelper(mContext);
        dragCloseHelper.setShareElementMode(true);
        dragCloseHelper.setDragCloseViews(parentView, childView);
        dragCloseHelper.setClickListener(new DragCloseHelper.ClickListener() {
            @Override
            public void onClick(View view, boolean isLongClick) {
                if (iOnVideoDragListener != null) {
                    iOnVideoDragListener.onClick(view, isLongClick);
                }
            }
        });
        dragCloseHelper.setDragCloseListener(new DragCloseHelper.DragCloseListener() {
            @Override
            public boolean intercept() {
                //默认false 不拦截 如果图片是放大状态，或者处于滑动返回状态，需要拦截
                return false;
            }

            @Override
            public void dragStart() {
                //拖拽开始。可以在此额外处理一些逻辑
                //此处通知之前点击的view重新显示出来
                //RxBus.get().post("updateView", index);
                if (iOnVideoDragListener != null) {
                    iOnVideoDragListener.onDragging(true);
                }
            }

            @Override
            public void dragging(float percent) {
                //拖拽中。percent当前的进度，取值0-1，可以在此额外处理一些逻辑
                if (iOnVideoDragListener != null) {
                    iOnVideoDragListener.onDragging(false);
                }
            }

            @Override
            public void dragCancel() {
                //拖拽取消，会自动复原。可以在此额外处理一些逻辑
            }

            @Override
            public void dragClose(boolean isShareElementMode) {
                //拖拽关闭，如果是共享元素的页面，需要执行activity的onBackPressed方法，注意如果使用finish方法，则返回的时候没有共享元素的返回动画
                if (isShareElementMode) {
                    release();
                    if (iOnVideoDragListener != null) {
                        iOnVideoDragListener.onCLose();
                    }
                }
            }
        });
    }

    public interface IOnVideoDragListener {
        void onCLose();

        void onClick(View view, boolean isLongClick);

        void onDragging(boolean isStart);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragCloseHelper != null) {
            dragCloseHelper.handleEvent(event);
        }
        return true;
    }
}

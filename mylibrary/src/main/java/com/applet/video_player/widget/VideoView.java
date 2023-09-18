package com.applet.video_player.widget;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aliyun.player.IPlayer;
import com.aliyun.player.nativeclass.CacheConfig;
import com.applet.video_player.VideoListener;
import com.applet.video_player.bean.ActionBean;
import com.applet.video_player.bean.VideoBean;
import com.applet.video_player.util.TimeFormater;
import com.applet.library.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.lang.ref.WeakReference;

public class VideoView extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "VideoView";
    private static final String CACHE_DIR_PATH = ".videoCache" + File.separator;

    private static final int WHAT_HIDE = 0;
    private static final int DELAY_TIME = 3 * 1000; //5秒后隐藏

    private Context mContext;
    private RelativeLayout rl_root;
    private AliVideoView video_ali;
    private SimpleDraweeView iv_poster;
    private ImageView iv_play;
    private LinearLayout ll_bottom;
    private ImageView iv_bottom_play;
    private TextView tv_duration;
    private TextView tv_total;
    private SeekBar sb_seek;

    private VideoBean mVideoBean;
    private ActionBean mActionBean;

    private int mPlayStatus = IPlayer.idle;

    public VideoView(Context context) {
        this(context, null);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        View view = inflate(mContext, R.layout.image_browser_view_video, this);
        rl_root = view.findViewById(R.id.rl_root);
        video_ali = view.findViewById(R.id.video_ali);
        iv_poster = view.findViewById(R.id.iv_poster);
        iv_play = view.findViewById(R.id.iv_play);
        ll_bottom = view.findViewById(R.id.ll_bottom_action);
        iv_bottom_play = view.findViewById(R.id.iv_bottom_play);
        tv_duration = view.findViewById(R.id.tv_bottom_duration);
        tv_total = view.findViewById(R.id.tv_bottom_total);
        sb_seek = view.findViewById(R.id.sb_bottom_seek);

        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.mEnable = true;
        cacheConfig.mDir = getDir() + CACHE_DIR_PATH;
        cacheConfig.mMaxDurationS = 30;
        cacheConfig.mMaxSizeMB = 240;
        video_ali.setCacheConfig(cacheConfig);

        iv_bottom_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayStatus == IPlayer.started) {
                    video_ali.pause();
                } else if(mPlayStatus == IPlayer.completion){
                    video_ali.seekTo(0, IPlayer.SeekMode.Accurate);
                    start();
                }else {
                    start();
                }
            }
        });

        iv_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    public void initView(VideoBean videoBean, ActionBean actionBean, AliVideoView.IOnVideoDragListener iOnVideoDragListener) {
        this.mVideoBean = videoBean;
        this.mActionBean = actionBean;

        if (mActionBean.showAction) {
            ll_bottom.setVisibility(View.VISIBLE);
            sb_seek.setOnSeekBarChangeListener(this);
            iv_play.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    start();
                }
            });
            mHideHandler = new HideHandler(this);
        } else {
            ll_bottom.setVisibility(View.GONE);
        }

        if (mActionBean.hasDrag) {
            video_ali.initDrag(this, rl_root, iOnVideoDragListener);
        }

        video_ali.setSurfaceType(AliVideoView.SurfaceType.SURFACE_VIEW);
        video_ali.setLoop(mActionBean.isLoop);
        video_ali.setUrl(mVideoBean.url);
        video_ali.prepare();

        video_ali.setListener(new VideoListener() {
            @Override
            public void onVideoPrepared() {
                long duration = video_ali.getDuration();
                Log.e(TAG, "onVideoPrepared: ----> 播放准备成功 " + duration);
                sb_seek.setMax((int) duration);
                tv_total.setText(TimeFormater.formatMs(duration));
                video_ali.setWH();
            }

            @Override
            public void onVideoStart() {
                Log.e(TAG, "onVideoStart: ----> 视频开始播放");
                iv_poster.setVisibility(View.GONE);
                changeAction(true);
            }

            @Override
            public void onVideoCompletion() {
                Log.e(TAG, "onVideoCompletion: ----> 播放完成");
                video_ali.reset();
                video_ali.seekTo(0, IPlayer.SeekMode.Accurate);
            }

            @Override
            public void onVideoInfo(com.aliyun.player.bean.InfoBean infoBean) {
                //Log.e(TAG, "onVideoInfo: ----> 视频播放其他事件 " + infoBean.getCode() + "  " + infoBean.getExtraValue());
            }

            @Override
            public void onVideoRendered(long timeMs, long pts) {
                long position = pts / 1000;
                sb_seek.setProgress((int) position);
                tv_duration.setText(TimeFormater.formatMs(position));
            }

            @Override
            public void onVideoStateChanged(int newState) {
                Log.e(TAG, "onVideoStateChanged: ----> 播放器状态改变事件 " + newState);
                mPlayStatus = newState;
                changeStatusUi();
            }

            @Override
            public void onVideoError(com.aliyun.player.bean.ErrorInfo errorInfo) {
                Log.e(TAG, "onVideoError: ----> 视频播放出错 " + errorInfo.getCode().getValue());
            }
        });

        if (mActionBean.isFill) {
            video_ali.getAliPlayer().setScaleMode(IPlayer.ScaleMode.SCALE_ASPECT_FILL);
        } else {
            video_ali.getAliPlayer().setScaleMode(IPlayer.ScaleMode.SCALE_ASPECT_FIT);
        }

        if (mActionBean.isStart) {
            start();
        }
    }

    private String getDir() {
        String dir;
        if (Build.VERSION.SDK_INT >= 29) {
            dir = mContext.getExternalFilesDir("") + File.separator + "Media" + File.separator;
        } else {
            dir = Environment.getExternalStorageDirectory() + File.separator + "DCIM"
                    + File.separator + "Camera" + File.separator;
        }
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public void changeStatusUi() {
        if (mPlayStatus ==  IPlayer.started) {
            iv_play.setVisibility(View.GONE);
            iv_bottom_play.setImageResource(R.drawable.image_browser_pause);
        } else {
            iv_play.setVisibility(View.VISIBLE);
            iv_bottom_play.setImageResource(R.drawable.image_browser_play);
            changeAction(true);
        }
    }

    public void changeAction(boolean isShow) {
        if (mActionBean == null) return;
        if (!mActionBean.showAction) return;
        if (isShow && ll_bottom.getVisibility() == View.GONE) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else if (!isShow && ll_bottom.getVisibility() == View.VISIBLE) {
            ll_bottom.setVisibility(GONE);
        }
        if (isShow) {
            hideDelayed();
        }
    }

    public void start() {
        video_ali.start();
        if (mActionBean.showAction && ll_bottom.getVisibility() == View.GONE) {
            ll_bottom.setVisibility(View.VISIBLE);
            hideDelayed();
        }
    }

    public void release(){
        video_ali.release();
    }

    public void pause(){
        video_ali.pause();
        changeAction(true);
    }

    public SimpleDraweeView getPoster() {
        return iv_poster;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;
        Log.e(TAG, "seekBar onProgressChanged: '----> " + progress);
        tv_duration.setText(TimeFormater.formatMs(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "seekBar onStartTrackingTouch: '----> " + seekBar.getProgress());
        video_ali.pause();
        if (mHideHandler != null) {
            mHideHandler.removeMessages(WHAT_HIDE);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "seekBar onStopTrackingTouch: '----> " + seekBar.getProgress());
        video_ali.seekTo(seekBar.getProgress(), IPlayer.SeekMode.Accurate);
        start();
    }

    private class HideHandler extends Handler {
        private WeakReference<VideoView> mVideoViewWeakReference;

        public HideHandler(VideoView videoView) {
            mVideoViewWeakReference = new WeakReference<VideoView>(videoView);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoView videoView = mVideoViewWeakReference.get();
            if (videoView != null) {
                videoView.changeAction(false);
            }
            super.handleMessage(msg);
        }
    }

    private HideHandler mHideHandler;

    private void hideDelayed() {
        if (mHideHandler == null) return;
        if (!mActionBean.showAction) return;
        mHideHandler.removeMessages(WHAT_HIDE);
        mHideHandler.sendEmptyMessageDelayed(WHAT_HIDE, DELAY_TIME);
    }
}

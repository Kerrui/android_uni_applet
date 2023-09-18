package com.applet.image_browser.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.applet.image_browser.IPreviewListener;
import com.applet.image_browser.PreviewInfo;
import com.applet.image_browser.loader.MySimpleTarget;
import com.applet.image_browser.loader.ZoomMediaLoader;
import com.applet.image_browser.photoview2.PhotoViewAttacher;
import com.applet.image_browser.wight.SmoothImageView;
import com.applet.video_player.bean.ActionBean;
import com.applet.video_player.bean.VideoBean;
import com.applet.video_player.widget.AliVideoView;
import com.applet.video_player.widget.VideoView;
import com.example.mylibrary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PreviewFragment extends Fragment {

    private static final String TAG = "PreviewFragment";

    public static PreviewFragment getInstance(PreviewInfo previewInfo, float sensitivity, int position, IPreviewListener iPreviewListener) {
        PreviewFragment previewFragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("p_info", previewInfo);
        bundle.putFloat("p_sensitivity", sensitivity);
        bundle.putInt("p_position", position);
        previewFragment.setArguments(bundle);
        previewFragment.setIPreviewListener(iPreviewListener);
        return previewFragment;
    }

    private static final int HANDLE_THUMBNAIL = 1;
    private static final int HANDLE_PHOTO_SUCCESS = 2;
    private static final int HANDLE_PHOTO_FAIL = 3;

    private View rootView;
    private ProgressBar pb_bar;
    private SmoothImageView img_thumbnail;
    private SmoothImageView iv_smooth;
    private VideoView view_video;

    private PreviewInfo mInfo;
    private float mSensitivity;
    private boolean isVideo;
    protected MySimpleTarget mSimpleTarget;
    private boolean isInitFinish = false;
    private int mPosition;
    private IPreviewListener mIPreviewListener;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == HANDLE_THUMBNAIL) {
                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap != null && !bitmap.isRecycled()) {
                    img_thumbnail.setImageBitmap(bitmap);
                }

            } else if (msg.what == HANDLE_PHOTO_SUCCESS) {
                pb_bar.setVisibility(View.GONE);
                img_thumbnail.setVisibility(View.GONE);

                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap != null && !bitmap.isRecycled()) {
                    if (isVideo) {
                        view_video.getPoster().setImageBitmap((Bitmap) msg.obj);
                    } else {
                        iv_smooth.setImageBitmap((Bitmap) msg.obj);
                    }
                }
                if (isVideo) view_video.changeStatusUi();

            } else if (msg.what == HANDLE_PHOTO_FAIL) {
                pb_bar.setVisibility(View.GONE);
                if (isVideo) view_video.changeStatusUi();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_browser_fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
        isInitFinish = true;
    }

    private void initData() {
        Bundle bundle = getArguments();
        mInfo = bundle.getParcelable("p_info");
        mSensitivity = bundle.getFloat("p_sensitivity");
        mPosition = bundle.getInt("p_position");
        isVideo = mInfo.isVideo;
    }

    private void initView(View view) {
        rootView = view.findViewById(R.id.rootView);
        pb_bar = view.findViewById(R.id.loading_1);
        iv_smooth = view.findViewById(R.id.photoView);
        view_video = view.findViewById(R.id.video_view);
        img_thumbnail = view.findViewById(R.id.img_thumbnail);

        if (mInfo.thumbnailUrl != null) {
            initSmooth(img_thumbnail);
            ZoomMediaLoader.getInstance().getLoader().displayImage(this, mInfo.thumbnailUrl, img_thumbnail, new MySimpleTarget() {
                @Override
                public void onResourceReady(Bitmap bitmap) {
                    Message message = new Message();
                    message.what = HANDLE_THUMBNAIL;
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorRes) {
                }
            });
        }

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#000000"));

        if (!isVideo) {
            rootView.setBackground(gd);
        }


        rootView.setDrawingCacheEnabled(false);
        rootView.setTag(mInfo.url);
        initSmooth(iv_smooth);

        mSimpleTarget = new MySimpleTarget() {
            @Override
            public void onResourceReady(Bitmap bitmap) {
                Message message = new Message();
                message.what = HANDLE_PHOTO_SUCCESS;
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorRes) {
                Message message = new Message();
                message.what = HANDLE_PHOTO_FAIL;
                mHandler.sendMessage(message);
            }
        };

        if (isVideo) {
            iv_smooth.setVisibility(View.INVISIBLE);
            view_video.setVisibility(View.VISIBLE);
            ZoomMediaLoader.getInstance().getLoader().displayImage(this, mInfo.coverUrl, view_video.getPoster(), mSimpleTarget);

            VideoBean videoBean = new VideoBean(mInfo.coverUrl, mInfo.url, 0, 0);
            ActionBean actionBean = new ActionBean(false, false, false, true, true);
            view_video.initView(videoBean, actionBean, new AliVideoView.IOnVideoDragListener() {
                @Override
                public void onCLose() {
                    closeFragment();
                }

                @Override
                public void onClick(View view, boolean isLongClick) {
                    if (isLongClick && mIPreviewListener != null) {
                        mIPreviewListener.onImgLongClick(mPosition);
                    }
                    view_video.pause();
                }

                @Override
                public void onDragging(boolean isStart) {
                    if (isStart) view_video.changeAction(false);
                }
            });

        } else {
            if (mInfo.url.toLowerCase().contains(".gif")) {
                iv_smooth.setZoomable(false);
                ZoomMediaLoader.getInstance().getLoader().displayGifImage(this, mInfo.url, iv_smooth, mSimpleTarget);
            } else {
                ZoomMediaLoader.getInstance().getLoader().displayImage(this, mInfo.url, iv_smooth, mSimpleTarget);
            }
        }
    }

    private void initSmooth(SmoothImageView smoothView) {
        smoothView.setDrawingCacheEnabled(false);
        smoothView.setDrag(false, mSensitivity);
        smoothView.setMinimumScale(0.7f);
        smoothView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (smoothView.checkMinScale()) {
                    closeFragment();
                }
            }
        });
        smoothView.setAlphaChangeListener(new SmoothImageView.OnAlphaChangeListener() {
            @Override
            public void onAlphaChange(int alpha) {
                rootView.setBackgroundColor(getColorWithAlpha(alpha / 255f, Color.BLACK));
            }
        });
        smoothView.setTransformOutListener(new SmoothImageView.OnTransformOutListener() {
            @Override
            public void onTransformOut() {
                closeFragment();
            }
        });
        smoothView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mIPreviewListener != null) {
                    mIPreviewListener.onImgLongClick(mPosition);
                }
                return true;
            }
        });
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void startVideo() {
        if (!isVideo) return;
        if (view_video == null) return;
        view_video.start();
    }

    public void releaseVideo() {
        if (!isVideo) return;
        if (view_video == null) return;
        view_video.release();
    }

    public void closeFragment() {
        ((GPreviewActivity) getActivity()).transformOut();
    }

    private int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    @Override
    public void onStop() {
        ZoomMediaLoader.getInstance().getLoader().onStop(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseVideo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isInitFinish) return;
        if (!getUserVisibleHint()) {
            if (isVideo) {
                view_video.pause();
            }
        }
    }

    public void setIPreviewListener(IPreviewListener IPreviewListener) {
        mIPreviewListener = IPreviewListener;
    }
}

package com.applet.module;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.alibaba.fastjson.JSONObject;
import com.applet.nav_view.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;
import io.dcloud.feature.uniapp.ui.component.UniComponent;
import io.dcloud.feature.uniapp.ui.component.UniComponentProp;

public class NavViewLottie extends UniComponent<LottieAnimationView> {

    private boolean isAutoPlay;
    private int mRepeatMode;
    private int mLoops;
    private int mInitFrame;
    private float mInitProgress;
    private float mInitSpeed;
    private boolean isEventStep;
    private String mSrc;

    private Animator.AnimatorListener mAnimatorListener;
    private LottieOnCompositionLoadedListener mCompositionLoadedListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    public NavViewLottie(UniSDKInstance instance, AbsVContainer parent, AbsComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected LottieAnimationView initComponentHostView(@NonNull Context context) {
        return new LottieAnimationView(context);
    }

    @UniComponentProp(name = "params")
    public void setParams(JSONObject params) {
        initListener();

        getHostView().cancelAnimation();

        isAutoPlay = params.containsKey("autoPlay") ? params.getBoolean("autoPlay") : false;
        // -1 无限
        mLoops = params.containsKey("loops") ? params.getInteger("loops") : -1;
        int repeatMode = params.containsKey("repeatMode") ? params.getInteger("repeatMode") : 1;
        if (repeatMode == 2) {
            mRepeatMode = LottieDrawable.REVERSE;
        } else {
            mRepeatMode = LottieDrawable.RESTART;
        }
        mInitFrame = params.containsKey("initFrame") ? params.getInteger("initFrame") : -1;
        isEventStep = params.containsKey("eventStep") ? params.getBoolean("eventStep") : false;
        mInitProgress = params.containsKey("progress") ? params.getFloatValue("progress") : 0;
        mInitSpeed = params.containsKey("speed") ? params.getFloatValue("speed") : 1;
        mSrc = params.getString("src");

        if (TextUtils.isEmpty(mSrc)) return;

        if (mLoops == -1) {
            getHostView().setRepeatCount(LottieDrawable.INFINITE);
        } else {
            getHostView().setRepeatCount(mLoops);
        }
        getHostView().setRepeatMode(mRepeatMode);
        getHostView().setFrame(mInitFrame);
        getHostView().setProgress(mInitProgress);
        getHostView().setSpeed(mInitSpeed);

        if (mSrc.startsWith("http")) {
            getHostView().setAnimationFromUrl(mSrc);
        } else {
            try {
                File file = new File(mSrc);
                if (!file.isFile()) {
                    eventError("file is empty");
                    return;
                }
                String[] split = mSrc.split("/");
                String cacheKey = split[split.length - 1];

                InputStream is = new FileInputStream(file);
                getHostView().setAnimation(is, cacheKey);
            } catch (Exception e) {
                e.printStackTrace();
                eventError("set path catch " + e.getMessage());
                return;
            }
        }

        if (isAutoPlay) {
            getHostView().playAnimation();
        }
    }

    @UniJSMethod
    public void play() {
        getHostView().playAnimation();
    }

    @UniJSMethod
    public void pause() {
        getHostView().pauseAnimation();
        event(Constants.EVENT_PAUSE);
    }

    @UniJSMethod
    public void resume() {
        getHostView().resumeAnimation();
    }

    @UniJSMethod
    public void stop() {
        getHostView().cancelAnimation();
    }

    @UniJSMethod
    public void setFrame(int frame) {
        getHostView().setFrame(frame);
    }

    @UniJSMethod
    public void setProgress(float progress) {
        getHostView().setProgress(progress);
    }

    @UniJSMethod
    public void setSpeed(float speed) {
        getHostView().setSpeed(speed);
    }

    @UniJSMethod
    public void setEventStep(boolean isEventStep) {
        this.isEventStep = isEventStep;
    }

    private void initListener() {
        if (mAnimatorListener == null) {
            mAnimatorListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    event(Constants.EVENT_PLAY);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    event(Constants.EVENT_FINISH);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    event(Constants.EVENT_REPEAT);
                }
            };
            getHostView().addAnimatorListener(mAnimatorListener);
        }
        if (mCompositionLoadedListener == null) {
            mCompositionLoadedListener = new LottieOnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(LottieComposition composition) {
                    event(Constants.EVENT_LOAD);
                }
            };
            getHostView().addLottieOnCompositionLoadedListener(mCompositionLoadedListener);
        }
        if (mAnimatorUpdateListener == null) {
            mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (!isEventStep) return;
                    Map<String, Object> params = new HashMap<>();
                    params.put("status", Constants.EVENT_STEP);
                    params.put("percentage", animation.getAnimatedFraction());
                    event(params);
                }
            };
            getHostView().addAnimatorUpdateListener(mAnimatorUpdateListener);
        }
    }

    private void eventError(String msg) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", Constants.EVENT_ERROR);
        params.put("msg", msg);
        event(params);
    }

    private void event(int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        event(params);
    }

    private void event(Map<String, Object> params) {
        Map<String, Object> backMap = new HashMap<>();
        backMap.put("detail", params);
        fireEvent("event", backMap);
    }
}

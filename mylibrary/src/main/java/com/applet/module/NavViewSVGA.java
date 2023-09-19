package com.applet.module;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.applet.nav_view.Constants;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;
import io.dcloud.feature.uniapp.ui.component.UniComponent;
import io.dcloud.feature.uniapp.ui.component.UniComponentProp;

public class NavViewSVGA extends UniComponent<SVGAImageView> {

    private static final String TAG = "NavAnimSVGA";

    private SVGAParser mSVGAParser;
    private boolean isAutoPlay;
    private int mLoops;
    private SVGAImageView.FillMode mFillMode;
    private int mInitFrame;
    private boolean isEventStep;
    private String mSrc;

    private boolean isSetSrc;
    private SVGAParser.ParseCompletion mParseCompletion;
    private SVGACallback mSVGACallback;

    public NavViewSVGA(UniSDKInstance instance, AbsVContainer parent, AbsComponentData componentData) {
        super(instance, parent, componentData);
    }

    @Override
    protected SVGAImageView initComponentHostView(@NonNull Context context) {
        SVGAImageView svgaImageView = new SVGAImageView(context);
        return svgaImageView;
    }

    @UniComponentProp(name = "params")
    public void setParams(JSONObject params) {
        if (mSVGAParser == null) {
            mSVGAParser = SVGAParser.Companion.shareParser();
        }
        initListener();

        getHostView().stopAnimation();

        isAutoPlay = params.containsKey("autoPlay") ? params.getBoolean("autoPlay") : false;
        // 0 无限
        mLoops = params.containsKey("loops") ? params.getInteger("loops") : 1;
        int fillModeInt = params.containsKey("fillMode") ? params.getInteger("fillMode") : 0;
        if (fillModeInt == 0) {
            mFillMode = SVGAImageView.FillMode.Backward;
        } else if (fillModeInt == 1) {
            mFillMode = SVGAImageView.FillMode.Forward;
        } else if (fillModeInt == 2) {
            mFillMode = SVGAImageView.FillMode.Clear;
        } else {
            mFillMode = SVGAImageView.FillMode.Backward;
        }
        mInitFrame = params.containsKey("initFrame") ? params.getInteger("initFrame") : -1;
        isEventStep = params.containsKey("eventStep") ? params.getBoolean("eventStep") : false;
        mSrc = params.getString("src");
        if (TextUtils.isEmpty(mSrc)) return;

        getHostView().setLoops(mLoops);
        getHostView().setFillMode(mFillMode);
        if (mInitFrame != -1) {
            getHostView().stepToFrame(mInitFrame, false);
        }

        if (mSrc.startsWith("http")) {
            decodeFromURL();
        } else {
            decodeFromInputStream();
        }
    }

    private void initListener() {
        if (mParseCompletion == null) {
            mParseCompletion = new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                    getHostView().setVideoItem(svgaVideoEntity);
                    event(Constants.EVENT_LOAD);
                    if (isAutoPlay) {
                        play();
                    }
                }

                @Override
                public void onError() {
                    eventError("ParseCompletion error");
                }
            };
        }

        if (mSVGACallback == null) {
            mSVGACallback = new SVGACallback() {
                @Override
                public void onPause() {
                    event(Constants.EVENT_PAUSE);
                }

                @Override
                public void onFinished() {
                    event(Constants.EVENT_FINISH);
                }

                @Override
                public void onRepeat() {
                    event(Constants.EVENT_REPEAT);
                }

                @Override
                public void onStep(int frame, double percentage) {
                    if (!isEventStep) return;
                    Map<String, Object> params = new HashMap<>();
                    params.put("status", Constants.EVENT_STEP);
                    params.put("frame", frame);
                    params.put("percentage", percentage);
                    event(params);
                }
            };
            getHostView().setCallback(mSVGACallback);
        }
    }

    @UniJSMethod
    public void play() {
        getHostView().startAnimation();
        event(Constants.EVENT_PLAY);
    }

    @UniJSMethod
    public void pause() {
        getHostView().pauseAnimation();
    }

    @UniJSMethod
    public void stop() {
        getHostView().stopAnimation();
    }

    @UniJSMethod
    public void stepToFrame(int frame, boolean andPlay) {
        getHostView().stepToFrame(frame, andPlay);
    }

    @UniJSMethod
    public void setEventStep(boolean isEventStep) {
        this.isEventStep = isEventStep;
    }

    private void decodeFromURL() {
        try {
            mSVGAParser.decodeFromURL(new URL(mSrc), mParseCompletion, null);
        } catch (Exception e) {
            e.printStackTrace();
            eventError("decodeFromURL");
        }
    }

    private void decodeFromInputStream() {
        try {
            File file = new File(mSrc);
            if (!file.isFile()) {
                eventError("file is empty");
                return;
            }
            String[] split = mSrc.split("/");
            String cacheKey = split[split.length - 1];

            InputStream is = new FileInputStream(file);
            mSVGAParser.decodeFromInputStream(is, cacheKey, mParseCompletion, true, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            eventError("set path catch " + e.getMessage());
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

package com.applet.mylibrary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.adjust.sdk.Adjust;

/**
 * Created by Alien-super on 2023/11/16.
 */
public class AlienApplicationListener implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "ApplicationListener";

    private int mForegroundCount = 0;
    private int mBufferCount = 0;

    private IMyApplicationListener mIMyApplicationListener;

    public AlienApplicationListener(IMyApplicationListener IMyApplicationListener) {
        mIMyApplicationListener = IMyApplicationListener;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mForegroundCount <= 0) {
            Log.e(TAG, "onActivityStarted: 恢复到前台 ");
            if (mIMyApplicationListener != null) {
                mIMyApplicationListener.onAppForegroundChange(false);
            }
        }

        if (mBufferCount < 0) {
            mBufferCount++;
        } else {
            mForegroundCount++;
        }
        if (mIMyApplicationListener != null) {
            mIMyApplicationListener.onActivityChange("started", activity, null);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Adjust.onResume();
        if (mIMyApplicationListener != null) {
            mIMyApplicationListener.onActivityChange("resumed", activity, null);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Adjust.onPause();
        if (mIMyApplicationListener != null) {
            mIMyApplicationListener.onActivityChange("paused", activity, null);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity.isChangingConfigurations()) {
            mBufferCount--;
        } else {
            mForegroundCount--;
            if (mForegroundCount <= 0) {
                Log.e(TAG, "onActivityStopped: 应用进入到后台 ");
                if (mIMyApplicationListener != null) {
                    mIMyApplicationListener.onAppForegroundChange(true);
                }
            }
        }
        if (mIMyApplicationListener != null) {
            mIMyApplicationListener.onActivityChange("stopped", activity, null);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (mIMyApplicationListener != null) {
            mIMyApplicationListener.onActivityChange("saveInstanceState", activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mIMyApplicationListener != null) {
            mIMyApplicationListener.onActivityChange("destroyed", activity, null);
        }
    }

    public interface IMyApplicationListener {
        void onActivityChange(String circle, Activity activity, Bundle bundle);

        void onAppForegroundChange(boolean isForeground);
    }
}

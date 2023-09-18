package com.applet.image_browser.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.applet.image_browser.IPreviewListener;
import com.applet.image_browser.PreviewInfo;
import com.applet.image_browser.loader.ZoomMediaLoader;
import com.applet.image_browser.wight.PhotoViewPager;
import com.applet.image_browser.wight.SmoothImageView;
import com.example.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class GPreviewActivity extends FragmentActivity {

    private static final String TAG = "GPreviewActivity";

    public static IPreviewListener sIPreviewListener;

    private FrameLayout fl_root;
    private PhotoViewPager view_pager;
    private TextView tv_dot;

    private List<PreviewInfo> mPreviewList;
    private int mIndex;
    private float mSensitivity;
    private boolean isShowIndicator;
    private boolean isFullscreen;
    private List<PreviewFragment> mFragmentList = new ArrayList<>();
    private PreviewFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser_act_p_preview);

        initData();
        initFragment();
        initSmoothImage();
        initView();
        if (sIPreviewListener != null) {
            sIPreviewListener.onCreate(mFragment);
        }
    }

    private void initData() {
        mPreviewList = getIntent().getParcelableArrayListExtra("previewInfo");
        mIndex = getIntent().getIntExtra("index", 0);
        mSensitivity = getIntent().getFloatExtra("sensitivity", 0.5F);
        isShowIndicator = getIntent().getBooleanExtra("indicator", true);
        isFullscreen = getIntent().getBooleanExtra("isFullscreen", true);
    }

    private void initView(){
        if (isFullscreen) {
            setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        }
        view_pager = findViewById(R.id.viewPager);
        tv_dot = findViewById(R.id.ltAddDot);
        fl_root = findViewById(R.id.fl_root);

        if (isShowIndicator) {
            tv_dot.setVisibility(View.VISIBLE);
            tv_dot.setText((mIndex + 1) + "/" + mPreviewList.size());
        } else {
            tv_dot.setVisibility(View.GONE);
        }

        PreviewPagerAdapter pagerAdapter = new PreviewPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(pagerAdapter);
        view_pager.setCurrentItem(mIndex);
        view_pager.setOffscreenPageLimit(3);
        view_pager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "onGlobalLayout: '-----> onGlobalLayout " + mIndex);
                view_pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (mFragment.isVideo()) {
                    mFragment.startVideo();
                }
            }
        });
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                mFragment = mFragmentList.get(mIndex);
                if (tv_dot != null) {
                    tv_dot.setText((mIndex + 1) + "/" + mPreviewList.size());
                }
                view_pager.setCurrentItem(mIndex, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //scrolling = state != 0;
            }
        });
    }

    private void initSmoothImage(){
        SmoothImageView.setFullscreen(isFullscreen);
        SmoothImageView.setIsScale(true);
        SmoothImageView.setDuration(300);
    }

    private void initFragment() {
        if (mPreviewList == null || mPreviewList.size() <= 0) {
            finish();
            return;
        }
        int size = mPreviewList.size();
        for (int i = 0; i < size; i++) {
            mFragmentList.add(PreviewFragment.getInstance(mPreviewList.get(i), mSensitivity, i, sIPreviewListener));
        }
        mFragment = mFragmentList.get(mIndex);
    }

    public void transformOut() {
        this.finish();
    }

    private class PreviewPagerAdapter extends FragmentStatePagerAdapter {

        public PreviewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }
    }

    @Override
    public void finish() {
        if (sIPreviewListener != null) {
            sIPreviewListener.onDestroy();
        }
        sIPreviewListener = null;
        super.finish();
    }

    @Override
    protected void onDestroy() {
        ZoomMediaLoader.getInstance().getLoader().clearMemory(this);
        if (view_pager != null) {
            view_pager.setAdapter(null);
            view_pager.clearOnPageChangeListeners();
            view_pager.removeAllViews();
            view_pager = null;
        }
        if (mFragmentList != null) {
            mFragmentList.clear();
            mFragmentList = null;
        }
        if (mPreviewList != null) {
            mPreviewList.clear();
            mPreviewList = null;
        }
        super.onDestroy();
    }
}

package com.applet.feature

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.dcloud.feature.sdk.Interface.IDCUniMPAppSplashView

class CSplash: IDCUniMPAppSplashView {

    private lateinit var imageView: ImageView
    override fun getSplashView(context: Context, p1: String?, p2: String?, p3: String?): View {
        imageView = ImageView(context)
        imageView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(imageView).load(LibConstant.SPLASH_IMG_PATH).into(imageView)
        return imageView;
    }

    override fun onCloseSplash(p0: ViewGroup?) {
        p0?.removeView(imageView)
    }

}
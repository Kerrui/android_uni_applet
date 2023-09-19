package com.applet.module;

import android.content.Context;
import android.graphics.Color;

import com.applet.nav_view.blur.RealtimeBlurView;

import androidx.annotation.NonNull;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;
import io.dcloud.feature.uniapp.ui.component.UniComponent;
import io.dcloud.feature.uniapp.ui.component.UniComponentProp;

public class NavViewBlur extends UniComponent<RealtimeBlurView> {

    private static final String TAG = "NavBlurView";

    public NavViewBlur(UniSDKInstance instance, AbsVContainer parent, AbsComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected RealtimeBlurView initComponentHostView(@NonNull Context context) {
        return new RealtimeBlurView(context, null);
    }

    @UniComponentProp(name = "radius")
    public void setRadius(int radius) {
        getHostView().setBlurRadius(radius);
    }

    @UniComponentProp(name = "color")
    public void setColor(String color) {
        getHostView().setOverlayColor(Color.parseColor(color));
    }
}

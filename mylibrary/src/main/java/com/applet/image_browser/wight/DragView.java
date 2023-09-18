package com.applet.image_browser.wight;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.applet.video_player.util.DragCloseHelper;


public class DragView extends RelativeLayout {

    private Context mContext;
    private DragCloseHelper dragCloseHelper;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
    }

}

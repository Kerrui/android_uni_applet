package com.applet.video_player.bean;

public class ActionBean {

    public boolean isFill;
    public boolean isLoop;
    public boolean isStart;
    public boolean showAction;
    public boolean hasDrag;

    public ActionBean(boolean isFill, boolean isLoop, boolean isStart, boolean showAction, boolean hasDrag) {
        this.isFill = isFill;
        this.isLoop = isLoop;
        this.isStart = isStart;
        this.showAction = showAction;
        this.hasDrag = hasDrag;
    }

    public ActionBean(boolean isFill, boolean isLoop, boolean isStart, boolean showAction) {
        this.isFill = isFill;
        this.isLoop = isLoop;
        this.isStart = isStart;
        this.showAction = showAction;
        this.hasDrag = false;
    }
}

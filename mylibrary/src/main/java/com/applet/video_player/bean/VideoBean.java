package com.applet.video_player.bean;

public class VideoBean {

    public String poster;
    public String url;
    public int height;
    public int width;

    public VideoBean(String url, String poster) {
        this.url = url;
        this.poster = poster;
        this.width = 0;
        this.height = 0;
    }

    public VideoBean(String poster, String url, int height, int width) {
        this.poster = poster;
        this.url = url;
        this.height = height;
        this.width = width;
    }
}

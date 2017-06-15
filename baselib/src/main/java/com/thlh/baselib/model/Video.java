package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/9/6.
 */
public class Video {


    /**
     * id : 109
     * url : http://cdn.mjmw365.com/v_17.mov
     * video_name : 五粮液喜气临人臻品
     * video_pic : http://v2.mjmw365.com/images/video/vp_17.jpg
     * duration :
     * for_pad : 1
     * for_app : 1
     */

    private String id;
    private String url;
    private String video_name;
    private String video_pic;
    private String duration;
    private String for_pad;
    private String for_app;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_pic() {
        return video_pic;
    }

    public void setVideo_pic(String video_pic) {
        this.video_pic = video_pic;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFor_pad() {
        return for_pad;
    }

    public void setFor_pad(String for_pad) {
        this.for_pad = for_pad;
    }

    public String getFor_app() {
        return for_app;
    }

    public void setFor_app(String for_app) {
        this.for_app = for_app;
    }
}

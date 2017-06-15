package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/4/25.
 */
public class GoodsDetailPic extends Goods{


    /**
     * url : /images/item/YzM/5OT/g2M/1466529333967.png
     * for_pad : 1
     * for_app : 1
     */

    private String url;
    private String for_pad;
    private String for_app;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ChoicenessGoods {

    private int imgResource;
    private String catid;
    private String title;
    private String subtitle;

    public ChoicenessGoods( String title,String catid,int imgResource,String subtitle) {
        this.imgResource = imgResource;
        this.catid = catid;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}

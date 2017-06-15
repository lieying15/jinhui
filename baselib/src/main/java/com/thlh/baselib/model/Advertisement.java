package com.thlh.baselib.model;

/**
 * Created by HQ on 2015/9/18.
 */
public class Advertisement {

    private String code;
    private int adId;
    private int placeId;
    private String media;
    private int pageId;
    private String title;
    private int type;
    private String url;
    private String desc;
    private String showMonitoringCode;
    private String clickMonitoringCode;

    public String getShowMonitoringCode() {
        return showMonitoringCode;
    }

    public void setShowMonitoringCode(String showMonitoringCode) {
        this.showMonitoringCode = showMonitoringCode;
    }

    public String getClickMonitoringCode() {
        return clickMonitoringCode;
    }

    public void setClickMonitoringCode(String clickMonitoringCode) {
        this.clickMonitoringCode = clickMonitoringCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public int getAdId() {
        return adId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getMedia() {
        return media;
    }

    public int getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc() {
        return desc;
    }
}

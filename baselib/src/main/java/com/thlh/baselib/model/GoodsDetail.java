package com.thlh.baselib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class GoodsDetail extends Goods{
//public class GoodsDetail extends GoodsDb{

    private String content;
    private String collection;

    private List<GoodsDetailProperty> spec;
    private List<GoodsMedia> media;

    private List<GoodsDetailPic> pic;
    private List<Video> video;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public List<GoodsDetailProperty> getSpec() {
        return spec;
    }

    public void setSpec(List<GoodsDetailProperty> spec) {
        this.spec = spec;
    }

    public List<GoodsMedia> getMedia() {
        return media;
    }

    public void setMedia(List<GoodsMedia> media) {
        this.media = media;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public List<GoodsDetailPic> getPic() {
        return pic;
    }

    public void setPic(List<GoodsDetailPic> pic) {
        this.pic = pic;
    }

    public List<Video> getVideo() {
        return video;
    }

    public void setVideo(List<Video> video) {
        this.video = video;
    }


}
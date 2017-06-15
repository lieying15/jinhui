package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class GoodsComment implements Parcelable {
    private String order_id;
    private String item_id;
    private String item_num;
    private String price;
    private String item_name;
    private String item_img;
    private String item_img_thumb;
    /**
     * rate : 2
     * comment : 这个商品真的很好
     * inputtime : 1467650771
     * answer :
     */

    private Comment comment;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getItem_img_thumb() {
        return item_img_thumb;
    }

    public void setItem_img_thumb(String item_img_thumb) {
        this.item_img_thumb = item_img_thumb;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.item_id);
        dest.writeString(this.item_num);
        dest.writeString(this.price);
        dest.writeString(this.item_name);
        dest.writeString(this.item_img);
        dest.writeString(this.item_img_thumb);
        dest.writeParcelable(this.comment, flags);
    }

    public GoodsComment() {
    }

    protected GoodsComment(Parcel in) {
        this.order_id = in.readString();
        this.item_id = in.readString();
        this.item_num = in.readString();
        this.price = in.readString();
        this.item_name = in.readString();
        this.item_img = in.readString();
        this.item_img_thumb = in.readString();
        this.comment = in.readParcelable(Comment.class.getClassLoader());
    }

    public static final Parcelable.Creator<GoodsComment> CREATOR = new Parcelable.Creator<GoodsComment>() {
        @Override
        public GoodsComment createFromParcel(Parcel source) {
            return new GoodsComment(source);
        }

        @Override
        public GoodsComment[] newArray(int size) {
            return new GoodsComment[size];
        }
    };
}

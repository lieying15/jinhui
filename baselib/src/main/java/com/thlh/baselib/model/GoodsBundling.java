package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class GoodsBundling implements Parcelable {
    private String bundling_id;
    private String item_id;
    private String item_name;
    private String item_img;
    private String item_thumb;
    private String begin;
    private String end;
    private int storage;
    private String item_price;


    private List<GoodsBundlingItem> items;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getBundling_id() {
        return bundling_id;
    }

    public void setBundling_id(String bundling_id) {
        this.bundling_id = bundling_id;
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

    public String getItem_thumb() {
        return item_thumb;
    }

    public void setItem_thumb(String item_thumb) {
        this.item_thumb = item_thumb;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }


    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public List<GoodsBundlingItem> getItem() {
        return items;
    }

    public void setItem(List<GoodsBundlingItem> items) {
        this.items = items;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.item_id);
        dest.writeString(this.bundling_id);
        dest.writeString(this.item_name);
        dest.writeString(this.item_img);
        dest.writeString(this.item_thumb);
        dest.writeString(this.begin);
        dest.writeString(this.end);
        dest.writeInt(this.storage);
        dest.writeString(this.item_price);
        dest.writeTypedList(this.items);
    }

    public GoodsBundling() {
    }

    protected GoodsBundling(Parcel in) {
        this.item_id = in.readString();
        this.bundling_id = in.readString();
        this.item_name = in.readString();
        this.item_img = in.readString();
        this.item_thumb = in.readString();
        this.begin = in.readString();
        this.end = in.readString();
        this.storage = in.readInt();
        this.item_price = in.readString();
        this.items = in.createTypedArrayList(GoodsBundlingItem.CREATOR);
    }

    public static final Creator<GoodsBundling> CREATOR = new Creator<GoodsBundling>() {
        @Override
        public GoodsBundling createFromParcel(Parcel source) {
            return new GoodsBundling(source);
        }

        @Override
        public GoodsBundling[] newArray(int size) {
            return new GoodsBundling[size];
        }
    };
}

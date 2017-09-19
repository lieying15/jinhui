package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class GoodsBundling implements Parcelable {
    private String begin;
    private String end;
    private String price;
    private String storage;
    private String frozen_storage;
    private String revoke;
    private String reject;
    private String item_id;
    private String item_status;
    private String bundling_id;
    private String item_name;
    private String item_img;
    private String item_thumb;
    private List<GoodsBundlingItem> items;
    private GoodsDetail bundle;

    protected GoodsBundling(Parcel in) {
        begin = in.readString();
        end = in.readString();
        price = in.readString();
        storage = in.readString();
        frozen_storage = in.readString();
        revoke = in.readString();
        reject = in.readString();
        item_id = in.readString();
        item_status = in.readString();
        bundling_id = in.readString();
        item_name = in.readString();
        item_img = in.readString();
        item_thumb = in.readString();
        items = in.createTypedArrayList(GoodsBundlingItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(begin);
        dest.writeString(end);
        dest.writeString(price);
        dest.writeString(storage);
        dest.writeString(frozen_storage);
        dest.writeString(revoke);
        dest.writeString(reject);
        dest.writeString(item_id);
        dest.writeString(item_status);
        dest.writeString(bundling_id);
        dest.writeString(item_name);
        dest.writeString(item_img);
        dest.writeString(item_thumb);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsBundling> CREATOR = new Creator<GoodsBundling>() {
        @Override
        public GoodsBundling createFromParcel(Parcel in) {
            return new GoodsBundling(in);
        }

        @Override
        public GoodsBundling[] newArray(int size) {
            return new GoodsBundling[size];
        }
    };

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getFrozen_storage() {
        return frozen_storage;
    }

    public void setFrozen_storage(String frozen_storage) {
        this.frozen_storage = frozen_storage;
    }

    public String getRevoke() {
        return revoke;
    }

    public void setRevoke(String revoke) {
        this.revoke = revoke;
    }

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
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

    public List<GoodsBundlingItem> getItem() {
        return items;
    }

    public void setItem(List<GoodsBundlingItem> items) {
        this.items = items;
    }

    public GoodsDetail getBundle() {
        return bundle;
    }

    public void setBundle(GoodsDetail bundle) {
        this.bundle = bundle;
    }

    public static Creator<GoodsBundling> getCREATOR() {
        return CREATOR;
    }
}

package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class GoodsSupplier implements Parcelable {

    /**
     * id : 1
     * store_name : 供货商1
     */

    private String id;
    private String store_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.store_name);
    }

    public GoodsSupplier() {
    }

    protected GoodsSupplier(Parcel in) {
        this.id = in.readString();
        this.store_name = in.readString();
    }

    public static final Parcelable.Creator<GoodsSupplier> CREATOR = new Parcelable.Creator<GoodsSupplier>() {
        @Override
        public GoodsSupplier createFromParcel(Parcel source) {
            return new GoodsSupplier(source);
        }

        @Override
        public GoodsSupplier[] newArray(int size) {
            return new GoodsSupplier[size];
        }
    };
}

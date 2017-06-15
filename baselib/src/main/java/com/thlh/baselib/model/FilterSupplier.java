package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/8/11.
 */
public class FilterSupplier implements Parcelable {

    /**
     * id : 3
     * name : 惠家有
     * store_name : 供货商3
     */

    private String id;
    private String name;
    private String store_name;
    private String sortLetters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.store_name);
        dest.writeString(this.sortLetters);
    }

    public FilterSupplier() {
    }

    protected FilterSupplier(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.store_name = in.readString();
        this.sortLetters = in.readString();
    }

    public static final Parcelable.Creator<FilterSupplier> CREATOR = new Parcelable.Creator<FilterSupplier>() {
        @Override
        public FilterSupplier createFromParcel(Parcel source) {
            return new FilterSupplier(source);
        }

        @Override
        public FilterSupplier[] newArray(int size) {
            return new FilterSupplier[size];
        }
    };
}

package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/27.
 */
    public class Brand implements Parcelable {
    private String brand_id;
    private String brand;
    private String sortLetters;

    public String getBrandid() {
        return brand_id;
    }

    public void setBrandid(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
        dest.writeString(this.brand_id);
        dest.writeString(this.brand);
        dest.writeString(this.sortLetters);
    }

    public Brand() {
    }

    protected Brand(Parcel in) {
        this.brand_id = in.readString();
        this.brand = in.readString();
        this.sortLetters = in.readString();
    }

    public static final Parcelable.Creator<Brand> CREATOR = new Parcelable.Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
}

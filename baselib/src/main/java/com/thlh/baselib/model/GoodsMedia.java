package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/6/21.
 */
public class GoodsMedia implements Parcelable {
    private String url;
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.type);
    }

    public GoodsMedia() {
    }

    protected GoodsMedia(Parcel in) {
        this.url = in.readString();
        this.type = in.readString();
    }

    public static final Creator<GoodsMedia> CREATOR = new Creator<GoodsMedia>() {
        @Override
        public GoodsMedia createFromParcel(Parcel source) {
            return new GoodsMedia(source);
        }

        @Override
        public GoodsMedia[] newArray(int size) {
            return new GoodsMedia[size];
        }
    };
}

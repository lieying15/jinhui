package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class OrderPack implements Parcelable {

    private String express_no;
    private String express_time;
    private String company;
    private String tel;

    private Track track;
    private List<GoodsOrder> item;

    public String getExpress_time() {
        return express_time;
    }

    public void setExpress_time(String express_time) {
        this.express_time = express_time;
    }

    public String getExpress_no() {
        return express_no;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public List<GoodsOrder> getItem() {
        return item;
    }

    public void setItem(List<GoodsOrder> item) {
        this.item = item;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.express_no);
        dest.writeString(this.express_time);
        dest.writeString(this.company);
        dest.writeString(this.tel);
        dest.writeParcelable(this.track, flags);
        dest.writeTypedList(this.item);
    }

    public OrderPack() {
    }

    protected OrderPack(Parcel in) {
        this.express_no = in.readString();
        this.express_time = in.readString();
        this.company = in.readString();
        this.tel = in.readString();
        this.track = in.readParcelable(Track.class.getClassLoader());
        this.item = in.createTypedArrayList(GoodsOrder.CREATOR);
    }

    public static final Creator<OrderPack> CREATOR = new Creator<OrderPack>() {
        @Override
        public OrderPack createFromParcel(Parcel source) {
            return new OrderPack(source);
        }

        @Override
        public OrderPack[] newArray(int size) {
            return new OrderPack[size];
        }
    };
}

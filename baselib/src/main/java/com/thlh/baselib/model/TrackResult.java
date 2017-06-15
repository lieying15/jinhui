package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class TrackResult implements Parcelable {
    private String no;
    private boolean ischeck;
    private String com;
    private String company;
    private String updatetime;
    /**
     * time : 2016-07-22 10:26:15
     * context : 在北京海淀区永泰公司进行签收扫描，快件已被 已签收 签收
     */

    private List<TrackData> data;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public List<TrackData> getData() {
        return data;
    }

    public void setData(List<TrackData> data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.no);
        dest.writeByte(this.ischeck ? (byte) 1 : (byte) 0);
        dest.writeString(this.com);
        dest.writeString(this.company);
        dest.writeString(this.updatetime);
        dest.writeTypedList(this.data);
    }

    public TrackResult() {
    }

    protected TrackResult(Parcel in) {
        this.no = in.readString();
        this.ischeck = in.readByte() != 0;
        this.com = in.readString();
        this.company = in.readString();
        this.updatetime = in.readString();
        this.data = in.createTypedArrayList(TrackData.CREATOR);
    }

    public static final Parcelable.Creator<TrackResult> CREATOR = new Parcelable.Creator<TrackResult>() {
        @Override
        public TrackResult createFromParcel(Parcel source) {
            return new TrackResult(source);
        }

        @Override
        public TrackResult[] newArray(int size) {
            return new TrackResult[size];
        }
    };
}

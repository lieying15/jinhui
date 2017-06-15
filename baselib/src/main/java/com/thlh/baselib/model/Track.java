package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/23.
 */
public class Track implements Parcelable {

    /**
     * error_code : 0
     * reason : 成功
     * result : {"no":"3944620313823","ischeck":true,"com":"yunda","company":"韵达快递","updatetime":"2016-07-23 16:05:27","data":[{"time":"2016-07-22 10:26:15","context":"在北京海淀区永泰公司进行签收扫描，快件已被 已签收 签收"},{"time":"2016-07-22 07:59:55","context":"在北京海淀区永泰公司进行派件扫描；派送业务员：张洪松；联系电话：18519295470"},{"time":"2016-07-22 07:10:37","context":"到达目的地网点北京海淀区永泰公司，快件将很快进行派送"},{"time":"2016-07-21 23:54:04","context":"从北京分拨中心发出，本次转运目的地：北京海淀区永泰公司"},{"time":"2016-07-21 22:07:21","context":"在分拨中心北京分拨中心进行卸车扫描"},{"time":"2016-07-20 21:49:06","context":"在浙江杭州分拨中心进行装车扫描，即将发往：北京分拨中心"},{"time":"2016-07-20 21:46:44","context":"在分拨中心浙江杭州分拨中心进行称重扫描"},{"time":"2016-07-20 21:44:23","context":"在浙江杭州分拨中心进行中转集包扫描，将发往：北京网点包"},{"time":"2016-07-20 21:31:22","context":"在分拨中心浙江杭州分拨中心进行称重扫描"},{"time":"2016-07-20 17:04:51","context":"在浙江市场部淘米公司进行到件扫描"}]}
     */

    private int error_code;
    private String reason;
    /**
     * no : 3944620313823
     * ischeck : true
     * com : yunda
     * company : 韵达快递
     * updatetime : 2016-07-23 16:05:27
     * data : [{"time":"2016-07-22 10:26:15","context":"在北京海淀区永泰公司进行签收扫描，快件已被 已签收 签收"},{"time":"2016-07-22 07:59:55","context":"在北京海淀区永泰公司进行派件扫描；派送业务员：张洪松；联系电话：18519295470"},{"time":"2016-07-22 07:10:37","context":"到达目的地网点北京海淀区永泰公司，快件将很快进行派送"},{"time":"2016-07-21 23:54:04","context":"从北京分拨中心发出，本次转运目的地：北京海淀区永泰公司"},{"time":"2016-07-21 22:07:21","context":"在分拨中心北京分拨中心进行卸车扫描"},{"time":"2016-07-20 21:49:06","context":"在浙江杭州分拨中心进行装车扫描，即将发往：北京分拨中心"},{"time":"2016-07-20 21:46:44","context":"在分拨中心浙江杭州分拨中心进行称重扫描"},{"time":"2016-07-20 21:44:23","context":"在浙江杭州分拨中心进行中转集包扫描，将发往：北京网点包"},{"time":"2016-07-20 21:31:22","context":"在分拨中心浙江杭州分拨中心进行称重扫描"},{"time":"2016-07-20 17:04:51","context":"在浙江市场部淘米公司进行到件扫描"}]
     */

    private TrackResult result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TrackResult getResult() {
        return result;
    }

    public void setResult(TrackResult result) {
        this.result = result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.error_code);
        dest.writeString(this.reason);
        dest.writeParcelable(this.result, flags);
    }

    public Track() {
    }

    protected Track(Parcel in) {
        this.error_code = in.readInt();
        this.reason = in.readString();
        this.result = in.readParcelable(TrackResult.class.getClassLoader());
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}

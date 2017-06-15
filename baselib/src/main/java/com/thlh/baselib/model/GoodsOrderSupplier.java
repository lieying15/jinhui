package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huqiang on 2016/12/17.
 */

public class GoodsOrderSupplier implements Parcelable {


    /**
     * id : 61
     * name : 北京合众优品商贸有限公司
     * store_name : 合众优品
     * contact : 李明
     * mobile : 18611954072
     * phone :
     * email :
     */

    private String id;
    private String name;
    private String store_name;
    private String contact;
    private String mobile;
    private String phone;
    private String email;

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        dest.writeString(this.contact);
        dest.writeString(this.mobile);
        dest.writeString(this.phone);
        dest.writeString(this.email);
    }

    public GoodsOrderSupplier() {
    }

    protected GoodsOrderSupplier(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.store_name = in.readString();
        this.contact = in.readString();
        this.mobile = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<GoodsOrderSupplier> CREATOR = new Parcelable.Creator<GoodsOrderSupplier>() {
        @Override
        public GoodsOrderSupplier createFromParcel(Parcel source) {
            return new GoodsOrderSupplier(source);
        }

        @Override
        public GoodsOrderSupplier[] newArray(int size) {
            return new GoodsOrderSupplier[size];
        }
    };
}

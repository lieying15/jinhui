package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Address implements Parcelable ,Comparable<Address>{

    /**
     * name : 葫芦娃
     * address : 富丽双子座
     * phone : 13240396344
     * is_on : 1
     */

    private String name;
    private String address;
    private String phone;
    private String is_on;
    private String id;
    private String province ;
    private String city ;
    private String district ;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_on() {
        return is_on;
    }

    public void setIs_on(String is_on) {
        this.is_on = is_on;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.is_on);
        dest.writeString(this.id);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.is_on = in.readString();
        this.id = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
    }


    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int compareTo(Address address) {
        return this.id.compareTo(address.is_on);
    }
}

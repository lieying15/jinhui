package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2016/9/6.
 */
@Entity
public class Supplier  implements Parcelable {
    @Id
    private Long dbid ;

    private String id;
    private String name;
    private String store_name;
    private String contact;
    private String mobile;
    private String phone;
    private String email;
    @Generated(hash = 343056983)
    public Supplier(Long dbid, String id, String name, String store_name,
            String contact, String mobile, String phone, String email) {
        this.dbid = dbid;
        this.id = id;
        this.name = name;
        this.store_name = store_name;
        this.contact = contact;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
    }
    @Generated(hash = 1051229294)
    public Supplier() {
    }
    public Long getDbid() {
        return this.dbid;
    }
    public void setDbid(Long dbid) {
        this.dbid = dbid;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStore_name() {
        return this.store_name;
    }
    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
    public String getContact() {
        return this.contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return this.email;
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
        dest.writeValue(this.dbid);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.store_name);
        dest.writeString(this.contact);
        dest.writeString(this.mobile);
        dest.writeString(this.phone);
        dest.writeString(this.email);
    }

    protected Supplier(Parcel in) {
        this.dbid = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readString();
        this.name = in.readString();
        this.store_name = in.readString();
        this.contact = in.readString();
        this.mobile = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
    }

    public static final Creator<Supplier> CREATOR = new Creator<Supplier>() {
        @Override
        public Supplier createFromParcel(Parcel source) {
            return new Supplier(source);
        }

        @Override
        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };
}

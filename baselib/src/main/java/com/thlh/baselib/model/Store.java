package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Store {
    /*"id": 12,
        "store_name": "美家乐购便利店",
        "logo": "/BmUploadFile/store/ZDA/5Ym/Y0M/1473229943396.png",
        "province": "北京市",
        "city": "北京市",
        "district": "海淀区",
        "address": "学院路29号中国地质大学",
        "lat": "39.99709700",
        "long": "116.35641900",
        "rating": "0"*/

    private String id;
    private String name;
    private String store_name;
    private String address;
    private String contact;
    private String phone;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

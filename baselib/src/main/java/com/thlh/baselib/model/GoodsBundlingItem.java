package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/8.
 */
public class GoodsBundlingItem implements Parcelable {

    /**
     * item_id : 484
     * item_price : 9.00
     * item_num : 1
     * item_name : 黄油椰蓉原味饼干
     * item_weight : 0
     * item_img : /BmUploadFile/goodsimg2016/20160519/1463623329.jpg
     * item_img_thumb : /BmUploadFile/goodsthumb2016/20160519/1463623329.jpg
     * supplier : {"id":"3","store_name":"供货商3"}
     */
    private String bar_code;
    private String item_subtitle;
    private String is_shelves;
    private String is_g;
    private String is_mjb;
    private String weight;
    private String is_area;

    private String item_id;
    private String item_price;
    private String item_num;
    private String item_name;
    private String item_weight;
    private String item_img;
    private String init_price;
    private String item_img_thumb;

    private String supplier_id;

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getItem_subtitle() {
        return item_subtitle;
    }

    public void setItem_subtitle(String item_subtitle) {
        this.item_subtitle = item_subtitle;
    }

    public String getIs_shelves() {
        return is_shelves;
    }

    public void setIs_shelves(String is_shelves) {
        this.is_shelves = is_shelves;
    }

    public String getIs_g() {
        return is_g;
    }

    public void setIs_g(String is_g) {
        this.is_g = is_g;
    }

    public String getIs_mjb() {
        return is_mjb;
    }

    public void setIs_mjb(String is_mjb) {
        this.is_mjb = is_mjb;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getIs_area() {
        return is_area;
    }

    public void setIs_area(String is_area) {
        this.is_area = is_area;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_weight() {
        return item_weight;
    }

    public void setItem_weight(String item_weight) {
        this.item_weight = item_weight;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getInit_price() {
        return init_price;
    }

    public void setInit_price(String init_price) {
        this.init_price = init_price;
    }

    public String getItem_img_thumb() {
        return item_img_thumb;
    }

    public void setItem_img_thumb(String item_img_thumb) {
        this.item_img_thumb = item_img_thumb;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bar_code);
        dest.writeString(this.item_subtitle);
        dest.writeString(this.is_shelves);
        dest.writeString(this.is_g);
        dest.writeString(this.is_mjb);
        dest.writeString(this.weight);
        dest.writeString(this.is_area);
        dest.writeString(this.item_id);
        dest.writeString(this.item_price);
        dest.writeString(this.item_num);
        dest.writeString(this.item_name);
        dest.writeString(this.item_weight);
        dest.writeString(this.item_img);
        dest.writeString(this.init_price);
        dest.writeString(this.item_img_thumb);
        dest.writeString(this.supplier_id);
    }

    public GoodsBundlingItem() {
    }

    protected GoodsBundlingItem(Parcel in) {
        this.bar_code = in.readString();
        this.item_subtitle = in.readString();
        this.is_shelves = in.readString();
        this.is_g = in.readString();
        this.is_mjb = in.readString();
        this.weight = in.readString();
        this.is_area = in.readString();
        this.item_id = in.readString();
        this.item_price = in.readString();
        this.item_num = in.readString();
        this.item_name = in.readString();
        this.item_weight = in.readString();
        this.item_img = in.readString();
        this.init_price = in.readString();
        this.item_img_thumb = in.readString();
        this.supplier_id = in.readString();
    }

    public static final Creator<GoodsBundlingItem> CREATOR = new Creator<GoodsBundlingItem>() {
        @Override
        public GoodsBundlingItem createFromParcel(Parcel source) {
            return new GoodsBundlingItem(source);
        }

        @Override
        public GoodsBundlingItem[] newArray(int size) {
            return new GoodsBundlingItem[size];
        }
    };
}

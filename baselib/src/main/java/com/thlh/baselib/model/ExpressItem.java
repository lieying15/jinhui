package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/10/14.
 */

public class ExpressItem {

    /**
     * item_id : 34978
     * item_num : 1
     * item_weight : 0
     * item_price : 0.01
     * item_mjb : 44.00
     * use_mjb : 0
     * supplier_price : 44.00
     * storage : 91
     */

    private int item_id;
    private int item_num;
    private String item_weight;
    private String item_price;
    private String item_mjb;
    private int use_mjb;
    private String item_img_thumb;
    private String supplier_price;
    private int storage;

    public String getItem_img_thumb() {
        return item_img_thumb;
    }

    public void setItem_img_thumb(String item_img_thumb) {
        this.item_img_thumb = item_img_thumb;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getItem_num() {
        return item_num;
    }

    public void setItem_num(int item_num) {
        this.item_num = item_num;
    }

    public String getItem_weight() {
        return item_weight;
    }

    public void setItem_weight(String item_weight) {
        this.item_weight = item_weight;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_mjb() {
        return item_mjb;
    }

    public void setItem_mjb(String item_mjb) {
        this.item_mjb = item_mjb;
    }

    public int getUse_mjb() {
        return use_mjb;
    }

    public void setUse_mjb(int use_mjb) {
        this.use_mjb = use_mjb;
    }

    public String getSupplier_price() {
        return supplier_price;
    }

    public void setSupplier_price(String supplier_price) {
        this.supplier_price = supplier_price;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }
}

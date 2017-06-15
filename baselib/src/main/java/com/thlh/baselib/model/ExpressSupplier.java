package com.thlh.baselib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class ExpressSupplier {
    /**
     * supplier_id : 49
     * agency_id : 0
     * store_name : 瀚微国际
     * item : [{"item_id":34978,"item_num":1,"item_weight":"0","item_price":"0.01","item_mjb":"44.00","use_mjb":0,"supplier_price":"44.00","storage":91}]
     * mjb : 0
     * amount : 0.01
     * express_fee : 10
     */

    private String supplier_id;
    private int agency_id;
    private String store_name;
    private int mjb;
    private double amount;
    private double express_fee;
    private List<ExpressItem> item;

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getMjb() {
        return mjb;
    }

    public void setMjb(int mjb) {
        this.mjb = mjb;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getExpress_fee() {
        return express_fee;
    }

    public void setExpress_fee(double express_fee) {
        this.express_fee = express_fee;
    }

    public List<ExpressItem> getItem() {
        return item;
    }

    public void setItem(List<ExpressItem> item) {
        this.item = item;
    }



}

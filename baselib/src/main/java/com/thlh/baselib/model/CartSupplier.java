package com.thlh.baselib.model;


import java.util.List;

public class CartSupplier {

    private String supplier_name;
    private String supplier_id;
    private List<Cartgoods> cartgoods;

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public List<Cartgoods> getCartgoods() {
        return cartgoods;
    }

    public void setCartgoods(List<Cartgoods> cartgoods) {
        this.cartgoods = cartgoods;
    }
}

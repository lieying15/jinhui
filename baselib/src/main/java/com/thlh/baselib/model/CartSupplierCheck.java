package com.thlh.baselib.model;


import java.util.ArrayList;
import java.util.List;

public class CartSupplierCheck {
    private Boolean select = true;
    private List<Boolean> goodsCheckStates = new ArrayList<>();

    public List<Boolean> getGoodsCheckStates() {
        return goodsCheckStates;
    }

    public void setGoodsCheckStates(List<Boolean> goodsCheckStates) {
        this.goodsCheckStates = goodsCheckStates;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }
}

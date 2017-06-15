package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/7/25.
 */
public class DealRecord {

    /**
     * from : 1
     * type : R
     * amount : 1000.00
     * inputtime : 1468489948
     * recharge_from : 3
     * order_id : 0
     * remark : 美家钻充值
     */

    private String type;
    private String amount;
    private String inputtime;
    private String recharge_from;
    private String order_id;
    private String remark;
    private String recharge_id;
    private String total;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getRecharge_from() {
        return recharge_from;
    }

    public void setRecharge_from(String recharge_from) {
        this.recharge_from = recharge_from;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRecharge_id() {
        return recharge_id;
    }

    public void setRecharge_id(String recharge_id) {
        this.recharge_id = recharge_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

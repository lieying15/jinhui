package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/5/4.
 */
public class OrderPayResponse extends BaseResponse {

    /**
     * order_id : 6
     * third : 3
     * pay_no : ZF201607210437359536
     * amount : 100
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String order_id;
        private String third;
        private String pay_no;
        private double amount;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getThird() {
            return third;
        }

        public void setThird(String third) {
            this.third = third;
        }

        public String getPay_no() {
            return pay_no;
        }

        public void setPay_no(String pay_no) {
            this.pay_no = pay_no;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}

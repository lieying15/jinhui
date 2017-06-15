package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/4/25.
 */
public class RechargeMjbResponse extends BaseResponse {

    /**
     * pay_no : MR201608050211199533
     * by_third : 3
     * third_amount : 6800
     * is_coupon : 0
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String pay_no;
        private int by_third;
        private double third_amount;
        private int is_coupon;

        public String getPay_no() {
            return pay_no;
        }

        public void setPay_no(String pay_no) {
            this.pay_no = pay_no;
        }

        public int getBy_third() {
            return by_third;
        }

        public void setBy_third(int by_third) {
            this.by_third = by_third;
        }

        public double getThird_amount() {
            return third_amount;
        }

        public void setThird_amount(double third_amount) {
            this.third_amount = third_amount;
        }

        public int getIs_coupon() {
            return is_coupon;
        }

        public void setIs_coupon(int is_coupon) {
            this.is_coupon = is_coupon;
        }
    }
}
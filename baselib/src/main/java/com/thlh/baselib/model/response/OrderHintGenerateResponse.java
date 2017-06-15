package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/5/4.
 */
public class OrderHintGenerateResponse extends BaseResponse {


    /**
     * pay_by_mjb : 0
     * pay_by_balance : 10.1
     * pay_by_third : 0
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private double pay_by_mjb;
        private double pay_by_balance;
        private double pay_by_third;

        public double getPay_by_mjb() {
            return pay_by_mjb;
        }

        public void setPay_by_mjb(double pay_by_mjb) {
            this.pay_by_mjb = pay_by_mjb;
        }

        public double getPay_by_balance() {
            return pay_by_balance;
        }

        public void setPay_by_balance(double pay_by_balance) {
            this.pay_by_balance = pay_by_balance;
        }

        public double getPay_by_third() {
            return pay_by_third;
        }

        public void setPay_by_third(double pay_by_third) {
            this.pay_by_third = pay_by_third;
        }
    }
}

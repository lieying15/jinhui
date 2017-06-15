package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/4/25.
 */
public class WalletResponse extends BaseResponse {


    /**
     * balance : 0
     * mjb : 0
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private double balance;
        private double mjb;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getMjb() {
            return mjb;
        }

        public void setMjb(double mjb) {
            this.mjb = mjb;
        }
    }
}
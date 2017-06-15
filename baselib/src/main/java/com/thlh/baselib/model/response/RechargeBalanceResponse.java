package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/4/25.
 */
public class RechargeBalanceResponse extends BaseResponse {

    /**
     * amount : 1000
     * pay_no : BR23525325235235
     * pay_by : 3
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String amount;
        private String pay_no;
        private int pay_by;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPay_no() {
            return pay_no;
        }

        public void setPay_no(String pay_no) {
            this.pay_no = pay_no;
        }

        public int getPay_by() {
            return pay_by;
        }

        public void setPay_by(int pay_by) {
            this.pay_by = pay_by;
        }
    }
}
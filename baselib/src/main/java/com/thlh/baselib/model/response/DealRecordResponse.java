package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.DealRecord;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class DealRecordResponse extends BaseResponse {


    /**
     * total : 2
     * count : 10
     * total_page : 1
     * current_page : 1
     * wallet : [{"from":"1","type":"R","amount":"1000.00","inputtime":"1468489948","recharge_from":"3","order_id":"0","remark":"美家币充值"}]
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String total;
        private int count;
        private int total_page;
        private int current_page;
        /**
         * from : 1
         * type : R
         * amount : 1000.00
         * inputtime : 1468489948
         * recharge_from : 3
         * order_id : 0
         * remark : 美家币充值
         */

        private List<DealRecord> wallet;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public List<DealRecord> getWallet() {
            return wallet;
        }

        public void setWallet(List<DealRecord> wallet) {
            this.wallet = wallet;
        }


    }
}
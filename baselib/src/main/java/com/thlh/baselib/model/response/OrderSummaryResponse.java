package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/4/25.
 */
public class OrderSummaryResponse extends BaseResponse {

    /**
     * no_pay : 63
     * no_delivery : 134
     * no_get : 2
     * no_comment : 0
     * return_num : 0
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int no_pay;
        private int no_delivery;
        private int no_get;
        private int no_comment;
        private int return_num;

        public int getNo_pay() {
            return no_pay;
        }

        public void setNo_pay(int no_pay) {
            this.no_pay = no_pay;
        }

        public int getNo_delivery() {
            return no_delivery;
        }

        public void setNo_delivery(int no_delivery) {
            this.no_delivery = no_delivery;
        }

        public int getNo_get() {
            return no_get;
        }

        public void setNo_get(int no_get) {
            this.no_get = no_get;
        }

        public int getNo_comment() {
            return no_comment;
        }

        public void setNo_comment(int no_comment) {
            this.no_comment = no_comment;
        }

        public int getReturn_num() {
            return return_num;
        }

        public void setReturn_num(int return_num) {
            this.return_num = return_num;
        }
    }
}
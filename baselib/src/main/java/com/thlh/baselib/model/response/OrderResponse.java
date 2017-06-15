package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Order;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class OrderResponse extends BaseResponse {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int total;
        private int count;
        private int total_page;
        private int current_page;
        private List<Order> orders;

        private int processing_num;
        private int done_num;
        private int uncomm_num;
        private int comm_num;
        private int unpay_num;
        private int undeliv_num;
        private int unget_num;
        private int cancel_num;


        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
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

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }


        public int getProcessing_num() {
            return processing_num;
        }

        public void setProcessing_num(int processing_num) {
            this.processing_num = processing_num;
        }

        public int getDone_num() {
            return done_num;
        }

        public void setDone_num(int done_num) {
            this.done_num = done_num;
        }

        public int getUncomm_num() {
            return uncomm_num;
        }

        public void setUncomm_num(int uncomm_num) {
            this.uncomm_num = uncomm_num;
        }

        public int getComm_num() {
            return comm_num;
        }

        public void setComm_num(int comm_num) {
            this.comm_num = comm_num;
        }

        public int getUnpay_num() {
            return unpay_num;
        }

        public void setUnpay_num(int unpay_num) {
            this.unpay_num = unpay_num;
        }

        public int getUndeliv_num() {
            return undeliv_num;
        }

        public void setUndeliv_num(int undeliv_num) {
            this.undeliv_num = undeliv_num;
        }

        public int getUnget_num() {
            return unget_num;
        }

        public void setUnget_num(int unget_num) {
            this.unget_num = unget_num;
        }

        public int getCancel_num() {
            return cancel_num;
        }

        public void setCancel_num(int cancel_num) {
            this.cancel_num = cancel_num;
        }
    }
}

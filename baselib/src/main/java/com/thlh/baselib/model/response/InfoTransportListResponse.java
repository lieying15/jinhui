package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.LogsOrder;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class InfoTransportListResponse extends BaseResponse {

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
         * id : 42
         * order_id : 1039
         * member_id : 28
         * store_id : 0
         * supplier_id : 1
         * order_no : DD201608081925164357
         * action_time : 1470655516
         * action : 1
         * remark : 下单成功
         */

        private List<LogsOrder> logs;

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

        public List<LogsOrder> getLogs() {
            return logs;
        }

        public void setLogs(List<LogsOrder> logs) {
            this.logs = logs;
        }


    }
}

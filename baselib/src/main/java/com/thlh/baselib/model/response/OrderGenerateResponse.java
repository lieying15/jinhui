package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/5/4.
 */
public class OrderGenerateResponse extends BaseResponse {

    /**
     * order_id : 6
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

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }
    }
}

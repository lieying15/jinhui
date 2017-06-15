package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * 增加收货地址的返回BEAN
 */
public class AddrAddResponse extends BaseResponse {

    /**
     * address_id : 238
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String address_id;

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }
    }
}

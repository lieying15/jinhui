package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * 查询用户充值渠道接口
 */
public class AgencyResponse extends BaseResponse {

    /**
     * data : {"recharge_id":0,"agency_id":"13","qr_code":"/images/channel/VGx/Jam/9pO/1477882064994.png","is_ch":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * recharge_id : 0
         * agency_id : 13
         * qr_code : /images/channel/VGx/Jam/9pO/1477882064994.png
         * is_ch : 0
         */

        private int recharge_id;
        private String agency_id;
        private String order_id;
        private String qr_code;
        private int is_ch;

        public int getRecharge_id() {
            return recharge_id;
        }

        public void setRecharge_id(int recharge_id) {
            this.recharge_id = recharge_id;
        }

        public String getAgency_id() {
            return agency_id;
        }

        public void setAgency_id(String agency_id) {
            this.agency_id = agency_id;
        }

        public String getQr_code() {
            return qr_code;
        }

        public void setQr_code(String qr_code) {
            this.qr_code = qr_code;
        }

        public int getIs_ch() {
            return is_ch;
        }

        public void setIs_ch(int is_ch) {
            this.is_ch = is_ch;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }
    }
}

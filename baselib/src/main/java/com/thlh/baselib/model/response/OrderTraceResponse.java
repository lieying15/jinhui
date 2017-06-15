package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.OrderPack;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class OrderTraceResponse extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String order_time;
        private String pay_time;
        private String express_fee;

        private List<GoodsOrder> item;

        private List<OrderPack> pack;

        public String getExpress_fee() {
            return express_fee;
        }

        public void setExpress_fee(String express_fee) {
            this.express_fee = express_fee;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public List<OrderPack> getPack() {
            return pack;
        }

        public void setPack(List<OrderPack> pack) {
            this.pack = pack;
        }

        public List<GoodsOrder> getItem() {
            return item;
        }

        public void setItem(List<GoodsOrder> item) {
            this.item = item;
        }

    }


}

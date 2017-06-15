package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.ExpressSupplier;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ExpressfreeResponse extends BaseResponse {
    /**
     * data : {"total":10,"supplier":[{"supplier_id":"49","agency_id":0,"store_name":"瀚微国际","item":[{"item_id":34978,"item_num":1,"item_weight":"0","item_price":"0.01","item_mjb":"44.00","use_mjb":0,"supplier_price":"44.00","storage":91}],"mjb":0,"amount":0.01,"express_fee":10}]}
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
         * total : 10
         * supplier : [{"supplier_id":"49","agency_id":0,"store_name":"瀚微国际","item":[{"item_id":34978,"item_num":1,"item_weight":"0","item_price":"0.01","item_mjb":"44.00","use_mjb":0,"supplier_price":"44.00","storage":91}],"mjb":0,"amount":0.01,"express_fee":10}]
         */

        private double total;
        private List<ExpressSupplier> supplier;

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public List<ExpressSupplier> getSupplier() {
            return supplier;
        }

        public void setSupplier(List<ExpressSupplier> supplier) {
            this.supplier = supplier;
        }



    }


}

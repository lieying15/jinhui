package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.FilterSupplier;

import java.util.List;

/**
 * 供应商
 */
public class SupplierResponse extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * name : 惠家有
         * store_name : 供货商3
         */

        private List<FilterSupplier> brand;

        public List<FilterSupplier> getSupplier() {
            return brand;
        }

        public void setSupplier(List<FilterSupplier> filterSupplier) {
            this.brand = filterSupplier;
        }

    }
}

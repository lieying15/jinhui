package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Brand;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class FilterBrandResponse extends BaseResponse {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * brandid : 6
         * brand : 尚品家纺
         */

        private List<Brand> brand;

        public List<Brand> getBrand() {
            return brand;
        }

        public void setBrand(List<Brand> brand) {
            this.brand = brand;
        }

    }
}

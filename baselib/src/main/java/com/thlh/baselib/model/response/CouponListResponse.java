package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Coupon;

import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
public class CouponListResponse extends BaseResponse {


    /**
     * total : 2
     * count : 10
     * total_page : 1
     * current_page : 1
     * coupons : [{"id":"1","coupon_name":"冰箱优惠券","logo":"/images/coupon/a.jpg","coupon_end_time":"1469426340"}]
     */

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

        /**
         * id : 1
         * coupon_name : 冰箱优惠券
         * logo : /images/coupon/a.jpg
         * coupon_end_time : 1469426340
         */

        private List<Coupon> coupons;

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

        public List<Coupon> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<Coupon> coupons) {
            this.coupons = coupons;
        }

    }
}

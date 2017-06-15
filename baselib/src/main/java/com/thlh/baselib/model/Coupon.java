package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/7/26.
 */
public class Coupon {


    /**
     * id : 1
     * coupon_name : 冰箱优惠券
     * logo : /images/coupon/a.jpg
     * coupon_end_time : 1469426340
     */

    private String id;
    private String coupon_name;
    private String logo;
    private String coupon_end_time;
    private String is_used;

    public String getIs_used() {
        return is_used;
    }

    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getLogo() {

        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCoupon_end_time() {
        return coupon_end_time;
    }

    public void setCoupon_end_time(String coupon_end_time) {
        this.coupon_end_time = coupon_end_time;
    }
}

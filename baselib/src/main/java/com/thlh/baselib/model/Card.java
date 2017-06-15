package com.thlh.baselib.model;

/**
 * Created by LD on 2017/4/24.
 */

public class Card {

    /**
     * id : 846
     * code : 1009170661002493
     * type_id : 2
     * start_date : 1496246400
     * expired_date : 1527696000
     * mjz_card : 1
     * face_value : 100.00
     * coupon_id : 0
     */

    private String id;
    private String code;
    private String type_id;
    private String start_date;
    private String expired_date;
    private String mjz_card;
    private String face_value;
    private String coupon_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    public String getMjz_card() {
        return mjz_card;
    }

    public void setMjz_card(String mjz_card) {
        this.mjz_card = mjz_card;
    }

    public String getFace_value() {
        return face_value;
    }

    public void setFace_value(String face_value) {
        this.face_value = face_value;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

}

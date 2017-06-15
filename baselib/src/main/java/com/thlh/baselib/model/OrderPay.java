package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/18.
 */
public class OrderPay implements Parcelable {

    /**
     * id : 174
     * pay_no : ZF201608241834415719
     * sum : 59.00
     * payment_id : 1
     * payment_method_id : 2
     * payment_time : 1472034881
     * pay_content :
     * transaction_id :
     * is_ok : 1
     * fail_reason :
     * out_trade_no :
     */

    private String id;
    private String pay_no;
    private String sum;
    private String payment_id;
    private String payment_method_id;
    private String payment_time;
    private String pay_content;
    private String transaction_id;
    private String is_ok;
    private String fail_reason;
    private String out_trade_no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPay_no() {
        return pay_no;
    }

    public void setPay_no(String pay_no) {
        this.pay_no = pay_no;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getPay_content() {
        return pay_content;
    }

    public void setPay_content(String pay_content) {
        this.pay_content = pay_content;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getIs_ok() {
        return is_ok;
    }

    public void setIs_ok(String is_ok) {
        this.is_ok = is_ok;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.pay_no);
        dest.writeString(this.sum);
        dest.writeString(this.payment_id);
        dest.writeString(this.payment_method_id);
        dest.writeString(this.payment_time);
        dest.writeString(this.pay_content);
        dest.writeString(this.transaction_id);
        dest.writeString(this.is_ok);
        dest.writeString(this.fail_reason);
        dest.writeString(this.out_trade_no);
    }

    public OrderPay() {
    }

    protected OrderPay(Parcel in) {
        this.id = in.readString();
        this.pay_no = in.readString();
        this.sum = in.readString();
        this.payment_id = in.readString();
        this.payment_method_id = in.readString();
        this.payment_time = in.readString();
        this.pay_content = in.readString();
        this.transaction_id = in.readString();
        this.is_ok = in.readString();
        this.fail_reason = in.readString();
        this.out_trade_no = in.readString();
    }

    public static final Parcelable.Creator<OrderPay> CREATOR = new Parcelable.Creator<OrderPay>() {
        @Override
        public OrderPay createFromParcel(Parcel source) {
            return new OrderPay(source);
        }

        @Override
        public OrderPay[] newArray(int size) {
            return new OrderPay[size];
        }
    };
}

package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class Order implements Parcelable {


    private String order_id;
    private String order_no;
    private String name;
    private String address;
    private String address_id;
    private String zipcode;
    private String telephone;
    private String order_time;
    private String goods_amount;
    private String cheap_fee;
    private String express_fee;
    private String is_pay;
    private String is_delivery;
    private String is_get;
    private String is_over;
    private String is_comment;
    private String remarks;
    private String store_id;
    private String supplier_id;
    private String pid;
    private String is_div;
    private String to_store;
    private String get_pack;
    private String service_time;
    private String  pay_by_mjb;
    /*
    * */
    private String note;

    public String getNote(){
        return note;
    }
    public void setNote(String note){
        this.note=note;
    }
    private double should_pay;
    private List<OrderItem> order_items;
    private List<OrderPay> pay;

    public List<OrderPay> getPay() {
        return pay;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public String getCheap_fee() {
        return cheap_fee;
    }

    public void setCheap_fee(String cheap_fee) {
        this.cheap_fee = cheap_fee;
    }

    public String getExpress_fee() {
        return express_fee;
    }

    public void setExpress_fee(String express_fee) {
        this.express_fee = express_fee;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(String is_delivery) {
        this.is_delivery = is_delivery;
    }

    public String getIs_get() {
        return is_get;
    }

    public void setIs_get(String is_get) {
        this.is_get = is_get;
    }

    public String getIs_over() {
        return is_over;
    }

    public void setIs_over(String is_over) {
        this.is_over = is_over;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIs_div() {
        return is_div;
    }

    public void setIs_div(String is_div) {
        this.is_div = is_div;
    }

    public String getTo_store() {
        return to_store;
    }

    public void setTo_store(String to_store) {
        this.to_store = to_store;
    }

    public String getGet_pack() {
        return get_pack;
    }

    public void setGet_pack(String get_pack) {
        this.get_pack = get_pack;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public String getPay_by_mjb() {
        return pay_by_mjb;
    }

    public void setPay_by_mjb(String pay_by_mjb) {
        this.pay_by_mjb = pay_by_mjb;
    }

    public double getShould_pay() {
        return should_pay;
    }

    public void setShould_pay(double should_pay) {
        this.should_pay = should_pay;
    }

    public List<OrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<OrderItem> order_items) {
        this.order_items = order_items;
    }

    public void setPay(List<OrderPay> pay) {
        this.pay = pay;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.order_no);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.address_id);
        dest.writeString(this.zipcode);
        dest.writeString(this.telephone);
        dest.writeString(this.order_time);
        dest.writeString(this.goods_amount);
        dest.writeString(this.cheap_fee);
        dest.writeString(this.express_fee);
        dest.writeString(this.is_pay);
        dest.writeString(this.is_delivery);
        dest.writeString(this.is_get);
        dest.writeString(this.is_over);
        dest.writeString(this.is_comment);
        dest.writeString(this.remarks);
        dest.writeString(this.store_id);
        dest.writeString(this.supplier_id);
        dest.writeString(this.pid);
        dest.writeString(this.is_div);
        dest.writeString(this.to_store);
        dest.writeString(this.get_pack);
        dest.writeString(this.service_time);
        dest.writeString(this.pay_by_mjb);
        dest.writeDouble(this.should_pay);
        dest.writeTypedList(this.order_items);
        dest.writeTypedList(this.pay);
        dest.writeString(this.note);
    }

    public Order() {
    }

    protected Order(Parcel in) {
        this.order_id = in.readString();
        this.order_no = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.address_id = in.readString();
        this.zipcode = in.readString();
        this.telephone = in.readString();
        this.order_time = in.readString();
        this.goods_amount = in.readString();
        this.cheap_fee = in.readString();
        this.express_fee = in.readString();
        this.is_pay = in.readString();
        this.is_delivery = in.readString();
        this.is_get = in.readString();
        this.is_over = in.readString();
        this.is_comment = in.readString();
        this.remarks = in.readString();
        this.store_id = in.readString();
        this.supplier_id = in.readString();
        this.pid = in.readString();
        this.is_div = in.readString();
        this.to_store = in.readString();
        this.get_pack = in.readString();
        this.service_time = in.readString();
        this.pay_by_mjb = in.readString();
        this.should_pay = in.readDouble();
        this.order_items = in.createTypedArrayList(OrderItem.CREATOR);
        this.pay = in.createTypedArrayList(OrderPay.CREATOR);
        this.note=in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}

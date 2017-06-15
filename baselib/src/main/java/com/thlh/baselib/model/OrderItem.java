package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class OrderItem implements Parcelable {

    /**
     * store_id : 0
     * store_name : 供货商1
     * supplier_id : 1
     * express_fee : 0.00
     * item : [{"item_id":"570","item_name":"斯勤星期面膜","bar_code":"","item_price":"59.00","member_price":"59.00","market_price":"59.00","is_promotion":"1","promotion_price":"0.00","start_date":"","end_date":"","item_img":"/BmUploadFile/goodsimg2016/20160630/1467259358.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160630/1467259358.jpg","is_shelves":"1","weight":"0","is_bundling":"0","bundling_id":"0","supplier_id":"1","item_subtitle":"百颜斯勤","is_g":"0","is_area":"0","is_div_delivery":"0","is_mjb":"0","supplier_name":"供货商1","storage":"0","supplier_price":"59.00","special":"","bundling_price":"0","item_no":"","item_num":"1"},{"item_id":"519","item_name":"百颜-斯勤致润修护玻尿酸原液","bar_code":"","item_price":"399.00","member_price":"399.00","market_price":"399.00","is_promotion":"1","promotion_price":"0.00","start_date":"","end_date":"","item_img":"/BmUploadFile/goodsimg2016/20160709/1468010870.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160709/1468010870.jpg","is_shelves":"1","weight":"0","is_bundling":"0","bundling_id":"1","supplier_id":"1","item_subtitle":"百颜斯勤","is_g":"0","is_area":"0","is_div_delivery":"0","is_mjb":"0","supplier_name":"供货商1","storage":"951","supplier_price":"0.00","special":"","bundling_price":"0","item_no":"","item_num":"1"}]
     */

    private int store_id;
    private String store_name;
    private String supplier_id;
    private String express_fee;

    private List<GoodsOrder> item;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getExpress_fee() {
        return express_fee;
    }

    public void setExpress_fee(String express_fee) {
        this.express_fee = express_fee;
    }

    public List<GoodsOrder> getItem() {
        return item;
    }

    public void setItem(List<GoodsOrder> item) {
        this.item = item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.store_id);
        dest.writeString(this.store_name);
        dest.writeString(this.supplier_id);
        dest.writeString(this.express_fee);
        dest.writeTypedList(this.item);
    }

    public OrderItem() {
    }

    protected OrderItem(Parcel in) {
        this.store_id = in.readInt();
        this.store_name = in.readString();
        this.supplier_id = in.readString();
        this.express_fee = in.readString();
        this.item = in.createTypedArrayList(GoodsOrder.CREATOR);
    }

    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel source) {
            return new OrderItem(source);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}

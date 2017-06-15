package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Goods;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class SimilarResponse extends BaseResponse {


    /**
     * count : 3
     * items : [{"item_id":"156","item_name":"佰格仕玛加纳干红葡萄酒","item_sub_title":"","item_no":"14603678467613","bar_code":"8437006297097","item_price":"158.00","member_price":"158.00","market_price":"158.00","promotion_price":"0.00","item_img":"/BmUploadFile/goodsimg2016/20160414/1460619415主图1.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460622487主图1-300.jpg","promotion_tag":"0","is_shelves":"1"},"..."]
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int count;
        /**
         * item_id : 156
         * item_name : 佰格仕玛加纳干红葡萄酒
         * item_sub_title :
         * item_no : 14603678467613
         * bar_code : 8437006297097
         * item_price : 158.00
         * member_price : 158.00
         * market_price : 158.00
         * promotion_price : 0.00
         * item_img : /BmUploadFile/goodsimg2016/20160414/1460619415主图1.jpg
         * item_img_thumb : /BmUploadFile/goodsthumb2016/20160414/1460622487主图1-300.jpg
         * promotion_tag : 0
         * is_shelves : 1
         */

        private List<Goods> items;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Goods> getItems() {
            return items;
        }

        public void setItems(List<Goods> items) {
            this.items = items;
        }

    }
}

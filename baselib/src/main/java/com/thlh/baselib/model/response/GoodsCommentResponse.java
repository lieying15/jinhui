package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.GoodsComment;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class GoodsCommentResponse extends BaseResponse {


    /**
     * total : 1
     * count : 20
     * total_page : 1
     * current_page : 1
     * items : [{"order_id":"224","item_id":"512","item_num":"1","price":"399.00","item_name":"斯勤水光针玻尿酸原液","item_img":"/BmUploadFile/goodsimg2016/20160520/1463727614.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160520/1463727614.jpg","comment":{"rate":"4","comment":"物品不错~~","inputtime":"1467821450","answer":"","pic":["/images/comment/Y2U/2Ym/I0N/1467821450675."]}}]
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int total;
        private int count;
        private int total_page;
        private int current_page;
        /**
         * order_id : 224
         * item_id : 512
         * item_num : 1
         * price : 399.00
         * item_name : 斯勤水光针玻尿酸原液
         * item_img : /BmUploadFile/goodsimg2016/20160520/1463727614.jpg
         * item_img_thumb : /BmUploadFile/goodsthumb2016/20160520/1463727614.jpg
         * comment : {"rate":"4","comment":"物品不错~~","inputtime":"1467821450","answer":"","pic":["/images/comment/Y2U/2Ym/I0N/1467821450675."]}
         */

        private List<GoodsComment> items;

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

        public List<GoodsComment> getItems() {
            return items;
        }

        public void setItems(List<GoodsComment> items) {
            this.items = items;
        }

    }
}

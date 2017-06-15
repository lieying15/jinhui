package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Goods;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SearchResponse extends BaseResponse {

    /**
     * total : 62
     * count : 10
     * total_page : 7
     * current_page : 1
     * item : [{"item_id":"134","item_name":"尚品白桦林抱枕被","item_img":"/BmUploadFile/goodsimg2016/20160414/1460621519白桦林-800.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460621519白桦林-300.jpg","item_price":"196.00","member_price":"196.00","market_price":"196.00"},{"item_id":"136","item_name":"尚品风华绝代丝巾","item_img":"/BmUploadFile/goodsimg2016/20160414/1460624107尚品风华绝代丝巾-800.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460624107尚品风华绝代丝巾-300.jpg","item_price":"659.00","member_price":"659.00","market_price":"659.00"},{"item_id":"177","item_name":"密闭式坚果夹","item_img":"/BmUploadFile/goodsimg2016/20160415/14606885571.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160415/14606885571.jpg","item_price":"62.00","member_price":"62.00","market_price":"62.00"},{"item_id":"147","item_name":"尚品孝心冰丝席三件套","item_img":"/BmUploadFile/goodsimg2016/20160414/1460623043尚品孝心冰丝席三件套-800.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460623043尚品孝心冰丝席三件套-300.jpg","item_price":"438.00","member_price":"438.00","market_price":"438.00"},{"item_id":"188","item_name":"【方拳】多功能家用工具","item_img":"/BmUploadFile/goodsimg2016/20160414/14606289481.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/14606289481.jpg","item_price":"272.00","member_price":"272.00","market_price":"272.00"},{"item_id":"158","item_name":"火葡园解百纳高级干红葡萄酒","item_img":"/BmUploadFile/goodsimg2016/20160414/1460622405火葡园赤霞珠金标-800.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460622405火葡园赤霞珠金标-300.jpg","item_price":"298.00","member_price":"298.00","market_price":"298.00"},{"item_id":"167","item_name":"S-616A光波炉（12升）","item_img":"/BmUploadFile/goodsimg2016/20160414/1460628088616A-800.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460628088616A-300.jpg","item_price":"298.00","member_price":"298.00","market_price":"298.00"},{"item_id":"137","item_name":"尚品古都新尚夏被","item_img":"/BmUploadFile/goodsimg2016/20160414/1460623773尚品古都新尚夏被-800.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460623773尚品古都新尚夏被-300.jpg","item_price":"306.00","member_price":"306.00","market_price":"306.00"},{"item_id":"178","item_name":"花型水果叉","item_img":"/BmUploadFile/goodsimg2016/20160415/14606892221.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160415/14606892221.jpg","item_price":"49.00","member_price":"49.00","market_price":"49.00"},{"item_id":"189","item_name":"【分身】多功能家用工具","item_img":"/BmUploadFile/goodsimg2016/20160415/14606865531.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160415/14606865531.jpg","item_price":"242.00","member_price":"242.00","market_price":"242.00"}]
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
         * item_id : 134
         * item_name : 尚品白桦林抱枕被
         * item_img : /BmUploadFile/goodsimg2016/20160414/1460621519白桦林-800.jpg
         * item_img_thumb : /BmUploadFile/goodsthumb2016/20160414/1460621519白桦林-300.jpg
         * item_price : 196.00
         * member_price : 196.00
         * market_price : 196.00
         */

        private List<Goods> item;

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

        public List<Goods> getItem() {
            return item;
        }

        public void setItem(List<Goods> item) {
            this.item = item;
        }

    }
}

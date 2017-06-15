package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Goods;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SearchIndexResponse extends BaseResponse {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> hot_search;
        /**
         * item_id : 135
         * c : 5
         * item_name : 尚品斑马世界毛巾三件套
         * item_subtitle :
         * item_no : SP-1130062
         * bar_code :
         * promotion_tag : 0
         * item_price : 486.00
         * member_price : 486.00
         * market_price : 486.00
         * promotion_price : 0.00
         * item_img : /BmUploadFile/goodsimg2016/20160414/1460622757尚品斑马世界毛巾三件套-800.jpg
         * item_img_thumb : /BmUploadFile/goodsthumb2016/20160414/1460622757尚品斑马世界毛巾三件套-300.jpg
         * is_shop : 1
         */

        private List<Goods> hot;

        public List<String> getHot_search() {
            return hot_search;
        }

        public void setHot_search(List<String> hot_search) {
            this.hot_search = hot_search;
        }

        public List<Goods> getHot() {
            return hot;
        }

        public void setHot(List<Goods> hot) {
            this.hot = hot;
        }

    }

//    private SeachIndex data;
//
//    public SeachIndex getData() {
//        return data;
//    }
//
//    public void setData(SeachIndex data) {
//        this.data = data;
//    }


}

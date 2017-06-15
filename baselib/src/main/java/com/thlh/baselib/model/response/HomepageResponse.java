package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.CategoryInHp;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.HomepageTitleAD;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class HomepageResponse extends BaseResponse {


    /**
     * err_code : 200
     * err_msg :
     * data : {"title":[{"item_id":"30","pic":"/BmUploadFile/lungoodsimg2016/20160411/1460381172a_31.jpg"},{"item_id":"29","pic":"/BmUploadFile/lungoodsimg2016/20160411/1460381162a_2.jpg"},{"item_id":"28","pic":"/BmUploadFile/lungoodsimg2016/20160411/1460381148a_31.jpg"},{"item_id":"27","pic":"/BmUploadFile/lungoodsimg2016/20160411/1460379053a_2.jpg"}],"today":[],"star":[{"item_id":"162","item_name":"巴巴激情高级干红葡萄酒","item_price":"巴巴激情高级干红葡萄酒","item_pic":"/BmUploadFile/goodsimg2016/20160414/1460622229巴巴激情干红葡萄酒-800.jpg","item_pic_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460622229巴巴激情干红葡萄酒-300.jpg","promotion_tag":""},{"item_id":"163","item_name":"罗茜奥半甜白葡萄酒","item_price":"罗茜奥半甜白葡萄酒","item_pic":"/BmUploadFile/goodsimg2016/20160414/1460622285罗茜奥半甜干白葡萄酒-800.jpg","item_pic_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460622285罗茜奥半甜干白葡萄酒-300.jpg","promotion_tag":""},{"item_id":"164","item_name":"托美亚传统干红葡萄酒","item_price":"托美亚传统干红葡萄酒","item_pic":"/BmUploadFile/goodsimg2016/20160414/1460622695托美亚传统干红-800.jpg","item_pic_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460622695托美亚传统干红-300.jpg","promotion_tag":""}]}
     */

    private HomepageData data;



    public HomepageData getData() {
        return data;
    }

    public void setData(HomepageData data) {
        this.data = data;
    }

    public static class HomepageData {
        /**
         * item_id : 30
         * pic : /BmUploadFile/lungoodsimg2016/20160411/1460381172a_31.jpg
         */

        private List<HomepageTitleAD> title;
        private List<Goods> today;
        private List<Goods> star;
        private List<CategoryInHp> category;



        /**
         * item_id : 162
         * item_name : 巴巴激情高级干红葡萄酒
         * item_price : 巴巴激情高级干红葡萄酒
         * item_pic : /BmUploadFile/goodsimg2016/20160414/1460622229巴巴激情干红葡萄酒-800.jpg
         * item_pic_thumb : /BmUploadFile/goodsthumb2016/20160414/1460622229巴巴激情干红葡萄酒-300.jpg
         * promotion_tag :
         */



        public List<HomepageTitleAD> getTitle() {
            return title;
        }

        public void setTitle(List<HomepageTitleAD> title) {
            this.title = title;
        }

        public List<Goods> getToday() {
            return today;
        }

        public void setToday(List<Goods> today) {
            this.today = today;
        }

        public List<Goods> getStar() {
            return star;
        }

        public void setStar(List<Goods> star) {
            this.star = star;
        }

        public List<CategoryInHp> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryInHp> category) {
            this.category = category;
        }
    }
}

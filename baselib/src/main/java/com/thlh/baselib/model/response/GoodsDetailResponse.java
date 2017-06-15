package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.GoodsDetail;

/**
 * Created by Administrator on 2016/4/25.
 */
public class GoodsDetailResponse extends BaseResponse {


    /**
     * item : {"item_id":"135","item_name":"尚品白桦林抱枕被","item_no":"SP-0540068","bar_code":"","item_price":"196.00","member_price":"196.00","market_price":"196.00","promotion_price":"0.00","item_img":"/BmUploadFile/goodsimg2016/20160414/1460621519白桦林-800.jpg","item_img_thumb":"/BmUploadFile/goodsthumb2016/20160414/1460621519白桦林-300.jpg","content":"<p>尺寸：40*40cm，打开110*150cm，面料： 涤棉<img src=\"/BmUploadFile/ueditor/image/20160412/570c5b2c4e6c8.jpg\" style=\"\" title=\"570c5b2c4e6c8.jpg\"/><\/p><p><img src=\"/BmUploadFile/ueditor/image/20160412/570c5b301cb2c.jpg\" style=\"\" title=\"570c5b301cb2c.jpg\"/><\/p><p><img src=\"/BmUploadFile/ueditor/image/20160412/570c5b3734fd0.jpg\" style=\"\" title=\"570c5b3734fd0.jpg\"/><\/p><p><img src=\"/BmUploadFile/ueditor/image/20160412/570c5b3891c55.jpg\" style=\"\" title=\"570c5b3891c55.jpg\"/><\/p><p><br/><\/p>","item_pic1":"/BmUploadFile/daoimg/topimg/a_t1.jpg","item_pic2":"/BmUploadFile/daoimg/topimg/a_t2.jpg","item_pic3":"/BmUploadFile/daoimg/topimg/a_t3.jpg","item_pic4":"/BmUploadFile/daoimg/topimg/a_t4.jpg","brand_id":"6","brand_name":"尚品家纺","spec":[{"name":"name1","value":"val1"},{"name":"name2","value":"val2"},{"name":"name3","value":"val3"}]}
     */

    private GoodsDetailData data;

    public GoodsDetailData getData() {
        return data;
    }

    public void setData(GoodsDetailData data) {
        this.data = data;
    }

    public static class GoodsDetailData {

        private GoodsDetail item;

        public GoodsDetail getItem() {
            return item;
        }

        public void setItem(GoodsDetail item) {
            this.item = item;
        }


    }
}

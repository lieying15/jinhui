package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Order;

/**
 * Created by LD on 2017/8/10.
 */

public class OrderDetailsResponse extends BaseResponse{


    /**
     * data : {"order_id":"423","order_no":"OD201708101406057374","address_id":"5","name":"测试","province":"北京","city":"北京","district":"海淀区","address":"北京北京海淀区黑泉路","zipcode":"000000","telephone":"17710054796","order_time":"1502345165","goods_amount":"32.58","cheap_fee":"0.00","express_fee":"10.00","pay_by_mjb":"14.64","pay_by_balance":"0.00","is_pay":"0","is_delivery":"0","is_get":"0","is_over":"0","remarks":"","store_id":"0","supplier_id":"151","pid":"0","is_div":"0","is_comment":"0","to_store":"0","get_pack":"1","service_time":"0","note":"","is_fridge":"0","order_items":[{"store_id":0,"store_name":"","supplier_id":"151","express_fee":"10.00","item":[{"is_area":"0","is_part":"0","bundling_id":"0","is_bundling":"0","is_limit":"0","promotion_price":"0.00","item_name":"炫迈跃动鲜果味无糖口香糖6片","supplier_sid":"0","cash_for_rest":"1","is_show":"1","part_of_id":"0","supplier_id":"151","item_price":"2.78","item_subtitle":"炫迈口香糖","market_price":"0.00","bar_code":"6954432710171","is_sell":"1","weight":"0","agency_id":"0","is_promotion":"","mjb_value":"0.28","name_of_supplier":"炫迈跃动鲜果味无糖口香糖6片","from_store_id":"0","g_status":"0","is_div_delivery":"0","item_img_thumb":"/BmUploadFile/goodsthumb2017/20170426/1493190513.jpg","is_g":"0","member_price":"0.00","pack_num":"0","is_mjb":"2","is_pack":"0","item_img":"/BmUploadFile/goodsimg2017/20170426/1493190513.jpg","is_shelves":"1","is_prepare":"0","in_cart":"1","is_express":"0","item_id":"37353","supplier":{"name":"天汇联合（北京）科技发展有限公司","store_name":"","company":"天汇联合（北京）科技发展有限公司","lat":"","contact":"123","mobile":"13520930000","phone":"","email":"","agency_id":"0","id":"151"},"supplier_name":"天汇联合（北京）科技发展有限公司","supplier_tel":"13520930000","storage":10,"catid":["156"],"limit_icon":"0","limit_num":"0","area":[],"item_num":"1"},{"is_area":"0","is_part":"0","bundling_id":"0","is_bundling":"0","is_limit":"0","promotion_price":"0.00","item_name":"力士(LUX)洗发乳 新活炫亮400ml","supplier_sid":"0","cash_for_rest":"0","is_show":"1","part_of_id":"0","supplier_id":"151","item_price":"29.80","item_subtitle":"新活炫亮","market_price":"0.00","bar_code":"","is_sell":"1","weight":"0","agency_id":"0","is_promotion":"","mjb_value":"14.36","name_of_supplier":"力士(LUX)洗发乳 新活炫亮400ml","from_store_id":"0","g_status":"0","is_div_delivery":"0","item_img_thumb":"/BmUploadFile/goodsthumb2017/20170705/1499244145.jpg","is_g":"0","member_price":"0.00","pack_num":"0","is_mjb":"2","is_pack":"0","item_img":"/BmUploadFile/goodsimg2017/20170705/1499244145.jpg","is_shelves":"1","is_prepare":"0","in_cart":"1","is_express":"0","item_id":"36731","supplier":{"name":"天汇联合（北京）科技发展有限公司","store_name":"","company":"天汇联合（北京）科技发展有限公司","lat":"","contact":"123","mobile":"13520930000","phone":"","email":"","agency_id":"0","id":"151"},"supplier_name":"天汇联合（北京）科技发展有限公司","supplier_tel":"13520930000","storage":20,"catid":["155"],"limit_icon":"0","limit_num":"0","area":[],"item_num":"1"}]}],"should_pay":42.58,"current":1502347865}
     */

    private Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }

}

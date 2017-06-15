package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Card;

/**
 * 兑换卡网络数据
 *
 * Created by LD on 2017/4/24.
 */
public class ExchangeCardResponse  extends BaseResponse {


    /**
     * data : {"card":{"id":"846","code":"1009170661002493","type_id":"2","start_date":"1496246400","expired_date":"1527696000","mjz_card":"1","face_value":"100.00","coupon_id":"0"}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * card : {"id":"846","code":"1009170661002493","type_id":"2","start_date":"1496246400","expired_date":"1527696000","mjz_card":"1","face_value":"100.00","coupon_id":"0"}
         */

        private Card card;

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

    }
}

package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.ChoiceGoods;

import java.util.List;

/**
 * 商城精选
 */
public class ChoiceResponse extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private List<ChoiceGoods> choice;

        public List<ChoiceGoods> getChoiceGoods() {
            return choice;
        }

        public void setChoiceGoods(List<ChoiceGoods> choiceGoods) {
            this.choice = choiceGoods;
        }

    }


}

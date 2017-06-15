package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Goods;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class GoodsRecommendResponse extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;

        private List<Goods> items;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Goods> getItem() {
            return items;
        }

        public void setItem(List<Goods> items) {
            this.items = items;
        }

    }
}

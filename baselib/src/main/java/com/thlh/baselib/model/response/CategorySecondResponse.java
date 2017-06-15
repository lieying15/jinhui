package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Category;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class CategorySecondResponse extends BaseResponse {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * catid : 16
         * catname : 酒
         */

        private List<Category> top;
        /**
         * catid : 19
         * catname : 家纺
         */

        private List<Category> list;
        /**
         * catid : 23
         * catname : 工具
         */

        private List<Category> like;
        /**
         * catid : 22
         * catname : 厨电
         */

        private List<Category> foru;

        public List<Category> getTop() {
            return top;
        }

        public void setTop(List<Category> top) {
            this.top = top;
        }

        public List<Category> getList() {
            return list;
        }

        public void setList(List<Category> list) {
            this.list = list;
        }

        public List<Category> getLike() {
            return like;
        }

        public void setLike(List<Category> like) {
            this.like = like;
        }

        public List<Category> getForu() {
            return foru;
        }

        public void setForu(List<Category> foru) {
            this.foru = foru;
        }

   
    }
}

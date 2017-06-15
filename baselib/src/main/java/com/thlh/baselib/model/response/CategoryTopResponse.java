package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Category;
import com.thlh.baselib.model.CategorySuper;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class CategoryTopResponse extends BaseResponse {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * catid : 49
         * catname : 美家精品
         */

        private List<Category> top;
        /**
         * catid : 0
         * catname : 常用类别
         * child : [{"catid":"17","catname":"葡萄酒"},{"catid":"32","catname":"米/ 面/ 油/ 干货"},{"catid":"33","catname":"方便速食"},{"catid":"44","catname":"登山包"},{"catid":"46","catname":"智能冰箱"},{"catid":"48","catname":"智能电视"},{"catid":"61","catname":"大家电"},{"catid":"62","catname":"小家电"},{"catid":"63","catname":"厨卫电器"}]
         */

        private List<CategorySuper> list;

        public List<Category> getTop() {
            return top;
        }

        public void setTop(List<Category> top) {
            this.top = top;
        }

        public List<CategorySuper> getList() {
            return list;
        }

        public void setList(List<CategorySuper> list) {
            this.list = list;
        }



    }

}

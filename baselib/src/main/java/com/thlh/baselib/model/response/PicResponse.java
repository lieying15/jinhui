package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

import java.util.List;

/**
 * 图片列表（商品晒图）
 */
public class PicResponse extends BaseResponse {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * url : /BmUploadFile/memberthumb2016/20160817/1471399410136.jpeg
         */

        private List<Pic> pic;

        public List<Pic> getPic() {
            return pic;
        }

        public void setPic(List<Pic> pic) {
            this.pic = pic;
        }

    }
}

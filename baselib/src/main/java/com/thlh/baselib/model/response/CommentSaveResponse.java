package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.CommentSave;

/**
 * Created by Administrator on 2016/6/6.
 */
public class CommentSaveResponse extends BaseResponse {

    /**
     * comment : {"rate":"2","comment":"这个商品真的很好","show_name":"1","pic":[]}
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
         * rate : 2
         * comment : 这个商品真的很好
         * show_name : 1
         * pic : []
         */

        private CommentSave comment;

        public CommentSave getComment() {
            return comment;
        }

        public void setComment(CommentSave comment) {
            this.comment = comment;
        }

    }
}

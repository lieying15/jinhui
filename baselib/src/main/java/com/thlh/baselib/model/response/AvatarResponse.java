package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/4/27.
 */
public class AvatarResponse extends BaseResponse {


    /**
     * photo_url : /BmUploadFile/memberthumb2016/20160509/1462791208486.png
     */

    private String photo_url;
    /**
     * photo_url : /BmUploadFile/memberthumb2016/20160509/1462791208486.png
     */

    private DataBean data;

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String photo_url;

        public String getPhoto_url() {
            return photo_url;
        }

        public void setPhoto_url(String photo_url) {
            this.photo_url = photo_url;
        }
    }



}

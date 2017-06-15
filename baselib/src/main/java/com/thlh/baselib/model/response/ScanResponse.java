package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/8/1.
 */
public class ScanResponse extends BaseResponse {


    /**
     * msg : 扫码已被识别
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

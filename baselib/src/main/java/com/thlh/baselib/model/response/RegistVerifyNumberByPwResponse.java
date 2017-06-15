package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;

/**
 * 通过密码绑定手机的回掉
 */
public class RegistVerifyNumberByPwResponse extends BaseResponse {


    /**
     * tmp : NTUwZTFiYWZlMDc3ZmYwYjBiNjdmNGUzMmYyOWQ3NTE=
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String tmp;

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }
    }
}

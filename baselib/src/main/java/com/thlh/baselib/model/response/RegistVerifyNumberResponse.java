package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;

/**
 * Created by Administrator on 2016/4/25.
 */
public class RegistVerifyNumberResponse extends BaseResponse {


    /**
     * "code": "603680"
     */

    private CodeData data;

    public CodeData getData() {
        return data;
    }

    public void setData(CodeData data) {
        this.data = data;
    }

    public static class CodeData {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}

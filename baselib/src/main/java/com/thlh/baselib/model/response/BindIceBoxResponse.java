package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Account;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BindIceBoxResponse extends BaseResponse {


    /**
     * address : {"id":"32","name":"我的","address":"新的地址,福建 福州 鼓楼区","phone":"13701060000","is_on":"1"}
     * equipment : {"id":"454","uuid":"121241241241241","code":""}
     * store : {"name":"我的小店啊","store_name":"实体小店","address":"北京市北京市朝阳区动卫路9好","contact":"张三","phone":"65433333"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private Account account;


        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }
    }
}
package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Address;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class AddrListResponse extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 葫芦娃
         * address : 富丽双子座
         * phone : 13240396344
         * is_on : 1
         */

        private List<Address> address;

        public List<Address> getAddress() {
            return address;
        }

        public void setAddress(List<Address> address) {
            this.address = address;
        }


    }
}

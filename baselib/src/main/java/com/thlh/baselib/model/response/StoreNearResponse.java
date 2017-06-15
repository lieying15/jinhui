package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.StoreNear;

import java.util.List;

/**
 * 附近小店
 */
public class StoreNearResponse extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private List<StoreNear> store;

        public List<StoreNear> getStoreNear() {
            return store;
        }

        public void setStoreNear(List<StoreNear> store) {
            this.store = store;
        }


    }
}

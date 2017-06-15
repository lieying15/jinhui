package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Storage;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class StorageResponse extends BaseResponse{

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * item_id : 161
         * is_shelves : 1
         */

        private List<Storage> storage;

        public List<Storage> getStorage() {
            return storage;
        }

        public void setStorage(List<Storage> storage) {
            this.storage = storage;
        }

    }
}

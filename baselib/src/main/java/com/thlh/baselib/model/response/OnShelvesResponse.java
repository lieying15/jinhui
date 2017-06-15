package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.OnShelves;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class OnShelvesResponse extends BaseResponse{

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

        private List<OnShelves> on_shelves;

        public List<OnShelves> getOn_shelves() {
            return on_shelves;
        }

        public void setOn_shelves(List<OnShelves> on_shelves) {
            this.on_shelves = on_shelves;
        }

    }
}

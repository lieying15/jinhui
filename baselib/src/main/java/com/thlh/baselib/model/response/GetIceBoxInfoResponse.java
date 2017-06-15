package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.Equipment;
import com.thlh.baselib.model.Store;

/**
 * Created by Administrator on 2016/4/25.
 */
public class GetIceBoxInfoResponse extends BaseResponse {


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
        /**
         * id : 32
         * name : 我的
         * address : 新的地址,福建 福州 鼓楼区
         * phone : 13701060000
         * is_on : 1
         */

        private Address address;
        /**
         * id : 454
         * uuid : 121241241241241
         * code : 
         */

        private Equipment equipment;
        /**
         * name : 我的小店啊
         * store_name : 实体小店
         * address : 北京市北京市朝阳区动卫路9好
         * contact : 张三
         * phone : 65433333
         */

        private Store store;
//        "store": {
//            "name": "我的小店啊",
//                    "store_name": "实体小店",
//                    "address": "北京市北京市朝阳区动卫路9好",
//                    "contact": "张三",
//                    "phone": "65433333"
//        }
        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Equipment getEquipment() {
            return equipment;
        }

        public void setEquipment(Equipment equipment) {
            this.equipment = equipment;
        }

        public Store getStore() {
            return store;
        }

        public void setStore(Store store) {
            this.store = store;
        }


    }
}
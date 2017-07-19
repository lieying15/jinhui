package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * Created by LD on 2017/7/18.
 */

public class StoreStateResponse extends BaseResponse {

    /**
     * data : {"is_bind":0,"store":{"id":"795","store_name":"测试-店铺","logo":"/BmUploadFile/store/NjF/iNG/E2N/1491358168461.png","province":"北京市","city":"北京市","district":"海淀区","address":"黑泉路8号","lat":"40.04195800","lng":"116.37874300","rating":"0","phone":"13911091776"}}
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
         * is_bind : 0
         * store : {"id":"795","store_name":"测试-店铺","logo":"/BmUploadFile/store/NjF/iNG/E2N/1491358168461.png","province":"北京市","city":"北京市","district":"海淀区","address":"黑泉路8号","lat":"40.04195800","lng":"116.37874300","rating":"0","phone":"13911091776"}
         */

        private int is_bind;
        private StoreBean store;

        public int getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(int is_bind) {
            this.is_bind = is_bind;
        }

        public StoreBean getStore() {
            return store;
        }

        public void setStore(StoreBean store) {
            this.store = store;
        }

        public static class StoreBean {
            /**
             * id : 795
             * store_name : 测试-店铺
             * logo : /BmUploadFile/store/NjF/iNG/E2N/1491358168461.png
             * province : 北京市
             * city : 北京市
             * district : 海淀区
             * address : 黑泉路8号
             * lat : 40.04195800
             * lng : 116.37874300
             * rating : 0
             * phone : 13911091776
             */

            private String id;
            private String store_name;
            private String logo;
            private String province;
            private String city;
            private String district;
            private String address;
            private String lat;
            private String lng;
            private String rating;
            private String phone;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}

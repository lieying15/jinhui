package com.thlh.baselib.model.response;


import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Account;
import com.thlh.baselib.model.Address;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class LoginResponse extends BaseResponse {
    /**
     * account : {"token":"8292693e15324b07abd13738fdbe9b8ecb26108f603c0c10f75df6b9a77cb13b2f655837bfdd4ffad9d5b7834fa629dfc77d8530aac68450594092b7723d6198","nickname":"","avatar":"","shop_id":"0"}
     * address : [{"id":"19","name":"宋帅","address":"北京市朝阳区","phone":"18701194198","is_on":"0"},{"id":"22","name":"宋帅","address":"北京市，朝阳区，东三环中路55号富力双子座B座2502","phone":"18701194198","is_on":"1"}]
     * order_info : {"wait_pay":"3","wait_deliver":"8","wait_get":"0","return_goods":"0","wait_comment":"0"}
     * systeminfo : {"info_amount":"3"}
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
         * token : 8292693e15324b07abd13738fdbe9b8ecb26108f603c0c10f75df6b9a77cb13b2f655837bfdd4ffad9d5b7834fa629dfc77d8530aac68450594092b7723d6198
         * nickname :
         * avatar :
         * shop_id : 0
         */

        private Account account;
        /**
         * wait_pay : 3
         * wait_deliver : 8
         * wait_get : 0
         * return_goods : 0
         * wait_comment : 0
         */

        private UserOderInfo order_info;
        /**
         * info_amount : 3
         */

        private UserSysteminfo systeminfo;
        /**
         * id : 19
         * name : 宋帅
         * address : 北京市朝阳区
         * phone : 18701194198
         * is_on : 0
         */

        private List<Address> address;

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }

        public UserOderInfo getOrder_info() {
            return order_info;
        }

        public void setOrder_info(UserOderInfo order_info) {
            this.order_info = order_info;
        }

        public UserSysteminfo getSysteminfo() {
            return systeminfo;
        }

        public void setSysteminfo(UserSysteminfo systeminfo) {
            this.systeminfo = systeminfo;
        }

        public List<Address> getAddress() {
            return address;
        }

        public void setAddress(List<Address> address) {
            this.address = address;
        }

        public static class UserOderInfo {
            private String wait_pay;
            private String wait_deliver;
            private String wait_get;
            private String return_goods;
            private String wait_comment;

            public String getWait_pay() {
                return wait_pay;
            }

            public void setWait_pay(String wait_pay) {
                this.wait_pay = wait_pay;
            }

            public String getWait_deliver() {
                return wait_deliver;
            }

            public void setWait_deliver(String wait_deliver) {
                this.wait_deliver = wait_deliver;
            }

            public String getWait_get() {
                return wait_get;
            }

            public void setWait_get(String wait_get) {
                this.wait_get = wait_get;
            }

            public String getReturn_goods() {
                return return_goods;
            }

            public void setReturn_goods(String return_goods) {
                this.return_goods = return_goods;
            }

            public String getWait_comment() {
                return wait_comment;
            }

            public void setWait_comment(String wait_comment) {
                this.wait_comment = wait_comment;
            }
        }

        public static class UserSysteminfo {
            private String info_amount;

            public String getInfo_amount() {
                return info_amount;
            }

            public void setInfo_amount(String info_amount) {
                this.info_amount = info_amount;
            }
        }

    }
}

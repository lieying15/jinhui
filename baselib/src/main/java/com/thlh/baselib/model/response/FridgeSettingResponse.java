package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;

/**
 * 冰箱参数接口
 */
public class FridgeSettingResponse extends BaseResponse {


    /**
     * equipment_id : 662
     * upper : 7
     * lower : -16
     * coolroom_kg : 0
     * fastcool_kg : 0
     * fastfrozen_kg : 0
     * smart_kg : 0
     * lock_kg : 0
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String equipment_id;
        private String upper;
        private String lower;
        private String coolroom_kg; //冷藏室开关
        private String fastcool_kg; //速冷开关
        private String fastfrozen_kg;//速冻开关
        private String smart_kg; //智能开关
        private String lock_kg; //锁定开关

        public String getEquipment_id() {
            return equipment_id;
        }

        public void setEquipment_id(String equipment_id) {
            this.equipment_id = equipment_id;
        }

        public String getUpper() {
            return upper;
        }

        public void setUpper(String upper) {
            this.upper = upper;
        }

        public String getLower() {
            return lower;
        }

        public void setLower(String lower) {
            this.lower = lower;
        }

        public String getCoolroom_kg() {
            return coolroom_kg;
        }

        public void setCoolroom_kg(String coolroom_kg) {
            this.coolroom_kg = coolroom_kg;
        }

        public String getFastcool_kg() {
            return fastcool_kg;
        }

        public void setFastcool_kg(String fastcool_kg) {
            this.fastcool_kg = fastcool_kg;
        }

        public String getFastfrozen_kg() {
            return fastfrozen_kg;
        }

        public void setFastfrozen_kg(String fastfrozen_kg) {
            this.fastfrozen_kg = fastfrozen_kg;
        }

        public String getSmart_kg() {
            return smart_kg;
        }

        public void setSmart_kg(String smart_kg) {
            this.smart_kg = smart_kg;
        }

        public String getLock_kg() {
            return lock_kg;
        }

        public void setLock_kg(String lock_kg) {
            this.lock_kg = lock_kg;
        }
    }
}

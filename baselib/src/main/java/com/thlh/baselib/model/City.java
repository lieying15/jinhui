package com.thlh.baselib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class City {

    /**
     * id : 52
     * name : 北京
     * district : [{"id":"500","name":"东城区"}]
     */

    private String id;
    private String name;
    /**
     * id : 500
     * name : 东城区
     */

    private List<District> district;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistrict() {
        return district;
    }

    public void setDistrict(List<District> district) {
        this.district = district;
    }

}

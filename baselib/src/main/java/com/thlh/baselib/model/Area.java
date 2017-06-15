package com.thlh.baselib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class Area {

    /**
     * id : 2
     * name : 北京
     * city : [{"id":"517","name":"延庆县"}]
     */

    private String id;
    private String name;
    /**
     * id : 517
     * name : 延庆县
     */

    private List<City> city;

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

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

}

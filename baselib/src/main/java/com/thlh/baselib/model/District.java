package com.thlh.baselib.model;

/**
 * 地址选择三级联动 区域
 */
public class District {

    public District(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * id : 410
     * name : 东市区
     */

    private String id;
    private String name;

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
}

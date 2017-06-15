package com.thlh.baselib.model;

/**
 * Created by Administrator on 2016/4/27.
 */
public class Category {
    protected String catid;
    protected String catname;

    public Category(){}
    public Category(String catid, String catname) {
        this.catid = catid;
        this.catname = catname;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }
}

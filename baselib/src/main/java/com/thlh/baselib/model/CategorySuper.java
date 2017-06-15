package com.thlh.baselib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class CategorySuper extends Category{

    /**
     * catid : 17
     * catname : 葡萄酒
     */

    private List<Category> child;

    public List<Category> getChild() {
        return child;
    }

    public void setChild(List<Category> child) {
        this.child = child;
    }

}

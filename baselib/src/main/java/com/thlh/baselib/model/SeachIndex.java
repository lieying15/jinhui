package com.thlh.baselib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SeachIndex {
    private List<String> hot_search;
    private List<String> hot;

    public List<String> getHot_search() {
        return hot_search;
    }

    public void setHot_search(List<String> hot_search) {
        this.hot_search = hot_search;
    }

    public List<String> getHot() {
        return hot;
    }

    public void setHot(List<String> hot) {
        this.hot = hot;
    }
}

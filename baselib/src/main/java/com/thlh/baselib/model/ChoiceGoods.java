package com.thlh.baselib.model;

/**
 * 用户账户
 */
public class ChoiceGoods {


    /**
     * pic : http://v2.mjmw365.com/images/
     * title : 什么什么
     * subtile : 什么什么sub
     * item_id : 12
     */

    private String pic;
    private String title;
    private String subtitle;
    private int item_id;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtile() {
        return subtitle;
    }

    public void setSubtile(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }
}

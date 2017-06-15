package com.thlh.baselib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class CommentSave {

    /**
     * rate : 4
     * comment : aaaa
     * show_name : 1
     * pic : ["/images/comment/YTd/lZm/YwN/1470766541466.jpeg","/images/comment/YTd/lZm/YwN/1470766541938.jpeg"]
     */

    private String rate;
    private String comment;
    private String show_name;
    private List<String> pic;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }
}

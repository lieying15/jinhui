package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Comment;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class    CommentResponse extends BaseResponse {

    /**
     * total : 1
     * total_page : 1
     * current_page : 1
     * count : 20
     * good : 1
     * comments : [{"rate":"4","comment":"物品不错~~","name":null,"pic":["/images/comment/Y2U/2Ym/I0N/1467821450675."],"answer":""}]
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int total;
        private int total_page;
        private int current_page;
        private int count;
        private double good;
        private String rate1;
        private String rate2;
        private String rate3;

        /**
         * rate : 4
         * comment : 物品不错~~
         * name : null
         * pic : ["/images/comment/Y2U/2Ym/I0N/1467821450675."]
         * answer :
         */

        private List<Comment> comments;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getGood() {
            return good;
        }

        public void setGood(double good) {
            this.good = good;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        public String getRate1() {
            return rate1;
        }

        public void setRate1(String rate1) {
            this.rate1 = rate1;
        }

        public String getRate2() {
            return rate2;
        }

        public void setRate2(String rate2) {
            this.rate2 = rate2;
        }

        public String getRate3() {
            return rate3;
        }

        public void setRate3(String rate3) {
            this.rate3 = rate3;
        }
    }
}

package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Comment implements Parcelable {

    /**
     * rate : 5
     * comment : 评论内容
     * name : 匿名
     * answer : 回复评论
     * pic : ["/BmUploadFile/message/2016/20160629/1467187618.jpg"]
     */

    private String rate;
    private String comment;
    private String name;
    private String answer;
    private String inputtime;
    private String order_time;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rate);
        dest.writeString(this.comment);
//        dest.writeString(this.name);
        dest.writeString(this.answer);
        dest.writeString(this.inputtime);
        dest.writeString(this.order_time);
        dest.writeStringList(this.pic);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.rate = in.readString();
        this.comment = in.readString();
//        this.name = in.readString();
        this.answer = in.readString();
        this.inputtime = in.readString();
        this.order_time = in.readString();
        this.pic = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}

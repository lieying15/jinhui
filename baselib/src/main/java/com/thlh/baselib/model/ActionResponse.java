package com.thlh.baselib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 操作的回调类
 */

public class ActionResponse implements Parcelable {
    private boolean isSuccess = true;
    private String headertitle;
    private String title;
    private String content;
    private String backStr = "返回首页";
    private String backType;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getHeadertitle() {
        return headertitle;
    }

    public void setHeadertitle(String headertitle) {
        this.headertitle = headertitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBackStr() {
        return backStr;
    }

    public void setBackStr(String backStr) {
        this.backStr = backStr;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSuccess ? (byte) 1 : (byte) 0);
        dest.writeString(this.headertitle);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.backStr);
        dest.writeString(this.backType);
    }

    public ActionResponse() {
    }

    protected ActionResponse(Parcel in) {
        this.isSuccess = in.readByte() != 0;
        this.headertitle = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.backStr = in.readString();
        this.backType = in.readString();
    }

    public static final Creator<ActionResponse> CREATOR = new Creator<ActionResponse>() {
        @Override
        public ActionResponse createFromParcel(Parcel source) {
            return new ActionResponse(source);
        }

        @Override
        public ActionResponse[] newArray(int size) {
            return new ActionResponse[size];
        }
    };
}

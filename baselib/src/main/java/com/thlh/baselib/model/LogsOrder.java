package com.thlh.baselib.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 消息类
 */
@Entity
public class LogsOrder {
    @Id
    private Long dbid;
    private String id;
    private String order_id;
    private String message;
    private String inputtime;
    private boolean isRead = false;
    @Generated(hash = 144199224)
    public LogsOrder(Long dbid, String id, String order_id, String message,
            String inputtime, boolean isRead) {
        this.dbid = dbid;
        this.id = id;
        this.order_id = order_id;
        this.message = message;
        this.inputtime = inputtime;
        this.isRead = isRead;
    }
    @Generated(hash = 1854283841)
    public LogsOrder() {
    }
    public Long getDbid() {
        return this.dbid;
    }
    public void setDbid(Long dbid) {
        this.dbid = dbid;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOrder_id() {
        return this.order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getInputtime() {
        return this.inputtime;
    }
    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
   
}

package com.thlh.baselib.base;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BaseResponse{
    private int err_code;
    private String err_msg;


    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }


}

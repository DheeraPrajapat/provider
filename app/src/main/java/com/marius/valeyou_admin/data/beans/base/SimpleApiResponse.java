package com.marius.valeyou_admin.data.beans.base;

import com.google.gson.annotations.SerializedName;

public class SimpleApiResponse {
    /**
     * success : 1
     * code : 200
     * msg : Sign Up Successfully
     */

    @SerializedName("success")
    private int success;
    @SerializedName("code")
    private int status;
    @SerializedName("msg")
    private String msg;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int code) {
        this.status = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

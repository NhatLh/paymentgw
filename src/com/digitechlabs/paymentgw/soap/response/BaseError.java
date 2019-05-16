package com.digitechlabs.paymentgw.soap.response;

public class BaseError {
    private String desc;
    private String error_code;
    private Object data;

    public BaseError() {
    }

    public BaseError(String desc, Object data) {
        this.error_code = "UNKNOWN_ERROR";
        this.desc = desc;
        this.data = data;
    }

    public BaseError(String desc, String error_code, Object data) {
        this.desc = desc;
        this.error_code = error_code;
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getError_code() {
        return error_code;
    }

    public BaseError setError_code(String error_code) {
        this.error_code = error_code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

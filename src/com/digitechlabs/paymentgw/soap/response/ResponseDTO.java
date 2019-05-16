package com.digitechlabs.paymentgw.soap.response;

public class ResponseDTO {

    private boolean success = true;
    private Object data;
    private Object meta;
    private BaseError error;

    public ResponseDTO() {
    }

    public ResponseDTO(boolean success, Object data, Object meta, BaseError error) {
        this.success = success;
        this.data = data;
        this.meta = meta;
        this.error = error;
    }

    public ResponseDTO(boolean success, Object data, Object meta) {
        this.success = success;
        this.data = data;
        this.meta = meta;
    }

    public ResponseDTO(Object data, Object meta) {
        this.data = data;
        this.meta = meta;
    }

    public ResponseDTO(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResponseDTO setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseDTO setData(Object data) {
        this.data = data;
        return this;
    }

    public Object getMeta() {
        return meta;
    }

    public ResponseDTO setMeta(Object meta) {
        this.meta = meta;
        return this;
    }

    public BaseError getError() {
        return error;
    }

    public ResponseDTO setError(BaseError error) {
        this.error = error;
        return this;
    }
}

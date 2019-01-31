
package com.digitechlabs.paymentgw.restobject;

public class NotifyResponse {

    private String success;
    private String data;
    private String meta;
    private String message;

    public NotifyResponse(String success, String data, String meta, String message) {
        this.success = success;
        this.data = data;
        this.meta = meta;
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

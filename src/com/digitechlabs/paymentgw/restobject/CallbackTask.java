package com.digitechlabs.paymentgw.restobject;

public class CallbackTask {

    private String id;
    private String order_id;
    private String status;
    private String price_amount;
    private String price_currency;
    private String receive_currency;
    private String receive_amount;
    private String pay_amount;
    private String pay_currency;
    private String created_at;
    private String token;

    public CallbackTask(String id, String order_id, String status, String price_amount, String price_currency, String receive_currency, String receive_amount, String pay_amount, String pay_currency, String created_at, String token) {
        this.id = id;
        this.order_id = order_id;
        this.status = status;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.receive_currency = receive_currency;
        this.receive_amount = receive_amount;
        this.pay_amount = pay_amount;
        this.pay_currency = pay_currency;
        this.created_at = created_at;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice_amount() {
        return price_amount;
    }

    public void setPrice_amount(String price_amount) {
        this.price_amount = price_amount;
    }

    public String getPrice_currency() {
        return price_currency;
    }

    public void setPrice_currency(String price_currency) {
        this.price_currency = price_currency;
    }

    public String getReceive_currency() {
        return receive_currency;
    }

    public void setReceive_currency(String receive_currency) {
        this.receive_currency = receive_currency;
    }

    public String getReceive_amount() {
        return receive_amount;
    }

    public void setReceive_amount(String receive_amount) {
        this.receive_amount = receive_amount;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getPay_currency() {
        return pay_currency;
    }

    public void setPay_currency(String pay_currency) {
        this.pay_currency = pay_currency;
    }

    public String getCreated_at() {
        return created_at.replace("%3A", ":").replace("%2B", "+");
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at.replace("%3A", ":").replace("%2B", "+");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String debugString() {
        return "[" + order_id + "]" + "status:" + status + ",token:" + token;
    }
}

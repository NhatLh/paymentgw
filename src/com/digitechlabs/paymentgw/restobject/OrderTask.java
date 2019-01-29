package com.digitechlabs.paymentgw.restobject;

public class OrderTask {

    private String private_key;
    private String order_id;
    private String price_amount;
    private String price_currency;
    private String receive_currency;
    private String title;
    private String description;
    private String callback_url;
    private String cancel_url;
    private String success_url;
    private String create_at;
    private String token;
    private String user_id = "";
    private String booking_number = "";

    public OrderTask(String private_key, String order_id, String price_amount, String price_currency, String receive_currency, String title,
            String description, String cancel_url, String succes_url, String user_id, String booking_number) {
        this.private_key = private_key;
        this.order_id = order_id;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.title = title;
        this.description = description;
        this.cancel_url = cancel_url;
        this.success_url = succes_url;
        this.user_id = user_id;
        this.booking_number = booking_number;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCancel_url() {
        return cancel_url;
    }

    public void setCancel_url(String cancel_url) {
        this.cancel_url = cancel_url;
    }

    public String getSuccess_url() {
        return success_url;
    }

    public void setSuccess_url(String success_url) {
        this.success_url = success_url;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getReceive_currency() {
        return receive_currency;
    }

    public void setReceive_currency(String receive_currency) {
        this.receive_currency = receive_currency;
    }

    public String getUser_id() {
        if (user_id == null) {
            user_id = "";
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(String booking_number) {
        this.booking_number = booking_number;
    }

    public String[] toArray() {
        return new String[]{order_id, price_amount, price_currency, receive_currency, title, description, callback_url, cancel_url, success_url, token};
    }

}

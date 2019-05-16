package com.digitechlabs.paymentgw.restobject;

public class OrderResponse {

    private String id;
    private String status;
    private boolean do_not_convert;
    private String price_currency;
    private String price_amount;
    private boolean lightning_network;
    private String receive_currency;
    private String receive_amount;
    private String created_at;
    private String order_id;
    private String payment_url;
    private String token;

    public OrderResponse(String id, String status, boolean do_not_convert, String price_currency, String price_amount, boolean lightning_network, String receive_currency, String receive_amount, String created_at, String order_id, String payment_url, String token) {
        this.id = id;
        this.status = status;
        this.do_not_convert = do_not_convert;
        this.price_currency = price_currency;
        this.price_amount = price_amount;
        this.lightning_network = lightning_network;
        this.receive_currency = receive_currency;
        this.receive_amount = receive_amount;
        this.created_at = created_at;
        this.order_id = order_id;
        this.payment_url = payment_url;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDo_not_convert() {
        return do_not_convert;
    }

    public void setDo_not_convert(boolean do_not_convert) {
        this.do_not_convert = do_not_convert;
    }

    public String getPrice_currency() {
        return price_currency;
    }

    public void setPrice_currency(String price_currency) {
        this.price_currency = price_currency;
    }

    public String getPrice_amount() {
        return price_amount;
    }

    public void setPrice_amount(String price_amount) {
        this.price_amount = price_amount;
    }

    public boolean isLightning_network() {
        return lightning_network;
    }

    public void setLightning_network(boolean lightning_network) {
        this.lightning_network = lightning_network;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

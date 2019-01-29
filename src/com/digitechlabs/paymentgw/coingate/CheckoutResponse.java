/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.coingate;

/**
 *
 * @author FOCUS
 */
public class CheckoutResponse {

    String id;
    String order_id;
    String pay_amount;
    String pay_currency;
    String payment_address;
    String payment_url;
    String price_amount;
    String price_currency;
    String receive_amount;
    String receive_currency;
    String status;
    String created_at;
    String expire_at;

    public CheckoutResponse(String id, String order_id, String pay_amount, String pay_currency, String payment_address, String payment_url, String price_amount, String price_currency, String receive_amount, String receive_currency, String status, String created_at, String expire_at) {
        this.id = id;
        this.order_id = order_id;
        this.pay_amount = pay_amount;
        this.pay_currency = pay_currency;
        this.payment_address = payment_address;
        this.payment_url = payment_url;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.receive_amount = receive_amount;
        this.receive_currency = receive_currency;
        this.status = status;
        this.created_at = created_at;
        this.expire_at = expire_at;
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

    public String getPayment_address() {
        return payment_address;
    }

    public void setPayment_address(String payment_address) {
        this.payment_address = payment_address;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
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

    public String getReceive_amount() {
        return receive_amount;
    }

    public void setReceive_amount(String receive_amount) {
        this.receive_amount = receive_amount;
    }

    public String getReceive_currency() {
        return receive_currency;
    }

    public void setReceive_currency(String receive_currency) {
        this.receive_currency = receive_currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(String expire_at) {
        this.expire_at = expire_at;
    }

}

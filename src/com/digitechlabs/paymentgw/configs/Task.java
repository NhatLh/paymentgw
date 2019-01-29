/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.configs;

import com.digitechlabs.paymentgw.soap.ProcessRequest;
import java.sql.Timestamp;

public class Task {

    private String id;
    private String order_id;
    private String price_amount;
    private String price_currency;
    private String title;
    private String description;
    private Timestamp create_at;
    private String token;
    private String user_id;
    private String email;
    private String receive_currency;
    private String receive_amount;
    private String pay_amount;
    private String pay_currency;
    private String status;

    private int type;

    public Task(String order_id, String price_amount, String price_currency, String title, String description, Timestamp create_at, String token, String user_id, String email) {
        this.order_id = order_id;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.title = title;
        this.description = description;
        this.create_at = create_at;
        this.token = token;
        this.user_id = user_id;
        this.email = email;

        this.type = ProcessRequest.API_TYPE_ORDER;
    }

    public Task(String id, String order_id, String price_amount, String price_currency, Timestamp create_at, String token, String receive_currency, String receive_amount, String pay_amount, String pay_currency, String status) {
        this.id = id;
        this.order_id = order_id;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.create_at = create_at;
        this.token = token;
        this.receive_currency = receive_currency;
        this.receive_amount = receive_amount;
        this.pay_amount = pay_amount;
        this.pay_currency = pay_currency;
        this.status = status;

        this.type = ProcessRequest.API_TYPE_CALLBACK;
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

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}

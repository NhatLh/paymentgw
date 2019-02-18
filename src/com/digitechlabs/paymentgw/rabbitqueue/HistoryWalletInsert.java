/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.rabbitqueue;

import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class HistoryWalletInsert {

    private String id;
    private String status;
    private double amount;
    private Timestamp created_at;
    private String from_address;
    private String to_address;
    private String transaction_type;
    private String user_id;
    private Timestamp expired_time;
    private String currency;
    private String order_id;
    private String transaction_id;

    public HistoryWalletInsert(String id, String status, double amount, Timestamp created_at, String from_address, String to_address, String transaction_type, String user_id, Timestamp expired_time, String currency, String order_id, String transaction_id) {
        this.id = id;
        this.status = status;
        this.amount = amount;
        this.created_at = created_at;
        this.from_address = from_address;
        this.to_address = to_address;
        this.transaction_type = transaction_type;
        this.user_id = user_id;
        this.expired_time = expired_time;
        this.currency = currency;
        this.order_id = order_id;
        this.transaction_id = transaction_id;
    }

    public HistoryWalletInsert() {

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Timestamp getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Timestamp expired_time) {
        this.expired_time = expired_time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

}

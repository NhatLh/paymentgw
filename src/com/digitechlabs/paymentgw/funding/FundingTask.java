/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.funding;

/**
 *
 * @author Admin
 */
public class FundingTask {

    private String id;
    private String user_id;
    private String fund;
    private String currency;
    private String transaction_type;
    private String reason;

    public FundingTask(String id, String user_id, String fund, String currency, String transaction_type, String reason) {
        this.id = id;
        this.user_id = user_id;
        this.fund = fund;
        this.currency = currency;
        this.transaction_type = transaction_type;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

}

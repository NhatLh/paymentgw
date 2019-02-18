/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.rabbitqueue;

/**
 *
 * @author Admin
 */
public class HistoryWalletUpdate {

    private String id;
    private String status;
    private String transType;

    public HistoryWalletUpdate(String id, String status, String transType) {
        this.id = id;
        this.status = status;
        this.transType = transType;
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

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.utils;

/**
 *
 * @author Admin
 */
public class PaypalPayment {

    private String id;
    private String orderID;
    private String exeUrl;

    public PaypalPayment(String id, String orderID, String exeUrl) {
        this.id = id;
        this.orderID = orderID;
        this.exeUrl = exeUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getExeUrl() {
        return exeUrl;
    }

    public void setExeUrl(String exeUrl) {
        this.exeUrl = exeUrl;
    }

}

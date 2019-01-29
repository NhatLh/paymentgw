/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.restobject;

/**
 *
 * @author nhatlh1
 */
public class BookResponseTask {

    private String order_id;
    private String status;
    private String reason;

    public BookResponseTask(String order_id, String status, String reason) {
        this.order_id = order_id;
        this.status = status;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.orderdetails;

/**
 *
 * @author Admin
 */
public class Response {

    private String status;
    private String description;
    private OrderDetails[] data;

    public Response(String status, String description, OrderDetails[] data) {
        this.status = status;
        this.description = description;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderDetails[] getData() {
        return data;
    }

    public void setData(OrderDetails[] data) {
        this.data = data;
    }

}

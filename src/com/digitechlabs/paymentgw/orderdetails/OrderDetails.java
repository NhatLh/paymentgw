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
public class OrderDetails {

    private String order_id;
    private String payment_method;
    private double amount;

    public OrderDetails(String order_id, String payment_method, double amount) {
        this.order_id = order_id;
        this.payment_method = payment_method;
        this.amount = amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}

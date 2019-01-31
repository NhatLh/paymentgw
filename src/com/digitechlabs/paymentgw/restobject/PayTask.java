/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.restobject;

/**
 *
 * @author Admin
 */
public class PayTask {

    private String order_id;
    private String user_id;
    private String pay_amount;
    private String pay_currency;
    private String booking_number = "";

    public PayTask(String order_id, String user_id, String pay_amount, String pay_currency, String booking_number) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.pay_amount = pay_amount;
        this.pay_currency = pay_currency;
        this.booking_number = booking_number;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(String booking_number) {
        this.booking_number = booking_number;
    }

}

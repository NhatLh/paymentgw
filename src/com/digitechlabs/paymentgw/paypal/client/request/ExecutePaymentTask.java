/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.paypal.client.request;

/**
 *
 * @author Admin
 */
public class ExecutePaymentTask {

    private String payment_id;
    private String payer_id;

    public ExecutePaymentTask(String payment_id, String payer_id) {
        this.payment_id = payment_id;
        this.payer_id = payer_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

}

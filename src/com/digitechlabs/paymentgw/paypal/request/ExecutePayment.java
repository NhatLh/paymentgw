/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.paypal.request;

/**
 *
 * @author Admin
 */
public class ExecutePayment {

    private String payer_id;

    public ExecutePayment(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

}

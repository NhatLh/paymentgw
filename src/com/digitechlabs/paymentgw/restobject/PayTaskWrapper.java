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
public class PayTaskWrapper {

    private PayTask paytask;
    private String transID;

    public PayTaskWrapper(PayTask paytask, String transID) {
        this.paytask = paytask;
        this.transID = transID;
    }

    public PayTask getPaytask() {
        return paytask;
    }

    public void setPaytask(PayTask paytask) {
        this.paytask = paytask;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

}

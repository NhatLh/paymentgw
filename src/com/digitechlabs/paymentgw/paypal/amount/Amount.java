/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.paypal.amount;

/**
 *
 * @author Admin
 */
public class Amount {

    private String total;
    private String currency;
    private Details details;

    public Amount(String total, String currency, Details details) {
        this.total = total;
        this.currency = currency;
        this.details = details;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

}

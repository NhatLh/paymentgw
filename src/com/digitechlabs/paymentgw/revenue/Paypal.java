/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.revenue;

/**
 *
 * @author Admin
 */
public class Paypal {

    private String USD;
    private String EUR;
    private String AVA;

    public Paypal(String USD, String EUR, String AVA) {
        this.USD = USD;
        this.EUR = EUR;
        this.AVA = AVA;
    }

    public String getUSD() {
        return USD;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public String getEUR() {
        return EUR;
    }

    public void setEUR(String EUR) {
        this.EUR = EUR;
    }

    public String getAVA() {
        return AVA;
    }

    public void setAVA(String AVA) {
        this.AVA = AVA;
    }
}

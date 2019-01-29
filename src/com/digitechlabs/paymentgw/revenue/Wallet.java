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
public class Wallet {

    private double USD;
    private double EUR;
    private double AVA;

    public Wallet(double USD, double EUR, double AVA) {
        this.USD = USD;
        this.EUR = EUR;
        this.AVA = AVA;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public double getEUR() {
        return EUR;
    }

    public void setEUR(double EUR) {
        this.EUR = EUR;
    }

    public double getAVA() {
        return AVA;
    }

    public void setAVA(double AVA) {
        this.AVA = AVA;
    }

    public void sum(Wallet wallet) {
        this.USD = this.USD + wallet.getUSD();
        this.EUR = this.EUR + wallet.getEUR();
        this.AVA = this.AVA + wallet.getAVA();
    }
}

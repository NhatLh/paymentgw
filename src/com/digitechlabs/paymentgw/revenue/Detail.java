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
public class Detail {

    private Coingate coin_gate;
    private Wallet wallet;
    private Paypal paypal;

    public Detail(Coingate coin_gate, Wallet wallet, Paypal paypal) {
        this.coin_gate = coin_gate;
        this.wallet = wallet;
        this.paypal = paypal;
    }

    public Coingate getCoin_gate() {
        return coin_gate;
    }

    public void setCoin_gate(Coingate coin_gate) {
        this.coin_gate = coin_gate;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Paypal getPaypal() {
        return paypal;
    }

    public void setPaypal(Paypal paypal) {
        this.paypal = paypal;
    }

}

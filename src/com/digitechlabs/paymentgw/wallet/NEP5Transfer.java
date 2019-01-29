/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.wallet;

/**
 *
 * @author FOCUS
 */
public class NEP5Transfer {

    private String amount;
    private String from_address;
    private String to_address;

    public NEP5Transfer(String amount, String from_address, String to_address) {
        this.amount = amount;
        this.from_address = from_address;
        this.to_address = to_address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

}

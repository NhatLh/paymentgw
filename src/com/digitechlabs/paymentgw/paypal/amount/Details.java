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
public class Details {

    private String subtotal;
    private String tax;
    private String insurance;

    public Details(String subtotal, String tax, String insurance) {
        this.subtotal = subtotal;
        this.tax = tax;
        this.insurance = insurance;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

}

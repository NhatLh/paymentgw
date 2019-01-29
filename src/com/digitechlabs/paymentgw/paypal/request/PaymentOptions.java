/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.paypal.request;

import com.digitechlabs.paymentgw.configs.ConfigLoader;

/**
 *
 * @author Admin
 */
public class PaymentOptions {

    private String allowed_payment_method = "UNRESTRICTED";

    public PaymentOptions() {
        this.allowed_payment_method = ConfigLoader.getInstance().getAllowPaymentMethod();
    }

    public String getAllowed_payment_method() {
        return allowed_payment_method;
    }

    public void setAllowed_payment_method(String allowed_payment_method) {
        this.allowed_payment_method = allowed_payment_method;
    }

}

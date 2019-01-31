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
public class CreatePaymentTaskWrapper {

    private String url;
    private CreatePaymentTask task;

    public CreatePaymentTaskWrapper(CreatePaymentTask task) {
        this.task = task;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CreatePaymentTask getTask() {
        return task;
    }

    public void setTask(CreatePaymentTask task) {
        this.task = task;
    }

}

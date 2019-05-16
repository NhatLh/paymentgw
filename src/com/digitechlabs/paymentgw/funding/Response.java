/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.funding;

/**
 *
 * @author Admin
 */
public class Response {

    private String status;
    private String data;

    public Response(String status, String data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return data;
    }

    public void setDescription(String data) {
        this.data = data;
    }

}

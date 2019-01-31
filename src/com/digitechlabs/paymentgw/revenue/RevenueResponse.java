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
public class RevenueResponse {

    private String status;
    private String desciption;
    private Revenue data;

    public RevenueResponse(String status, String desciption, Revenue data) {
        this.status = status;
        this.desciption = desciption;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Revenue getData() {
        return data;
    }

    public void setData(Revenue data) {
        this.data = data;
    }

}

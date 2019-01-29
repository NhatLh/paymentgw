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
public class Data {

    private Revenue revenue;

    public Data(Revenue revenue) {
        this.revenue = revenue;
    }

    public Revenue getRevenue() {
        return revenue;
    }

    public void setRevenue(Revenue revenue) {
        this.revenue = revenue;
    }

}

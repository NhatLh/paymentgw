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
public class Revenue {

    private int total;
    private Detail detail;

    public Revenue(int total, Detail detail) {
        this.total = total;
        this.detail = detail;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

}

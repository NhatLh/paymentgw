/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.wallet;

public class Deposit {

    private String type;
    private String event;
    private DataDeposit data;

    public Deposit(String type, String event, DataDeposit data) {
        this.type = type;
        this.event = event;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public DataDeposit getData() {
        return data;
    }

    public void setData(DataDeposit data) {
        this.data = data;
    }

}

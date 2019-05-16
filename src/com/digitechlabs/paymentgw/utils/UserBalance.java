/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.utils;

/**
 *
 * @author Admin
 */
public class UserBalance {

    private String userID;
    private double available = 0;
    private double blocked = 0;
    private double award = 0;
    private double total;
    private int status = 0;

    public UserBalance() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
        this.total = available + blocked + award;
    }

    public double getBlocked() {
        return blocked;
    }

    public void setBlocked(double blocked) {
        this.blocked = blocked;
        this.total = available + blocked + award;
    }

    public double getAward() {
        return award;
    }

    public void setAward(double award) {
        this.award = award;
        this.total = available + blocked + award;
    }

    public double getTotal() {
        return available + blocked + award;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

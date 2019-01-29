/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.Object;

/**
 *
 * @author FOCUS
 */
public class UserWallet {

    private int userID;
    private String wallet_add;
    private String wallet_type;
    private boolean isActive;
    private float balance;
    private float award;

    public UserWallet(int userID, String wallet_add, String wallet_type, boolean isActive, float balance, float award) {
        this.userID = userID;
        this.wallet_add = wallet_add;
        this.wallet_type = wallet_type;
        this.isActive = isActive;
        this.balance = balance;
        this.award = award;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getWallet_add() {
        return wallet_add;
    }

    public void setWallet_add(String wallet_add) {
        this.wallet_add = wallet_add;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getAward() {
        return award;
    }

    public void setAward(float award) {
        this.award = award;
    }

}

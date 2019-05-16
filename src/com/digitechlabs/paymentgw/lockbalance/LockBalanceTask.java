/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.lockbalance;

/**
 *
 * @author Admin
 */
public class LockBalanceTask {

    private String id;
    private String user_id;
    private String block_amount;
    private String block_currency;
    private String block_reason;

    public LockBalanceTask(String id, String user_id, String block_amount, String block_currency, String block_reason) {
        this.id = id;
        this.user_id = user_id;
        this.block_amount = block_amount;
        this.block_currency = block_currency;
        this.block_reason = block_reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBlock_amount() {
        return block_amount;
    }

    public void setBlock_amount(String block_amount) {
        this.block_amount = block_amount;
    }

    public String getBlock_currency() {
        return block_currency;
    }

    public void setBlock_currency(String block_currency) {
        this.block_currency = block_currency;
    }

    public String getBlock_reason() {
        return block_reason;
    }

    public void setBlock_reason(String block_reason) {
        this.block_reason = block_reason;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.history;

import com.digitechlabs.paymentgw.rabbitqueue.HistoryWalletInsert;
import com.digitechlabs.paymentgw.rabbitqueue.HistoryWalletUpdate;

/**
 *
 * @author Admin
 */
public class HistoryRequest {

    private String type;
    private HistoryWalletInsert insert;
    private HistoryWalletUpdate update;

    public HistoryRequest(String type, HistoryWalletInsert insert, HistoryWalletUpdate update) {
        this.type = type;
        this.insert = insert;
        this.update = update;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HistoryWalletInsert getInsert() {
        return insert;
    }

    public void setInsert(HistoryWalletInsert insert) {
        this.insert = insert;
    }

    public HistoryWalletUpdate getUpdate() {
        return update;
    }

    public void setUpdate(HistoryWalletUpdate update) {
        this.update = update;
    }

}

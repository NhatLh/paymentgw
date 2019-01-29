/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.wallet;

/**
 *
 * @author FOCUS
 */
public class Tx {

    private String txid;
    private String confirmations;
    private String blockHash;
    private String blockTime;
    private NEP5Transfer[] NEP5Transfer;

    public Tx(String txid, String confirmations, String blockHash, String blockTime, NEP5Transfer[] NEP5Transfer) {
        this.txid = txid;
        this.confirmations = confirmations;
        this.blockHash = blockHash;
        this.blockTime = blockTime;
        this.NEP5Transfer = NEP5Transfer;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(String confirmations) {
        this.confirmations = confirmations;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    public NEP5Transfer[] getNEP5Transfer() {
        return NEP5Transfer;
    }

    public void setNEP5Transfer(NEP5Transfer[] NEP5Transfer) {
        this.NEP5Transfer = NEP5Transfer;
    }

}

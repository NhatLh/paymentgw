package com.digitechlabs.paymentgw.wallet;

public class DataDeposit {

    private String currency;
    private String txid;
    private String timestamp;
    private String address;
    private String amount;

    public DataDeposit(String currency, String txid, String timestamp, String address, String amount) {
        this.currency = currency;
        this.txid = txid;
        this.timestamp = timestamp;
        this.address = address;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}

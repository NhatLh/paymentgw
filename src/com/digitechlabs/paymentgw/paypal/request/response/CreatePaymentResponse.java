
package com.digitechlabs.paymentgw.paypal.request.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePaymentResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("intent")
    @Expose
    private String intent;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("payer")
    @Expose
    private Payer payer;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;
    @SerializedName("note_to_payer")
    @Expose
    private String noteToPayer;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getNoteToPayer() {
        return noteToPayer;
    }

    public void setNoteToPayer(String noteToPayer) {
        this.noteToPayer = noteToPayer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}

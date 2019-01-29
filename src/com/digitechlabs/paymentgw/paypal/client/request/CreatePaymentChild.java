package com.digitechlabs.paymentgw.paypal.client.request;

import com.digitechlabs.paymentgw.paypal.request.Payer;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transactions",
    "note_to_payer",
    "redirect_urls"
})
public class CreatePaymentChild {

    @JsonProperty("transactions")
    private List<Transaction> transactions = null;
    @JsonProperty("note_to_payer")
    private String note_to_payer;
    @JsonProperty("redirect_urls")
    private RedirectUrls redirect_urls;
    @JsonProperty("sale")
    private String intent = "sale";
    @JsonProperty("payer")
    private Payer payer = new Payer();

    public CreatePaymentChild(String note_to_payer, RedirectUrls redirect_urls) {
        this.note_to_payer = note_to_payer;
        this.redirect_urls = redirect_urls;
    }

    @JsonProperty("transactions")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @JsonProperty("transactions")
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @JsonProperty("note_to_payer")
    public String getNoteToPayer() {
        return note_to_payer;
    }

    @JsonProperty("note_to_payer")
    public void setNoteToPayer(String note_to_payer) {
        this.note_to_payer = note_to_payer;
    }

    @JsonProperty("redirect_urls")
    public RedirectUrls getRedirectUrls() {
        return redirect_urls;
    }

    @JsonProperty("redirect_urls")
    public void setRedirectUrls(RedirectUrls redirect_urls) {
        this.redirect_urls = redirect_urls;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

}

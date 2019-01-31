package com.digitechlabs.paymentgw.paypal.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "total",
    "currency",
    "details"
})
public class Amount {

    @JsonProperty("total")
    private double total;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("details")
    private Details details;

    @JsonProperty("total")
    public double getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(double total) {
        this.total = total;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("details")
    public Details getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(Details details) {
        this.details = details;
    }

}

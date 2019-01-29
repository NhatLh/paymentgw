
package com.digitechlabs.paymentgw.paypal.execute.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amount {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("details")
    @Expose
    private Details details;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

}

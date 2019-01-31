
package com.digitechlabs.paymentgw.paypal.execute.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelatedResource {

    @SerializedName("sale")
    @Expose
    private Sale sale;

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

}

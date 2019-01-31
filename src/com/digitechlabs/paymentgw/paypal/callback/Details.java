
package com.digitechlabs.paymentgw.paypal.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("subtotal")
    @Expose
    private String subtotal;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

}

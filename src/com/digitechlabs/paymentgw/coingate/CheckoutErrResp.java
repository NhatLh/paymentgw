
package com.digitechlabs.paymentgw.coingate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutErrResp {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("reason")
    @Expose
    private String reason;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

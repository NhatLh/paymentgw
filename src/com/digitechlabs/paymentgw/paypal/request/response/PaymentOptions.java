
package com.digitechlabs.paymentgw.paypal.request.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentOptions {

    @SerializedName("allowed_payment_method")
    @Expose
    private String allowedPaymentMethod;
    @SerializedName("recurring_flag")
    @Expose
    private Boolean recurringFlag;
    @SerializedName("skip_fmf")
    @Expose
    private Boolean skipFmf;

    public String getAllowedPaymentMethod() {
        return allowedPaymentMethod;
    }

    public void setAllowedPaymentMethod(String allowedPaymentMethod) {
        this.allowedPaymentMethod = allowedPaymentMethod;
    }

    public Boolean getRecurringFlag() {
        return recurringFlag;
    }

    public void setRecurringFlag(Boolean recurringFlag) {
        this.recurringFlag = recurringFlag;
    }

    public Boolean getSkipFmf() {
        return skipFmf;
    }

    public void setSkipFmf(Boolean skipFmf) {
        this.skipFmf = skipFmf;
    }

}

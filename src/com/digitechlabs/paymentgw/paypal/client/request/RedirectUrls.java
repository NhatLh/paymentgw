package com.digitechlabs.paymentgw.paypal.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "return_url",
    "cancel_url"
})
public class RedirectUrls {

    @JsonProperty("return_url")
    private String return_url;
    @JsonProperty("cancel_url")
    private String cancel_url;

    @JsonProperty("return_url")
    public String getReturnUrl() {
        return return_url;
    }

    @JsonProperty("return_url")
    public void setReturnUrl(String returnUrl) {
        this.return_url = returnUrl;
    }

    @JsonProperty("cancel_url")
    public String getCancelUrl() {
        return cancel_url;
    }

    @JsonProperty("cancel_url")
    public void setCancelUrl(String cancelUrl) {
        this.cancel_url = cancelUrl;
    }
}

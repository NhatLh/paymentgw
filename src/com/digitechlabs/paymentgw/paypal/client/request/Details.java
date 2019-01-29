package com.digitechlabs.paymentgw.paypal.client.request;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "subtotal",
    "tax",
    "insurance"
})
public class Details {

    @JsonProperty("subtotal")
    private String subtotal;
    @JsonProperty("tax")
    private String tax;
    @JsonProperty("insurance")
    private String insurance;

    @JsonProperty("subtotal")
    public String getSubtotal() {
        return subtotal;
    }

    @JsonProperty("subtotal")
    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    @JsonProperty("tax")
    public String getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(String tax) {
        this.tax = tax;
    }

    @JsonProperty("insurance")
    public String getInsurance() {
        return insurance;
    }

    @JsonProperty("insurance")
    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

}

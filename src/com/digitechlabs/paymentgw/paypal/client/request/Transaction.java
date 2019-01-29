package com.digitechlabs.paymentgw.paypal.client.request;

import com.digitechlabs.paymentgw.paypal.request.PaymentOptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "description",
    "custom",
    "item_list"
})
public class Transaction {

    @JsonProperty("amount")
    private Amount amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("custom")
    private String custom;
    @JsonProperty("item_list")
    private ItemList item_list;
    @JsonProperty("payment_options")
    private PaymentOptions payment_options = new PaymentOptions();

    @JsonProperty("amount")
    public Amount getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("custom")
    public String getCustom() {
        return custom;
    }

    @JsonProperty("custom")
    public void setCustom(String custom) {
        this.custom = custom;
    }

    @JsonProperty("item_list")
    public ItemList getItemList() {
        return item_list;
    }

    @JsonProperty("item_list")
    public void setItemList(ItemList item_list) {
        this.item_list = item_list;
    }

    public PaymentOptions getPayment_options() {
        return payment_options;
    }

    public void setPayment_options(PaymentOptions payment_options) {
        this.payment_options = payment_options;
    }

}

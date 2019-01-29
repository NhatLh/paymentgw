
package com.digitechlabs.paymentgw.paypal.request.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("custom")
    @Expose
    private String custom;
    @SerializedName("payment_options")
    @Expose
    private PaymentOptions paymentOptions;
    @SerializedName("item_list")
    @Expose
    private ItemList itemList;
    @SerializedName("related_resources")
    @Expose
    private List<Object> relatedResources = null;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public PaymentOptions getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(PaymentOptions paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

    public List<Object> getRelatedResources() {
        return relatedResources;
    }

    public void setRelatedResources(List<Object> relatedResources) {
        this.relatedResources = relatedResources;
    }

}


package com.digitechlabs.paymentgw.paypal.execute.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("payee")
    @Expose
    private Payee payee;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("custom")
    @Expose
    private String custom;
    @SerializedName("item_list")
    @Expose
    private ItemList itemList;
    @SerializedName("related_resources")
    @Expose
    private List<RelatedResource> relatedResources = null;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
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

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

    public List<RelatedResource> getRelatedResources() {
        return relatedResources;
    }

    public void setRelatedResources(List<RelatedResource> relatedResources) {
        this.relatedResources = relatedResources;
    }

}


package com.digitechlabs.paymentgw.paypal.execute.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resource {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("protection_eligibility")
    @Expose
    private String protectionEligibility;
    @SerializedName("protection_eligibility_type")
    @Expose
    private String protectionEligibilityType;
    @SerializedName("transaction_fee")
    @Expose
    private TransactionFee transactionFee;
    @SerializedName("invoice_number")
    @Expose
    private String invoiceNumber;
    @SerializedName("custom")
    @Expose
    private String custom;
    @SerializedName("parent_payment")
    @Expose
    private String parentPayment;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("update_time")
    @Expose
    private String updateTime;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getProtectionEligibility() {
        return protectionEligibility;
    }

    public void setProtectionEligibility(String protectionEligibility) {
        this.protectionEligibility = protectionEligibility;
    }

    public String getProtectionEligibilityType() {
        return protectionEligibilityType;
    }

    public void setProtectionEligibilityType(String protectionEligibilityType) {
        this.protectionEligibilityType = protectionEligibilityType;
    }

    public TransactionFee getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(TransactionFee transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getParentPayment() {
        return parentPayment;
    }

    public void setParentPayment(String parentPayment) {
        this.parentPayment = parentPayment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}

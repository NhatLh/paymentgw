package com.digitechlabs.paymentgw.paypal.refund;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefundResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("update_time")
    @Expose
    private String updateTime;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("refund_from_transaction_fee")
    @Expose
    private RefundFromTransactionFee refundFromTransactionFee;
    @SerializedName("total_refunded_amount")
    @Expose
    private TotalRefundedAmount totalRefundedAmount;
    @SerializedName("refund_from_received_amount")
    @Expose
    private RefundFromReceivedAmount refundFromReceivedAmount;
    @SerializedName("sale_id")
    @Expose
    private String saleId;
    @SerializedName("parent_payment")
    @Expose
    private String parentPayment;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public RefundFromTransactionFee getRefundFromTransactionFee() {
        return refundFromTransactionFee;
    }

    public void setRefundFromTransactionFee(RefundFromTransactionFee refundFromTransactionFee) {
        this.refundFromTransactionFee = refundFromTransactionFee;
    }

    public TotalRefundedAmount getTotalRefundedAmount() {
        return totalRefundedAmount;
    }

    public void setTotalRefundedAmount(TotalRefundedAmount totalRefundedAmount) {
        this.totalRefundedAmount = totalRefundedAmount;
    }

    public RefundFromReceivedAmount getRefundFromReceivedAmount() {
        return refundFromReceivedAmount;
    }

    public void setRefundFromReceivedAmount(RefundFromReceivedAmount refundFromReceivedAmount) {
        this.refundFromReceivedAmount = refundFromReceivedAmount;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getParentPayment() {
        return parentPayment;
    }

    public void setParentPayment(String parentPayment) {
        this.parentPayment = parentPayment;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}

package com.digitechlabs.paymentgw.coingate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutResp {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("do_not_convert")
    @Expose
    private Boolean doNotConvert;
    @SerializedName("price_currency")
    @Expose
    private String priceCurrency;
    @SerializedName("price_amount")
    @Expose
    private String priceAmount;
    @SerializedName("pay_currency")
    @Expose
    private String payCurrency;
    @SerializedName("pay_amount")
    @Expose
    private String payAmount;
    @SerializedName("lightning_network")
    @Expose
    private Boolean lightningNetwork;
    @SerializedName("receive_currency")
    @Expose
    private String receiveCurrency;
    @SerializedName("receive_amount")
    @Expose
    private String receiveAmount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("expire_at")
    @Expose
    private String expireAt;
    @SerializedName("payment_address")
    @Expose
    private String paymentAddress;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("payment_url")
    @Expose
    private String paymentUrl;

    private String destination_tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDoNotConvert() {
        return doNotConvert;
    }

    public void setDoNotConvert(Boolean doNotConvert) {
        this.doNotConvert = doNotConvert;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(String priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getPayCurrency() {
        return payCurrency;
    }

    public void setPayCurrency(String payCurrency) {
        this.payCurrency = payCurrency;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public Boolean getLightningNetwork() {
        return lightningNetwork;
    }

    public void setLightningNetwork(Boolean lightningNetwork) {
        this.lightningNetwork = lightningNetwork;
    }

    public String getReceiveCurrency() {
        return receiveCurrency;
    }

    public void setReceiveCurrency(String receiveCurrency) {
        this.receiveCurrency = receiveCurrency;
    }

    public String getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(String receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }

    public String getPaymentAddress() {
        return paymentAddress;
    }

    public void setPaymentAddress(String paymentAddress) {
        this.paymentAddress = paymentAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getDestination_tag() {
        return destination_tag;
    }

    public void setDestination_tag(String destination_tag) {
        this.destination_tag = destination_tag;
    }

}

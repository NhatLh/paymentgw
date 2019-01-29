package com.digitechlabs.paymentgw.crypter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionEventPayload {
    @JsonProperty("type")
    private String type ="wallet.update";
    @JsonProperty("wallet_address")
    private String wallet;
    @JsonProperty("amount")
    private float amount;

    public TransactionEventPayload(String type, String wallet, float amount) {
        this.type = type;
        this.wallet = wallet;
        this.amount = amount;
    }

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}

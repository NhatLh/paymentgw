/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.history;

public class HistoryResponse {

    private String status;
    private String description;
    private Meta meta;
    private History[] data;

    public HistoryResponse(String status, String description, Meta meta, History[] data) {
        this.status = status;
        this.description = description;
        this.meta = meta;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public History[] getData() {
        return data;
    }

    public void setData(History[] data) {
        this.data = data;
    }

}

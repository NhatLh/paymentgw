
package com.digitechlabs.paymentgw.paypal.execute.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExecutePaymentResponseNotApproved {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("information_link")
    @Expose
    private String informationLink;
    @SerializedName("debug_id")
    @Expose
    private String debugId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInformationLink() {
        return informationLink;
    }

    public void setInformationLink(String informationLink) {
        this.informationLink = informationLink;
    }

    public String getDebugId() {
        return debugId;
    }

    public void setDebugId(String debugId) {
        this.debugId = debugId;
    }

}

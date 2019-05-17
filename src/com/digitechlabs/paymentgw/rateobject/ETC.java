
package com.digitechlabs.paymentgw.rateobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ETC {

    @SerializedName("ETC")
    @Expose
    private String eTC;

    public String getETC() {
        return eTC;
    }

    public void setETC(String eTC) {
        this.eTC = eTC;
    }

}

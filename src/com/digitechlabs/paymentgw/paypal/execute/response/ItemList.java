
package com.digitechlabs.paymentgw.paypal.execute.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemList {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("shipping_address")
    @Expose
    private ShippingAddress_ shippingAddress;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public ShippingAddress_ getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress_ shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

}

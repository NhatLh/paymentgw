/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.paypal.items;

/**
 *
 * @author Admin
 */
public class ListItems {

    private Item[] item_list;

    public ListItems(Item[] item_list) {
        this.item_list = item_list;
    }

    public Item[] getItem_list() {
        return item_list;
    }

    public void setItem_list(Item[] item_list) {
        this.item_list = item_list;
    }

}

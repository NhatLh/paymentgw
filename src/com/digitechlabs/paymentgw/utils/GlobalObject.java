/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.utils;

import java.util.Hashtable;

/**
 *
 * @author nhatlh1
 */
public class GlobalObject {

    private static GlobalObject instance;
    private final Hashtable<String, Object> hashOrderID = new Hashtable<>();

    //key:
    private final Hashtable<String, PaypalPayment> hashPaypalExeLink = new Hashtable<>();

    public static synchronized GlobalObject getInstance() {
        if (instance == null) {
            instance = new GlobalObject();
        }

        return instance;
    }

    //PayTaskWrapper
    public Hashtable<String, Object> getHashOrder() {
        return hashOrderID;
    }

    public Hashtable<String, PaypalPayment> getHashPaypalExeLink() {
        return hashPaypalExeLink;
    }

}

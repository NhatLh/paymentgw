/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.crypter;

/**
 *
 * @author FOCUS
 */
public class IDgenerator {

    private static IDgenerator instance;

    public static IDgenerator getInstance() {
        if (instance == null) {
            instance = new IDgenerator();
        }

        return instance;
    }

    public String genID() {
        String stringID = Long.toString(System.currentTimeMillis(), 36);
        return stringID.toUpperCase();
    }
}

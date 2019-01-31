/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.main;

import com.google.gson.Gson;

public class TestJson {

    public static void main(String[] args) {
        String a = "  aa c     b  ";

        String b = a.replace(" ", "");
        
        System.out.println(b);
        
        
        Gson gson = new Gson();
//        gson.to
    }
}

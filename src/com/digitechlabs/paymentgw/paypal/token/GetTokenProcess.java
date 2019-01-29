/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.paypal.token;

import com.digitechlabs.paymentgw.ssl.RestFulClient;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ProcessThread;

/**
 *
 * @author Admin
 */
public class GetTokenProcess extends ProcessThread {

    private static GetTokenProcess instance;
    private String liveToken;
    private long expired_in = 0;
    private RestFulClient client;

    public static synchronized GetTokenProcess getInstance() {
        if (instance == null) {
            instance = new GetTokenProcess("GetTokenProcess");
        }

        return instance;
    }

    public GetTokenProcess(String threadName) {
        this.threadName = threadName;
        client = new RestFulClient();
    }

    @Override
    protected void process() {
        long start = System.currentTimeMillis();
        GetTokenResp token = getToken();

        if (token != null) {
            liveToken = token.getAccess_token();
            expired_in = token.getExpires_in() - (System.currentTimeMillis() - start);

            logger.info("[" + expired_in + "]access token:" + liveToken);
            try {
                Thread.sleep(expired_in * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GetTokenProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GetTokenProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getLiveToken() {
        return liveToken;
    }

    private GetTokenResp getToken() {
        String rsp = client.getPaypalToken();

        if (rsp != null) {
            Gson gson = new Gson();
            GetTokenResp token = gson.fromJson(rsp, GetTokenResp.class);
            return token;
        } else {
            expired_in = 0;
            return null;
        }
    }

}

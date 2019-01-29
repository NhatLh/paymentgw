/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.currency;

import com.digitechlabs.paymentgw.dbpooling.DbInterface;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import utils.ProcessThreadMX;

/**
 *
 * @author Admin
 */
public class SyncRateProcess extends ProcessThreadMX {

    private static SyncRateProcess instance;
    private Hashtable<String, Double> rateHash;

    private final DbInterface dbInt;

    public static synchronized SyncRateProcess getInstance() {
        if (instance == null) {
            instance = new SyncRateProcess("SyncRateProcess");
        }

        return instance;
    }

    public SyncRateProcess(String threadName) {
        super(threadName);
        this.rateHash = new Hashtable<>();
        dbInt = new DbInterface();
    }

    @Override
    protected void process() {
        Hashtable<String, Double> r = dbInt.syncRate();
        if (r != null) {
            rateHash = r;
        }

        try {
            Thread.sleep(3 * 60 * 100);
        } catch (InterruptedException ex) {
            Logger.getLogger(SyncRateProcess.class).error(ex.getMessage(), ex);
        }
    }

    public double getRate(String curency) {
        double rate = rateHash.get(curency);

        return rate;
    }

}

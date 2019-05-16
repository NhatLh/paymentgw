package com.digitechlabs.paymentgw.main;

import com.digitechlabs.paymentgw.Object.jobs.ReportBalanceSystem;

public class Stop {

    public static void main(String[] args) {
        Main.getInstance().stop();

        ReportBalanceSystem.getInstance().start();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.Object.jobs;

import com.digitechlabs.paymentgw.configs.ConfigLoader;
import com.digitechlabs.paymentgw.dbpooling.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ProcessThreadMX;

/**
 *
 * @author Admin
 */
public class ReportBalanceSystem extends ProcessThreadMX {

    private static ReportBalanceSystem instance;

    public static synchronized ReportBalanceSystem getInstance() {
        if (instance == null) {
            instance = new ReportBalanceSystem("ReportBalanceSystem");
        }

        return instance;
    }

    private long period;
    private String sqlSelect = "select sum(balance) as availabe, sum(blocked) as locked, sum(award) as award from user_wallet where is_active = true";
    private String sqlInsert = "insert into report_internal_wallet (available, locked, award, total,created_at ,note) values (?,?,?,?,now(),?)";

    public ReportBalanceSystem(String threadName) {
        super(threadName);
        this.period = ConfigLoader.getInstance().getReportBalanceSystemPeriod();
    }

    @Override
    protected void process() {

        report();

        try {
            Thread.sleep(period);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReportBalanceSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void report() {
        TotalBalance record = getTotal();
        save(record);
    }

    private TotalBalance getTotal() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sqlSelect);

            rs = pstm.executeQuery();

            if (rs.next()) {
                double available = rs.getDouble("availabe");
                double locked = rs.getDouble("locked");
                double award = rs.getDouble("award");
                return new TotalBalance(available, locked, award);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }

            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    private void save(TotalBalance record) {
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sqlInsert);

            pstm.setDouble(1, record.getAvailable());
            pstm.setDouble(2, record.getLocked());
            pstm.setDouble(3, record.getAward());
            pstm.setDouble(4, record.getAvailable() + record.getLocked() + record.getAward());
            pstm.setString(5, "");

            int rs = pstm.executeUpdate();

            logger.info("inserted " + rs + " row(s)");

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {

            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    class TotalBalance {

        private double available;
        private double locked;
        private double award;

        public TotalBalance(double available, double locked, double award) {
            this.available = available;
            this.locked = locked;
            this.award = award;
        }

        public double getAvailable() {
            return available;
        }

        public void setAvailable(double available) {
            this.available = available;
        }

        public double getLocked() {
            return locked;
        }

        public void setLocked(double locked) {
            this.locked = locked;
        }

        public double getAward() {
            return award;
        }

        public void setAward(double award) {
            this.award = award;
        }

    }

}

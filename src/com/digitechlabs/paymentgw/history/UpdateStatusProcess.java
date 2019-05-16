/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.history;

import com.digitechlabs.paymentgw.dbpooling.DBConnect;
import com.digitechlabs.paymentgw.utils.GlobalVariables;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.ProcessThread;

/**
 *
 * @author FOCUS
 */
public class UpdateStatusProcess extends ProcessThread {

    private static UpdateStatusProcess instance;

//    private Notify notify = new Notify();
    public static UpdateStatusProcess getInstance() {
        if (instance == null) {
            instance = new UpdateStatusProcess();
        }

        return instance;
    }

    private final String SQL_UPDATE = "update history set status = ? where status = ? and expired_time < now()";

    @Override
    protected void process() {
        updateStatus();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void updateStatus() {
        String sql = SQL_UPDATE;
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, GlobalVariables.TRANSACTION_STATUS_EXPIRED);
            pstm.setString(2, GlobalVariables.TRANSACTION_STATUS_START);

            int row = pstm.executeUpdate();

            if (row > 0) {
                logger.info("[row:" + row + "]update status to expired successfully");
            }

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

}

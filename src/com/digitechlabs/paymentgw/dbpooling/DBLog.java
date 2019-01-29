package com.digitechlabs.paymentgw.dbpooling;

import java.sql.Connection;
import utils.BlockQueue;
import utils.ProcessThreadMX;

public class DBLog extends ProcessThreadMX {

    private BlockQueue insertQueue = new BlockQueue();
    private int status = 0;
    private Connection conn;

    public DBLog(String threadName) {
        super(threadName);
        //init Connection
        conn = DBConnect.getConnection("conn");
    }

//    private boolean retry() {
//        if (conn == null) {
//            conn = DBConnect.getConnection("conn");
//
//            PreparedStatement pstm;
//            try {
//                pstm = conn.prepareStatement("select 1 from dual");
//                ResultSet rs = pstm.executeQuery();
//
//                if (rs.next()) {
//                    logger.info("retry connection ... OK");
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(DBLog.class.getName()).log(Level.SEVERE, null, ex);
//                return false;
//            }
//        } else {
//            try {
//                PreparedStatement pstm = conn.prepareStatement("select 1 from dual");
//                ResultSet rs = pstm.executeQuery();
//                if (rs.next()) {
//                    logger.info("connection is remaining..");
//                    return true;
//                } else {
//                    logger.error("Connection is down --> retry connection");
//                    conn = DBConnect.getConnection("conn");
//                    try {
//                        pstm = conn.prepareStatement("select 1 from dual");
//                        rs = pstm.executeQuery();
//                        if (rs.next()) {
//                            logger.info("connection is established");
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    } catch (SQLException ex1) {
//                        ex1.printStackTrace();
//                        return false;
//                    } finally {
//                        try {
//                            if (pstm != null) {
//                                pstm.close();
//                            }
//                        } catch (SQLException ex1) {
//                            Logger.getLogger(DBLog.class.getName()).log(Level.SEVERE, null, ex1);
//                        }
//                    }
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//                logger.error("Connection is down --> retry connection");
//                conn = DBConnect.getConnection("conn");
//                PreparedStatement pstm = null;
//                try {
//                    pstm = conn.prepareStatement("select 1 from dual");
//                    ResultSet rs = pstm.executeQuery();
//                    if (rs.next()) {
//                        logger.info("connection is established");
//                        return true;
//                    } else {
//                        return false;
//                    }
//                } catch (SQLException ex1) {
//                    ex1.printStackTrace();
//                    return false;
//                } finally {
//                    try {
//                        if (pstm != null) {
//                            pstm.close();
//                        }
//                    } catch (SQLException ex1) {
//                        Logger.getLogger(DBLog.class.getName()).log(Level.SEVERE, null, ex1);
//                    }
//                }
//            }
//        }
//    }
    @Override
    protected void process() {
//        if (status == 0) {
//            //normal status
//            Object o = insertQueue.dequeue();
//        } else {
//
//        }
    }

    public void writeLog() {

    }

}

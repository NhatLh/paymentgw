/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.dbpooling;

import com.digitechlabs.paymentgw.Object.UserWallet;
import com.digitechlabs.paymentgw.coingate.CheckoutTask;
import com.digitechlabs.paymentgw.currency.SyncRateProcess;
import com.digitechlabs.paymentgw.history.History;
import com.digitechlabs.paymentgw.history.HistoryRequest;
import com.digitechlabs.paymentgw.history.WithdrawClientNotifyTask;
import com.digitechlabs.paymentgw.orderdetails.OrderDetails;
import com.digitechlabs.paymentgw.paypal.client.request.CreatePaymentTask;
import com.digitechlabs.paymentgw.rabbitqueue.HistoryWalletInsert;
import com.digitechlabs.paymentgw.rabbitqueue.HistoryWalletUpdate;
import com.digitechlabs.paymentgw.rabbitqueue.RabbitMQHisWalletSender;
import com.digitechlabs.paymentgw.restobject.CallbackTask;
import com.digitechlabs.paymentgw.restobject.OrderTask;
import com.digitechlabs.paymentgw.restobject.PayTask;
import com.digitechlabs.paymentgw.restobject.WithdrawRequestTask;
import com.digitechlabs.paymentgw.revenue.Coingate;
import com.digitechlabs.paymentgw.revenue.Wallet;
import com.digitechlabs.paymentgw.soap.ProcessRequest;
import com.digitechlabs.paymentgw.utils.GlobalVariables;
import com.digitechlabs.paymentgw.wallet.Deposit;
import com.digitechlabs.paymentgw.wallet.Refund;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;

/**
 *
 * @author FOCUS
 */
public class DbInterface {

    Logger logger = Logger.getLogger(DbInterface.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX");
    private Gson gson = new Gson();

    public DbInterface() {
    }

    /**
     * get userid from address and currency
     *
     * @param walletAddress
     * @param currency
     * @return
     */
    public String checkExistAddress(String walletAddress, String currency) {
        String sql = GlobalVariables.SQL_CHECK_ADDRESS_EXIST;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, walletAddress);
            pstm.setString(2, currency);

            rs = pstm.executeQuery();
            String userid;
            if (rs.next()) {
                userid = rs.getString(1);
            } else {
                userid = "";
            }
            return userid;
        } catch (SQLException ex) {
            logger.error("SQL ERROR:" + ex.getMessage(), ex);
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

    public String getEmail(int userID) {
        String sql = GlobalVariables.SQL_SELECT_EMAIL;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, userID);

            rs = pstm.executeQuery();
            String email;
            if (rs.next()) {
                email = rs.getString(1);
            } else {
                email = "";
            }
            return email;
        } catch (SQLException ex) {
            logger.error("SQL ERROR:" + ex.getMessage(), ex);
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

    public UserWallet getUserWallet(int userID) {
        String sql = GlobalVariables.SQL_SELECT_USER_WALLET;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, String.valueOf(userID));

            rs = pstm.executeQuery();
            if (rs.next()) {
                String wallet_add = rs.getString("wallet_add");
                String wallet_type = rs.getString("wallet_type");
                boolean isActive = rs.getBoolean("is_active");
                float balance = rs.getFloat("balance");
                float award = rs.getFloat("award");

                UserWallet uw = new UserWallet(userID, wallet_add, wallet_type, isActive, balance, award);
                return uw;

            } else {
                return null;
            }
        } catch (SQLException ex) {
            logger.error("SQL ERROR:" + ex.getMessage(), ex);
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

    /**
     * insert log history of every transaction create order
     *
     * @param task
     */
    public void insertOrderHist(OrderTask task, String receive_amout, String pay_amount, String pay_currency, String status) {
        Connection conn = null;
        PreparedStatement pstm = null;

        if (receive_amout.trim().isEmpty()) {
            receive_amout = "-1";
        }

        if (task.getPrice_amount().trim().isEmpty()) {
            task.setPrice_amount("-1");
        }

        if (pay_amount.trim().isEmpty()) {
            pay_amount = "-1";
        }

        String sql = GlobalVariables.SQL_INSERT_ORDER_HIS;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, task.getOrder_id());
                pstm.setDouble(2, Double.valueOf(task.getPrice_amount()));
                pstm.setString(3, task.getPrice_currency());
                pstm.setString(4, task.getTitle());
                pstm.setString(5, task.getDescription());
                pstm.setTimestamp(6, new Timestamp(sdf.parse(task.getCreate_at()).getTime()));
                pstm.setString(7, task.getToken());
                pstm.setString(8, task.getReceive_currency());
                pstm.setDouble(9, Double.valueOf(receive_amout));
                pstm.setDouble(10, Double.valueOf(pay_amount));
                pstm.setString(11, pay_currency);
                pstm.setString(12, status);
                pstm.setObject(13, UUID.randomUUID().toString(), Types.OTHER);
                pstm.setBoolean(14, true);
                pstm.setString(15, task.getUser_id());
                pstm.setString(16, null);
                pstm.setString(17, GlobalVariables.PAY_COINGATE);

                int result = pstm.executeUpdate();
            } else {
                logger.error("[" + task.getOrder_id() + "]Lost DB connection --> Please Checks");
            }
        } catch (SQLException | ParseException ex) {
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

    public void insertOrderHist(CheckoutTask task, String receive_amout, String pay_amount, String pay_currency, String status) {
        Connection conn = null;
        PreparedStatement pstm = null;

        if (receive_amout.trim().isEmpty()) {
            receive_amout = "-1";
        }

        if (task.getPrice_amount().trim().isEmpty()) {
            task.setPrice_amount("-1");
        }

        if (pay_amount.trim().isEmpty()) {
            pay_amount = "-1";
        }

        String sql = GlobalVariables.SQL_INSERT_ORDER_HIS;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, task.getOrder_id());
                pstm.setDouble(2, Double.valueOf(task.getPrice_amount()));
                pstm.setString(3, task.getPrice_currency());
                pstm.setString(4, task.getTitle());
                pstm.setString(5, task.getDescription());
                pstm.setTimestamp(6, new Timestamp(sdf.parse(task.getCreate_at()).getTime()));
                pstm.setString(7, task.getToken());
                pstm.setString(8, task.getReceive_currency());
                pstm.setDouble(9, Double.valueOf(receive_amout));
                pstm.setDouble(10, Double.valueOf(pay_amount));
                pstm.setString(11, pay_currency);
                pstm.setString(12, status);
                pstm.setObject(13, UUID.randomUUID().toString(), Types.OTHER);
                pstm.setBoolean(14, true);
                pstm.setString(15, task.getUser_id());
                pstm.setString(16, null);
                pstm.setString(17, GlobalVariables.PAY_COINGATE);

                int result = pstm.executeUpdate();
            } else {
                logger.error("[" + task.getOrder_id() + "]Lost DB connection --> Please Checks");
            }
        } catch (SQLException | ParseException ex) {
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

    public void insertOrderHist(PayTask task, String status, String jsonRevenue) {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstm = null;

        if (task.getPay_amount().trim().isEmpty()) {
            task.setPay_amount("-1");
        }

        String sql = GlobalVariables.SQL_INSERT_ORDER_HIS;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, task.getOrder_id());
                pstm.setDouble(2, Double.valueOf(task.getPay_amount()));
                pstm.setString(3, task.getPay_currency());
                pstm.setString(4, "BOOK BY AVA");
                pstm.setString(5, "BOOK ROOM AND PAY BY AVA");
                pstm.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                pstm.setString(7, "");
                pstm.setString(8, task.getPay_currency());
                pstm.setDouble(9, Double.valueOf(task.getPay_amount()));
                pstm.setDouble(10, Double.valueOf(task.getPay_amount()));
                pstm.setString(11, task.getPay_currency());
                pstm.setString(12, status);
                pstm.setObject(13, UUID.randomUUID().toString(), Types.OTHER);
                pstm.setBoolean(14, true);
                pstm.setString(15, task.getUser_id());
                pstm.setObject(16, jsonRevenue);
                pstm.setString(17, GlobalVariables.PAY_WALLET);

                int result = pstm.executeUpdate();
            } else {
                logger.error("[" + task.getOrder_id() + "]Lost DB connection --> Please Checks");
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

        logger.info("Finish Insert transaction_his in " + (System.currentTimeMillis() - start) + " ms");
    }

    public void insertHistory(String id, String status, double amount,
            Timestamp created_at, String from_address, String to_address,
            String transaction_type, String user_id, Timestamp expired_time, String currency, String order_id, String transaction_id) {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstm = null;

        String sql = GlobalVariables.INSERT_HISTORY;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, id);
                pstm.setString(2, status);
                pstm.setDouble(3, amount);
                pstm.setTimestamp(4, created_at);
                pstm.setString(5, from_address);
                pstm.setString(6, to_address);
                pstm.setString(7, transaction_type);
                pstm.setString(8, user_id);
                pstm.setTimestamp(9, expired_time);
                pstm.setString(10, currency);
                pstm.setString(11, order_id);
                pstm.setString(12, transaction_id);

                int result = pstm.executeUpdate();
            } else {
                logger.error("[" + id + "]Lost DB connection --> Please Checks");
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

        //send message to queue
        HistoryWalletInsert his = createHisWalletObject(id, status, amount, created_at, from_address, to_address, transaction_type, user_id, expired_time, currency, order_id, transaction_id);
        logger.info("[" + id + "]Finish Insert history in " + (System.currentTimeMillis() - start) + " ms");

        HistoryRequest request = new HistoryRequest("insert", his, null);

        String message = gson.toJson(request);
        RabbitMQHisWalletSender.getInstance().enqueue(message);
    }

    public HistoryWalletInsert createHisWalletObject(String id, String status, double amount,
            Timestamp created_at, String from_address, String to_address,
            String transaction_type, String user_id, Timestamp expired_time, String currency, String order_id, String transaction_id) {

        HistoryWalletInsert hw = new HistoryWalletInsert();

        hw.setId(id);
        hw.setStatus(status);
        hw.setAmount(amount);
        hw.setCreated_at(created_at);
        hw.setFrom_address(from_address);
        hw.setTo_address(to_address);
        hw.setTransaction_type(transaction_type);
        hw.setUser_id(user_id);
        hw.setExpired_time(expired_time);
        hw.setCurrency(currency);
        hw.setOrder_id(order_id);
        hw.setTransaction_id(transaction_id);

        return hw;
    }

    public double[] checkLimit(WithdrawRequestTask task) {
        double limit = 0;
        double amount = 0;

        try {
            limit = Double.valueOf(task.getMax_per_day());
            amount = Double.valueOf(task.getAmount());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new double[]{2, -1}; //invalid limit input
        }

        String sql = GlobalVariables.SQL_CHECK_WITHDRAW;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, task.getUser_id());
            pstm.setString(2, task.getCurrency());

            rs = pstm.executeQuery();
            double sum;
            if (rs.next()) {
                sum = rs.getDouble(1);
            } else {
                return new double[]{1, -1};
            }
            if (sum + amount <= limit) {
                return new double[]{0, sum}; //sum less than limit
            } else {
                return new double[]{1, sum}; //sum more than limit
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("check sum withdraw:" + task.getUser_id() + ", with currency:" + task.getCurrency() + " FAIL --> Please Check");
            return new double[]{-1, -1}; //system error
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

    public double getBalance(String userID, String currency) {

        String sql = GlobalVariables.SQL_GET_BALANCE;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, userID);
            pstm.setString(2, currency);

            rs = pstm.executeQuery();
            double balance;
            if (rs.next()) {
                balance = rs.getDouble(1);

                return balance;
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("get balance:" + userID + ", with currency:" + currency + " FAIL --> Please Check");
            return -2; //system error
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

    public String getHisTransID(String requestID, String type) {

        String sql = GlobalVariables.SQL_GET_HIS_TRANSID;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, requestID);
            pstm.setString(2, type);

            rs = pstm.executeQuery();
            String transactionID;
            if (rs.next()) {
                transactionID = rs.getString(1);
                return transactionID;
            } else {
                return "";
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("get his transID:" + requestID + ", with type:" + type + " FAIL --> Please Check");
            return null; //system error
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

    public String getIDfromHis(String order_id, String type) {

        String sql = GlobalVariables.SQL_GET_ID_FROM_HIS;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, order_id);
            pstm.setString(2, type);

            rs = pstm.executeQuery();
            String id;
            if (rs.next()) {
                id = rs.getString(1);
                return id;
            } else {
                return "";
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("get his transID:" + order_id + ", with type:" + type + " FAIL --> Please Check");
            return null; //system error
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

    public String getStatusFromHis(String id, String type) {

        String sql = GlobalVariables.SQL_GET_STATUS_FROM_HIS;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, id);
            pstm.setString(2, type);

            rs = pstm.executeQuery();
            String status;
            if (rs.next()) {
                status = rs.getString(1);
                return status;
            } else {
                return "";
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("get his transID:" + id + ", with type:" + type + " FAIL --> Please Check");
            return null; //system error
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

    private String genList(String[] array) {
        String output = "";
        for (String element : array) {
            if (!element.equals(array[array.length - 1])) {
                output = output + "'" + element + "',";
            } else {
                output = output + "'" + element + "'";
            }
        }

        logger.info("GenList:" + output);

        return output;
    }

    public List<OrderDetails> getOrderDetails(String[] arrOrder) {
        long start = System.currentTimeMillis();

        String sql = GlobalVariables.SQL_SELECT_ORDER_DETAIL_TYPE1;
        sql = sql.replace("#LIST#", genList(arrOrder));

        logger.info("SQL:" + sql);
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        List<OrderDetails> listOrder = new ArrayList<>();
        try {
            conn = DBConnect.getConnection("conn");

            //create array
            pstm = conn.prepareStatement(sql);

//            Array arr = pstm.getConnection().createArrayOf("text", arrOrder);
//            Array arr = createArr(arrOrder);
//            pstm.setArray(1, arr);
//            pstm.setInt(1, limit);
//            pstm.setInt(2, offset);
            rs = pstm.executeQuery();

            while (rs.next()) {
                String order_id = rs.getString("order_id");
                String pay_currency = rs.getString("pay_currency");
                double pay_amount = rs.getDouble("pay_amount");
                OrderDetails order = new OrderDetails(order_id, pay_currency, pay_amount);

                listOrder.add(order);
            }
            logger.info("Finish select Order detail in " + (System.currentTimeMillis() - start) + " ms");

            return listOrder;

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            return null; //system error
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

    /**
     * return status and sum withdrawed
     *
     * @param task
     * @return
     */
    public double[] checkLimit(WithdrawClientNotifyTask task) {
        double limit = 0;
        double amount = 0;

        try {
            limit = Double.valueOf(task.getMax_per_day());
            amount = Double.valueOf(task.getAmount());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new double[]{2, -1}; //invalid limit input
        }

        String sql = GlobalVariables.SQL_CHECK_WITHDRAW;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, task.getUser_id());
            pstm.setString(2, task.getCurrency());

            rs = pstm.executeQuery();
            double sum;
            if (rs.next()) {
                sum = rs.getDouble(1);
            } else {
                return new double[]{1, -1};
            }
            if (sum + amount <= limit) {
                return new double[]{0, sum}; //sum less than limit
            } else {
                return new double[]{1, sum}; //sum more than limit
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("check sum withdraw:" + task.getUser_id() + ", with currency:" + task.getCurrency() + " FAIL --> Please Check");
            return new double[]{-1, -1}; //system error
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

    /**
     * update balance for address
     *
     * @param currency type of currency
     * @param amount amount deposit
     * @param address address deposit
     * @return 0 - success. 1 - system error
     */
    public int updateBalance(String currency, double amount, String address) {
        String sql = GlobalVariables.SQL_UPDATE_BALANCE;
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setDouble(1, amount);
            pstm.setString(2, address);
            pstm.setString(3, currency.toUpperCase());

            int row = pstm.executeUpdate();

            if (row == 1) {
                logger.info("update address:" + address + ", with currency:" + currency + " , amount:" + amount + " success");
            } else {
                logger.info("[row:" + row + "]update address:" + address + ", with currency:" + currency + " , amount:" + amount + " success");
            }

            return 0;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("update address:" + address + ", with currency:" + currency + " , amount:" + amount + " FAIL --> Please Check");
            return 1;
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

    public int updateStatusHistory(String id, String status, String transType) {
        String sql = GlobalVariables.UPDATE_STATUS_HISTORY;
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, status);
            pstm.setString(2, id);
            pstm.setString(3, transType);

            int row = pstm.executeUpdate();

            if (row == 1) {
                logger.info("update id:" + id + ", to status:" + status + " success");
            } else {
                logger.info("[row:" + row + "]update id:" + id + ", to status:" + status + " fail");
            }

            HistoryWalletUpdate his = new HistoryWalletUpdate(id, status, transType);
            HistoryRequest request = new HistoryRequest("update", null, his);

            String message = gson.toJson(request);
            RabbitMQHisWalletSender.getInstance().enqueue(message);

            return 0;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("update id:" + id + ", to status:" + status + " FAIL --> Please Check");
            return 1;
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

    public int updateHisUserDeleted(String userID, String sqlInput) {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sqlInput);
            pstm.setString(1, userID);

            int row = pstm.executeUpdate();

            if (row == 1) {
                logger.info("update is_deleted:" + userID + " success");
            } else {
                logger.info("[row:" + row + "]update address:" + userID + " success");
            }

            return 0;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("update address:" + userID + " FAIL --> Please Check");
            return 1;
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

    /**
     * update balance for userID
     *
     * @param user_id
     * @param currency
     * @param amount amount deposit
     * @return 0 - success. 1 - system error
     */
    public int updateBalance(String user_id, String currency, double amount) {
        String sql = GlobalVariables.SQL_UPDATE_BALANCE_BY_USER;
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(2, user_id);
            pstm.setDouble(1, amount);
            pstm.setString(3, currency.toUpperCase());

            int row = pstm.executeUpdate();

            if (row == 0) { //no row effected
                logger.info("user id:" + user_id + " not found");
                return -1;
            }

            if (row == 1) { //one row effected
                logger.info("update userid:" + user_id + ", with currency:" + currency + " , amount:" + amount + " success");
                return 0;
            } else { //multi row effected
                logger.info("[row:" + row + "]update userid:" + user_id + ", with currency:" + currency + " , amount:" + amount + " success");
                return 0;
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("update userid:" + user_id + ", with currency:" + currency + " , amount:" + amount + " FAIL --> Please Check");
            return 1;
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

    public void insertOrderHist(CallbackTask task, String user_id, String jsonRevenue) {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstm = null;

        String sql = GlobalVariables.SQL_INSERT_ORDER_HIS;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, task.getOrder_id());
                pstm.setDouble(2, Double.valueOf(task.getPrice_amount()));
                pstm.setString(3, task.getPrice_currency());
                pstm.setString(4, "callbackID:" + task.getId());
                pstm.setString(5, "call back from coingate");
                pstm.setTimestamp(6, new Timestamp(sdf.parse(task.getCreated_at()).getTime()));
                pstm.setString(7, task.getToken());
                pstm.setString(8, task.getReceive_currency());
                pstm.setDouble(9, Double.valueOf(task.getReceive_amount()));
                pstm.setDouble(10, Double.valueOf(task.getPay_amount()));
                pstm.setString(11, task.getPay_currency());
                pstm.setString(12, task.getStatus());
                pstm.setObject(13, UUID.randomUUID().toString(), Types.OTHER);
                pstm.setBoolean(14, true);
                pstm.setString(15, user_id);
                pstm.setObject(16, jsonRevenue);
                pstm.setString(17, GlobalVariables.PAY_COINGATE);

                pstm.executeUpdate();

                logger.info("[" + task.getOrder_id() + "]Insert his success for order: " + task.debugString());

            } else {
                logger.error("[" + task.getOrder_id() + "]Lost DB connection --> Please Checks");
            }
        } catch (SQLException | ParseException ex) {
            logger.error("[" + task.getOrder_id() + "]" + ex.getMessage(), ex);
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

        logger.info("[" + task.getOrder_id() + "]Finish insert db in " + (System.currentTimeMillis() - start) + " ms");
    }

    public void insertOrderHist(CreatePaymentTask task, String status, String revenue) {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstm = null;

        String sql = GlobalVariables.SQL_INSERT_ORDER_HIS;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, task.getOrder_id());
                pstm.setDouble(2, task.getTransactions().get(0).getAmount().getTotal());
                pstm.setString(3, task.getTransactions().get(0).getAmount().getCurrency());
                pstm.setString(4, "Book by paypal:" + task.getOrder_id());
                pstm.setString(5, task.getTransactions().get(0).getDescription());
                pstm.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                pstm.setString(7, "");
                pstm.setString(8, task.getTransactions().get(0).getAmount().getCurrency());
                pstm.setDouble(9, task.getTransactions().get(0).getAmount().getTotal());
                pstm.setDouble(10, task.getTransactions().get(0).getAmount().getTotal());
                pstm.setString(11, task.getTransactions().get(0).getAmount().getCurrency());
                pstm.setString(12, status);
                pstm.setObject(13, UUID.randomUUID().toString(), Types.OTHER);
                pstm.setBoolean(14, true);
                pstm.setString(15, task.getUser_id());
                pstm.setObject(16, revenue);
                pstm.setString(17, GlobalVariables.PAY_PAYPAL);

                pstm.executeUpdate();

                logger.info("[" + task.getOrder_id() + "]Insert his success for order: " + task.getOrder_id());

            } else {
                logger.error("[" + task.getOrder_id() + "]Lost DB connection --> Please Checks");
            }
        } catch (SQLException ex) {
            logger.error("[" + task.getOrder_id() + "]" + ex.getMessage(), ex);
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

        logger.info("[" + task.getOrder_id() + "]Finish insert db in " + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * get blockchain address from userID and currency
     *
     * @param userID
     * @param currency
     * @return blockchain wallet address. null if not found. ERROR string if
     * query error
     */
    public String getAddressfromUserCurrency(String userID, String currency) {
        String sql = GlobalVariables.SQL_SELECT_ADDRESS_FROM_USERID_CURRENCY;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");

            pstm = conn.prepareStatement(sql);
            pstm.setString(1, userID);
            pstm.setString(2, currency.toUpperCase());

            rs = pstm.executeQuery();

            if (rs.next()) {
                String address = rs.getString("wallet_add");

                return address;
            } else {
                //return account not found
                logger.warn("[" + userID + "] address with currency:" + currency + " not found");
                return "";
            }
        } catch (SQLException ex) {
            logger.error("SQL ERROR:" + ex.getMessage(), ex);
            return "ERROR";
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.error("SQL ERROR:" + ex.getMessage(), ex);
                }
            }

            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException ex) {
                    logger.error("SQL ERROR:" + ex.getMessage(), ex);
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    logger.error("SQL ERROR:" + ex.getMessage(), ex);
                }
            }
        }
    }

    public void insertDepositHis(Deposit task, String userID, String from_address) {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstm = null;
//type, event, currency, txid,timestamp,address, amount
        String sql = GlobalVariables.SQL_INSERT_DEPOSIT_HIS;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, task.getType());
                pstm.setString(2, task.getEvent());
                pstm.setString(3, task.getData().getCurrency().toUpperCase());
                pstm.setString(4, task.getData().getTxid());
                pstm.setString(5, task.getData().getTimestamp());
                pstm.setString(6, task.getData().getAddress());
                pstm.setDouble(7, Double.valueOf(task.getData().getAmount()));
                pstm.setString(8, userID);
                pstm.setString(9, from_address);

                pstm.executeUpdate();

                logger.info("[" + task.getData().getTxid() + "]Insert his success ");

            } else {
                logger.error("[" + task.getData().getTxid() + "]Lost DB connection --> Please Checks");
            }
        } catch (SQLException ex) {
            logger.error("[" + task.getData().getTxid() + "]" + ex.getMessage(), ex);
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

        logger.info("[" + task.getData().getTxid() + "]Finish insert db in " + (System.currentTimeMillis() - start) + " ms");
    }

    public void insertDepositHis(String currency, String to_address, double amount, String userid, String from_address) {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstm = null;
//type, event, currency, txid,timestamp,address, amount
        String sql = GlobalVariables.SQL_INSERT_DEPOSIT_HIS;

        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, "deposit");
                pstm.setString(2, "completed");
                pstm.setString(3, currency.toUpperCase());
                pstm.setString(4, "");
                pstm.setString(5, "" + System.currentTimeMillis());
                pstm.setString(6, to_address);
                pstm.setDouble(7, amount);
                pstm.setString(8, userid);
                pstm.setString(9, from_address);

                pstm.executeUpdate();

                logger.info("[" + userid + "]Insert his success ");

            } else {
                logger.error("[" + userid + "]Lost DB connection --> Please Checks");
            }
        } catch (SQLException ex) {
            logger.error("[" + userid + "]" + ex.getMessage(), ex);
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

        logger.info("[" + userid + "]Finish insert db in " + (System.currentTimeMillis() - start) + " ms");
    }

    public void insertWithdrawHis(String userid, String curency, String amount, String to_address, String max_day, String event, long transaction_id, String request_id) {
        long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstm = null;

        String sql = GlobalVariables.SQL_INSERT_WITHDRAW_HIS;
//user_id, currency, amount, to_address, max_per_day, created_at
        try {
            conn = DBConnect.getConnection("conn");
            if (conn != null) {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, userid);
                pstm.setString(2, curency.toUpperCase());
                pstm.setDouble(3, Double.valueOf(amount));
                pstm.setString(4, to_address);
                pstm.setDouble(5, Double.valueOf(max_day));
                pstm.setString(6, event);
                pstm.setInt(7, 0);
                pstm.setLong(8, transaction_id);
                pstm.setString(9, request_id);

                pstm.executeUpdate();

                logger.info("[" + to_address + "]Insert his success ");

            } else {
                logger.error("[" + to_address + "]Lost DB connection --> Please Checks");
            }
        } catch (SQLException ex) {
            logger.error("[" + to_address + "]" + ex.getMessage(), ex);
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

        logger.info("[" + to_address + "]Finish insert db in " + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * check address available status
     *
     * @param wallet_add address need to check
     * @param wallet_type type of address
     * @return 0 - address and type is validated. 1 - address not found. 2 -
     * found too many address. 3 - Query error.
     */
    public String checkAddress(String wallet_add, String wallet_type) {
        String sql = GlobalVariables.SQL_SELECT_ADDRESS;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String user_id;

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, wallet_add);
            pstm.setString(2, wallet_type.toUpperCase());

            rs = pstm.executeQuery();

            if (rs.next()) {
                //address found --> next one time more
                user_id = rs.getString(1);
                if (rs.next()) {
                    //found address too many time --> return 2.
                    logger.warn("Address:" + wallet_add + ", with type:" + wallet_type + " is found too many time --> please Check!");
                    return "WARNING";
                } else {
                    //address is validated --> accept deposit
                    logger.info("Address:" + wallet_add + ", with type:" + wallet_type + " is valid --> do Deposit!");
                    return user_id;
                }

            } else {
                //address with type input not found
                logger.info("Address:" + wallet_add + ", with type:" + wallet_type + " not found --> ignore this transaction");
                return "IGNORE";
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            return "ERROR";
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

    public String getRequestID(String transactionID) {
        String sql = GlobalVariables.SELECT_GET_REQUEST_ID_FROM_TRANSACTION_ID;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String request_id;

        try {
            long transID = Long.parseLong(transactionID);
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, transID);

            rs = pstm.executeQuery();

            if (rs.next()) {
                //address found --> next one time more
                request_id = rs.getString(1);

                return request_id;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            return "ERROR";
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

    public Refund getRefund(long transaction_id) {
        String sql = GlobalVariables.SELECT_INFO_REFUND;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, transaction_id);

            rs = pstm.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("user_id");
                double amount = rs.getDouble("amount");

                Refund ref = new Refund(userId, amount);
                return ref;
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

    /**
     * check transaction exist in withdraw request history. If transaction exist
     * and result success --> insert DB to log his. If transaction exist and
     * result fail --> refund user_id that make withdraw request
     *
     * @param transaction_id
     * @return 0 - address and type is validated. 1 - address not found. 2 -
     * found too many address. 3 - Query error.
     */
    public int checkTransactionExist(long transaction_id) {
        String sql = GlobalVariables.SQL_SELECT_TRANS_ID;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, transaction_id);

            rs = pstm.executeQuery();

            if (rs.next()) {
                //address found --> next one time more

                if (rs.next()) {
                    //found address too many time --> return 2.
                    logger.warn("transID:" + transaction_id + " is found too many time --> please Check!");
                    return 2;
                } else {
                    //address is validated --> accept deposit
                    logger.info("transID:" + transaction_id + " is valid !");
                    return 0;
                }

            } else {
                //address with type input not found
                logger.info("transID:" + transaction_id + " not found --> ignore this transaction");
                return 1;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            return 3;
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

    /**
     * SELECT order_id, status, amount, created_at, to_address FROM
     * transaction_history
     *
     * @param userID
     * @param from
     * @param to
     * @return
     */
    public List<History> getHisPay(String userID, Date from, Date to) {
        if (from == null) {
            from = new Date(0);
        }

        if (to == null) {
            to = new Date(System.currentTimeMillis());
        }

        String sql = GlobalVariables.SQL_SELECT_HIS_BY_TYPE;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<History> his = new ArrayList<>();

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, userID);
            pstm.setTimestamp(2, new Timestamp(from.getTime()));
            pstm.setTimestamp(3, new Timestamp(to.getTime() + GlobalVariables.MILIS_DAY));
            pstm.setString(4, "PAY");

            rs = pstm.executeQuery();
            while (rs.next()) {

                String orderID = rs.getString("id");
                String status = rs.getString("status");
                String amount = rs.getString("amount");
                String created_at = rs.getString("created_at");
                String to_address = rs.getString("to_address");
                String from_address = rs.getString("from_address");
                String currency = rs.getString("currency");
                String trans_type = rs.getString("transaction_type");
                String order_id = rs.getString("order_id");
                String transaction_id = rs.getString("transaction_id");

                History h = new History(orderID, status, amount, created_at, from_address, to_address, trans_type, currency, order_id, transaction_id);
                his.add(h);
            }
            return his;
        } catch (Exception ex) {
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

    public List<Object> getRevenue(Date from, Date to) {
        if (from == null) {
            from = new Date(0);
        }

        if (to == null) {
            to = new Date(System.currentTimeMillis());
        }

        String sql = GlobalVariables.SQL_SELECT_REVENUE;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Object> revenue = new ArrayList<>();

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setTimestamp(1, new Timestamp(from.getTime()));
            pstm.setTimestamp(2, new Timestamp(to.getTime() + GlobalVariables.MILIS_DAY));

            rs = pstm.executeQuery();
            while (rs.next()) {
                String type = rs.getString("pay_type");
                double usd = rs.getDouble("USD");
                double eur = rs.getDouble("EUR");
                double ava = rs.getDouble("AVA");

                if (type.equalsIgnoreCase(GlobalVariables.PAY_COINGATE)) {
                    Coingate cg = new Coingate(usd, eur, ava);
                    revenue.add(cg);
                } else if (type.equalsIgnoreCase(GlobalVariables.PAY_WALLET)) {
                    Wallet wl = new Wallet(usd, eur, ava);
                    revenue.add(wl);
                }
            }

            return revenue;
        } catch (Exception ex) {
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

    /**
     * SELECT txid, event, amount, created_at, address
     *
     * @param userID
     * @param from
     * @param to
     * @return
     */
    public List<History> getHisDeposit(String userID, Date from, Date to) {
        if (from == null) {
            from = new Date(0);
        }

        if (to == null) {
            to = new Date(System.currentTimeMillis());
        }

        String sql = GlobalVariables.SQL_SELECT_HIS_BY_TYPE;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<History> his = new ArrayList<>();

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, userID);
            pstm.setTimestamp(2, new Timestamp(from.getTime()));
            pstm.setTimestamp(3, new Timestamp(to.getTime() + GlobalVariables.MILIS_DAY));
            pstm.setString(4, "DEPOSIT");

            rs = pstm.executeQuery();
            while (rs.next()) {

                String orderID = rs.getString("id");
                String status = rs.getString("status");
                String amount = rs.getString("amount");
                String created_at = rs.getString("created_at");
                String to_address = rs.getString("to_address");
                String from_address = rs.getString("from_address");
                String currency = rs.getString("currency");
                String trans_type = rs.getString("transaction_type");
                String order_id = rs.getString("order_id");
                String transaction_id = rs.getString("transaction_id");

                History h = new History(orderID, status, amount, created_at, from_address, to_address, trans_type, currency, order_id, transaction_id);
                his.add(h);
            }
            return his;
        } catch (Exception ex) {
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

    public List<History> getHistory(String userID, Date from, Date to) {
        if (from == null) {
            from = new Date(0);
        }

        if (to == null) {
            to = new Date(System.currentTimeMillis());
        }

        String sql = GlobalVariables.SQL_SELECT_HIS;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<History> his = new ArrayList<>();

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, userID);
            pstm.setTimestamp(2, new Timestamp(from.getTime()));
            pstm.setTimestamp(3, new Timestamp(to.getTime() + GlobalVariables.MILIS_DAY));
            pstm.setString(4, "DEPOSIT");
            pstm.setString(5, "WITHDRAW");
            pstm.setString(6, "PAY");
            pstm.setString(7, "REFUND");

            rs = pstm.executeQuery();
            while (rs.next()) {

                String orderID = rs.getString("id");
                String status = rs.getString("status");
                String amount = rs.getString("amount");
                String created_at = rs.getString("created_at");
                String to_address = rs.getString("to_address");
                String from_address = rs.getString("from_address");
                String currency = rs.getString("currency");
                String trans_type = rs.getString("transaction_type");
                String order_id = rs.getString("order_id");
                String transaction_id = rs.getString("transaction_id");

                History h = new History(orderID, status, amount, created_at, from_address, to_address, trans_type, currency, order_id, transaction_id);
                his.add(h);
            }
            return his;
        } catch (Exception ex) {
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

    /**
     * SELECT tx, event, amount, created_at, to_address FROM withdraw_his WHERE
     * user_id =?
     *
     * @param userID
     * @param from
     * @param to
     * @return
     */
    public List<History> getHisWithDraw(String userID, Date from, Date to) {

        if (from == null) {
            from = new Date(0);
        }

        if (to == null) {
            to = new Date(System.currentTimeMillis());
        }

        String sql = GlobalVariables.SQL_SELECT_HIS_BY_TYPE;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<History> his = new ArrayList<>();

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, userID);
            pstm.setTimestamp(2, new Timestamp(from.getTime()));
            pstm.setTimestamp(3, new Timestamp(to.getTime() + GlobalVariables.MILIS_DAY));
            pstm.setString(4, "WITHDRAW");

            rs = pstm.executeQuery();
            while (rs.next()) {

                String orderID = rs.getString("id");
                String status = rs.getString("status");
                String amount = rs.getString("amount");
                String created_at = rs.getString("created_at");
                String to_address = rs.getString("to_address");
                String from_address = rs.getString("from_address");
                String currency = rs.getString("currency");
                String trans_type = rs.getString("transaction_type");
                String order_id = rs.getString("order_id");
                String transaction_id = rs.getString("transaction_id");

                History h = new History(orderID, status, amount, created_at, from_address, to_address, trans_type, currency, order_id, transaction_id);
                his.add(h);
            }
            return his;
        } catch (Exception ex) {
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

    public int checkTxExist(String tx, String address) {
        String sql;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection("conn");
            if (address != null) {
                sql = GlobalVariables.SQL_CHECK_TX_DEPOSIT;
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, tx);
                pstm.setString(2, address);
            } else {
                sql = GlobalVariables.SQL_CHECK_TX_WITHDRAW;
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, tx);
            }

            rs = pstm.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);

                return count;
            } else {
                return 0;
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            return -1;
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

    public Hashtable<String, Double> syncRate() {
        Hashtable<String, Double> hRate = new Hashtable<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection("conn");
            pstm = conn.prepareStatement(GlobalVariables.SQL_SYNC_RATE);

            rs = pstm.executeQuery();

            while (rs.next()) {
                String key = rs.getString("key");
                double rate = rs.getDouble("rate");

                logger.debug("get rate:" + key + " --> " + rate);
                hRate.put(key, rate);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

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
        return hRate;
    }

}

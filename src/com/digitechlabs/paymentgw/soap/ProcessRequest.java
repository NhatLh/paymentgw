package com.digitechlabs.paymentgw.soap;

import com.digitechlabs.paymentgw.Object.UserWallet;
import com.digitechlabs.paymentgw.coingate.CheckoutResp;
import com.digitechlabs.paymentgw.coingate.CheckoutTask;
import com.digitechlabs.paymentgw.ssl.RestFulClient;
import com.google.gson.Gson;
import com.digitechlabs.paymentgw.restobject.CallbackTask;
import com.digitechlabs.paymentgw.configs.ConfigLoader;
import com.digitechlabs.paymentgw.main.Main;
import com.digitechlabs.paymentgw.configs.MyLog;
import com.digitechlabs.paymentgw.crypter.Decrypter;
import com.digitechlabs.paymentgw.crypter.IDgenerator;
import com.digitechlabs.paymentgw.currency.SyncRateProcess;
import com.digitechlabs.paymentgw.restobject.OrderResponse;
import com.digitechlabs.paymentgw.restobject.OrderTask;
import com.digitechlabs.paymentgw.restobject.PayTask;
import com.digitechlabs.paymentgw.dbpooling.DBConnect;
import com.digitechlabs.paymentgw.dbpooling.DbInterface;
import com.digitechlabs.paymentgw.funding.FundingTask;
//import com.digitechlabs.paymentgw.funding.Response;
import com.digitechlabs.paymentgw.history.History;
import com.digitechlabs.paymentgw.history.HistoryResponse;
import com.digitechlabs.paymentgw.history.Meta;
import com.digitechlabs.paymentgw.history.Pageable;
import com.digitechlabs.paymentgw.history.WithdrawClientNotifyTask;
import com.digitechlabs.paymentgw.orderdetails.OrderDetails;
import com.digitechlabs.paymentgw.orderdetails.Response;
import com.digitechlabs.paymentgw.paypal.NotifyBookFail;
import com.digitechlabs.paymentgw.paypal.callback.PaypalCallbackTask;
import com.digitechlabs.paymentgw.paypal.client.request.CreatePaymentTask;
import com.digitechlabs.paymentgw.paypal.client.request.ExecutePaymentTask;
import com.digitechlabs.paymentgw.paypal.client.request.response.PaymentClientResponse;
import com.digitechlabs.paymentgw.paypal.client.request.response.Data;
import com.digitechlabs.paymentgw.paypal.execute.response.ExeResponse;
import com.digitechlabs.paymentgw.paypal.execute.response.ExecutePaymentResponse;
import com.digitechlabs.paymentgw.paypal.refund.RefundResponse;
import com.digitechlabs.paymentgw.paypal.request.ExecutePayment;
import com.digitechlabs.paymentgw.paypal.request.response.CreatePaymentResponse;
import com.digitechlabs.paymentgw.paypal.request.response.Link;
import com.digitechlabs.paymentgw.rabbitqueue.HistoryWalletInsert;
import com.digitechlabs.paymentgw.rabbitqueue.RabbitMQHisWalletSender;
import com.digitechlabs.paymentgw.restobject.BookResponseTask;
import com.digitechlabs.paymentgw.restobject.PayTaskWrapper;
import com.digitechlabs.paymentgw.restobject.WithdrawRequestTask;
import com.digitechlabs.paymentgw.revenue.Coingate;
import com.digitechlabs.paymentgw.revenue.Detail;
import com.digitechlabs.paymentgw.revenue.Revenue;
import com.digitechlabs.paymentgw.revenue.RevenueResponse;
import com.digitechlabs.paymentgw.revenue.Wallet;
import com.digitechlabs.paymentgw.ssl.WithdrawResp;
import com.digitechlabs.paymentgw.utils.GlobalObject;
import com.digitechlabs.paymentgw.utils.GlobalVariables;
import com.digitechlabs.paymentgw.utils.PaypalPayment;
import com.digitechlabs.paymentgw.wallet.Deposit;
import com.digitechlabs.paymentgw.wallet.NEP5Transfer;
import com.digitechlabs.paymentgw.wallet.Refund;
import com.digitechlabs.paymentgw.wallet.WithDraw;
import com.digitechlabs.paymentgw.websocket.Notify;
import com.paypal.api.payments.Event;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
//import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class ProcessRequest implements Runnable {

    protected RequestQueue mQueue;
    protected boolean isRunning;
    protected int mId;
    private final Main mMain;

    public static final int API_TYPE_ORDER = 1;
    public static final int API_TYPE_CALLBACK = 2;
    public static final int API_TYPE_PAY = 3;
    public static final int API_TYPE_DEPOSIT = 4;
    public static final int API_TYPE_WITHDRAW = 5;
    public static final int API_TYPE_WITHDRAW_REQUEST = 6;
    public static final int API_TYPE_WITHDRAW_REQUEST_NOTIFY = 7;
    public static final int API_TYPE_CHECKOUT_TASK = 8;
    public static final int API_TYPE_BOOK_RESPONSE = 9;
    public static final int API_TYPE_CREATE_PAYMENT_PAYPAL = 10;
    public static final int API_TYPE_EXECUTE_PAYMENT_PAYPAL = 11;
    public static final int API_TYPE_PAYPAL_CALLBACK = 12;
    public static final int API_TYPE_FUNDING = 13;

    public static final String STATUS_SUCCESS = "Success";
    public static final String STATUS_FAILED = "Failed";

    private final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX");

    private final SimpleDateFormat sdfHis = new SimpleDateFormat("yyyyMMdd");

    private final Logger logger = Logger.getLogger(ProcessRequest.class);

    private Object task;
    private final SecureRandom random = new SecureRandom();
    private final RestFulClient client;

    private final DbInterface dbInf;
    private final Notify not = new Notify();

//    public static void main(String[] args) {
//        DbInterface db = new DbInterface();
//
//        System.out.println(db.getBalance("335", "AVA"));
//    }
//    private Crypter crypt;
    public ProcessRequest(int id, RequestQueue q) {
        mMain = Main.getInstance();
        mQueue = q;
        isRunning = false;
        mId = id;
        client = new RestFulClient();
        dbInf = new DbInterface();
//        crypt = new Crypter();
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Socket mySocket = mQueue.getRequest();

                if (mySocket != null) {
                    String ipClient = mySocket.getInetAddress().getHostAddress();

                    MyLog.Debug("THREAD " + mId + ": Got connection from '" + ipClient + "' -> TRANSID: " + "' (" + mQueue.getSize() + " left)");
                    process(mySocket, ipClient);
                }
                Thread.sleep(1);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * update balance of user when they book room.
     *
     * @param userID ID of user
     * @return: -1 query exception; 0 - account not found; 1 - balance is not
     * enough, 2 - success
     */
    private int pay(String userID, String currency, double payAmount, String orderID) {
        String sql = GlobalVariables.SQL_SELECT_USERID_FOR_UPDATE;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection("conn");
            conn.setAutoCommit(false);

            pstm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstm.setString(1, userID);
            pstm.setString(2, currency.toUpperCase());

            rs = pstm.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
//                double balance = Double.parseDouble(crypt.decrypt(en_balance));
                if (balance >= payAmount) {

                    logger.info("Update balance from " + balance + " to " + (balance - payAmount) + " by payamount:" + payAmount);

                    rs.updateDouble("balance", balance - payAmount);
                    rs.updateRow();

                    conn.commit();

                    return 2;
                } else {
                    logger.info("[" + orderID + "] balance is not enough. Require:" + payAmount + " actual: " + balance);
                    conn.commit();
                    return 1;
                }
            } else {
                //return account not found
                logger.warn("[" + orderID + "] UserID not found:" + userID + " with currency:" + currency);
                conn.commit();
                return 0;
            }
        } catch (SQLException ex) {
            logger.error("SQL ERROR:" + ex.getMessage(), ex);
            return -1;
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

    private void doCreateOrder(Socket s, String postLine) {
        long sta = System.currentTimeMillis();

        //set url of destination
        client.setUrl(ConfigLoader.getInstance().getUrlOrder());
        task = decodeParms(s, postLine, API_TYPE_ORDER);
        OrderTask order = (OrderTask) task;
        order.setCreate_at(sdf.format(System.currentTimeMillis()));

        if (!order.getPrivate_key().equals(ConfigLoader.getInstance().getPrivateKey())) {
            sendResultToClient(s, order.getOrder_id(), "ERROR", "invalid private key", "");
            return;
        }

        String token = generateToken(order.getOrder_id());
        logger.info("[" + order.getOrder_id() + "] generate token:" + token);
        order.setToken(token);
        order.setCallback_url(ConfigLoader.getInstance().getCallbackUrl());
        String response = client.createOrder(order);

        if ("".equals(response)) {
            logger.warn("System Error  ---------------->>>  Please check");
            sendResultToClient(s, order.getOrder_id(), "ERROR", "System Error", "");

            //insert history with status ERROR
            dbInf.insertOrderHist(order, "", "", "", "ERROR");
//            dbInf.insertHistory(order.getOrder_id(),
//                    GlobalVariables.TRANSACTION_STATUS_FAIL,
//                    Double.valueOf(order.getPrice_amount()), new Timestamp(System.currentTimeMillis()),
//                    "", "", "BOOKING", order.getUser_id(), new Timestamp(System.currentTimeMillis()), order.getPrice_currency());
        } else {
            Gson gson = new Gson();
            OrderResponse resp = gson.fromJson(response, OrderResponse.class);
            sendResultToClient(s, resp.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), resp.getStatus(), resp.getPayment_url());

            //insert history with status CREATED (order)
            dbInf.insertOrderHist(order, "", "", "", "CREATED");
//            dbInf.insertHistory(order.getOrder_id(),
//                    GlobalVariables.COINGATE_STATUS_PENDING,
//                    Double.valueOf(order.getPrice_amount()), new Timestamp(System.currentTimeMillis()),
//                    "", "", "BOOKING", order.getUser_id(), new Timestamp(System.currentTimeMillis()), order.getPrice_currency());
            logger.info("[" + order.getOrder_id() + "]finish process order in " + (System.currentTimeMillis() - sta));
        }
    }

    private boolean isAcceptConfirming(String currency) {
        String[] arr = ConfigLoader.getInstance().getAcceptConfirming();
        for (String cur : arr) {
            if (cur.equalsIgnoreCase(currency)) {
                return true;
            }
        }

        return false;
    }

    private void doCheckoutOrder(Socket s, String postLine) {
        long sta = System.currentTimeMillis();

        //set url of destination
        client.setUrl(ConfigLoader.getInstance().getUrlOrder());
        task = decodeParms(s, postLine, API_TYPE_CHECKOUT_TASK);
        CheckoutTask checkout = (CheckoutTask) task;
        checkout.setCreate_at(sdf.format(System.currentTimeMillis()));

        if (!checkout.getPrivate_key().equals(ConfigLoader.getInstance().getPrivateKey())) {
            sendResultToClient(s, checkout.getOrder_id(), "ERROR", "invalid private key", "");
            return;
        }

        String token = generateToken(checkout.getOrder_id());
        logger.info("[" + checkout.getOrder_id() + "] generate token:" + token);
        checkout.setToken(token);
        checkout.setCallback_url(ConfigLoader.getInstance().getCallbackUrl());
        String response = client.createOrder(checkout);

        logger.info("RESPONSE FROM COINGATE:" + response);

        if ("".equals(response)) {
            logger.warn("System Error  ---------------->>>  Please check");
            sendResultToClient(s, checkout.getOrder_id(), "ERROR", "System Error", "");

            //insert history with status ERROR
            dbInf.insertOrderHist(checkout, "", "", "", "ERROR");
//            dbInf.insertHistory(checkout.getOrder_id(),
//                    GlobalVariables.TRANSACTION_STATUS_FAIL,
//                    Double.valueOf(checkout.getPrice_amount()), new Timestamp(System.currentTimeMillis()),
//                    "", "", "BOOKING", checkout.getUser_id(), new Timestamp(System.currentTimeMillis()), checkout.getPrice_currency());
        } else {
            Gson gson = new Gson();
            OrderResponse resp = gson.fromJson(response, OrderResponse.class);

            String id = resp.getId();
            client.setUrl(ConfigLoader.getInstance().getUrlOrder() + "/" + id + "/checkout");
            logger.info("URL REQUEST:" + client.getUrl());
            String chkOutResp = client.checkOut(checkout.getPay_currency());

            //parse to object
            CheckoutResp rspObj = gson.fromJson(chkOutResp, CheckoutResp.class);
            if (rspObj.getOrderId() != null && !rspObj.getOrderId().equalsIgnoreCase("")) {

                sendResultToClient(s, chkOutResp);
                //insert history with status CREATED (order)
                dbInf.insertOrderHist(checkout, "", "", "", GlobalVariables.COINGATE_STATUS_PENDING);
            } else {

            }

//            dbInf.insertHistory(checkout.getOrder_id(),
//                    GlobalVariables.COINGATE_STATUS_PENDING,
//                    Double.valueOf(checkout.getPrice_amount()), new Timestamp(System.currentTimeMillis()),
//                    "", "", "BOOKING", checkout.getUser_id(), new Timestamp(System.currentTimeMillis()), checkout.getPrice_currency());
            logger.info("[" + checkout.getOrder_id() + "]finish process order in " + (System.currentTimeMillis() - sta));
        }
    }

    private void doCallback(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        task = decodeParms(s, postLine, API_TYPE_CALLBACK);
        CallbackTask callback = (CallbackTask) task;
        //response http code 200 to notify
        sendResponse(s, HttpResponse.HTTP_OK, "application/json", null, null);
        logger.info("Receive Callback:" + postLine);
        double recvAmount = Double.valueOf(callback.getReceive_amount());
        //insert DB callback transaction.
        String json = createJsonRevenue(recvAmount, callback.getReceive_currency().toUpperCase());
        dbInf.insertOrderHist(callback, "", json);

        boolean isAcceptConfirming = isAcceptConfirming(callback.getPay_currency());

        if (isAcceptConfirming) {

            //check status of transaction
            if (callback.getStatus().equalsIgnoreCase(GlobalVariables.COINGATE_STATUS_PAID)
                    || GlobalVariables.COINGATE_STATUS_CONFIRMING.equalsIgnoreCase(callback.getStatus())) {
//                client.setUrl(ConfigLoader.getInstance().getServiceUrl());

                //enqueue or rest client
                Main.getInstance().getNotifyQueue().enqueue(callback.getOrder_id());
//                dbInf.updateStatusHistory(callback.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, "BOOKING");

//                client.notifyService(callback);
            }
//            else if (callback.getStatus().equalsIgnoreCase(GlobalVariables.COINGATE_STATUS_EXPIRED)) {
//                dbInf.updateStatusHistory(callback.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_EXPIRED, "BOOKING");
//            } else if (callback.getStatus().equalsIgnoreCase(GlobalVariables.COINGATE_STATUS_CANCELED)) {
//                dbInf.updateStatusHistory(callback.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_CANCEL, "BOOKING");
//            }
        } else {
            //check status of transaction
            if (callback.getStatus().equalsIgnoreCase(GlobalVariables.COINGATE_STATUS_PAID)) {
//                client.setUrl(ConfigLoader.getInstance().getServiceUrl());

                //enqueue or rest client
                Main.getInstance().getNotifyQueue().enqueue(callback.getOrder_id());
//                dbInf.updateStatusHistory(callback.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, "BOOKING");
//                client.notifyService(callback);
            }
//            else if (callback.getStatus().equalsIgnoreCase(GlobalVariables.COINGATE_STATUS_EXPIRED)) {
//                dbInf.updateStatusHistory(callback.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_EXPIRED, "BOOKING");
//            } else if (callback.getStatus().equalsIgnoreCase(GlobalVariables.COINGATE_STATUS_CANCELED)) {
//                dbInf.updateStatusHistory(callback.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_CANCEL, "BOOKING");
//            }
//            else if (callback.getStatus().equalsIgnoreCase(GlobalVariables.COINGATE_STATUS_CONFIRMING)) {
//                dbInf.updateStatusHistory(callback.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_PENDING);
//            }
        }

        logger.info("[" + callback.getOrder_id() + "]finish process callback in " + (System.currentTimeMillis() - sta) + " ms");
    }

    /**
     * Pay in user internal wallet
     *
     * @param s socket client keep alive
     * @param postLine content of client request
     */
    private void doPay(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        task = decodeParms(s, postLine, API_TYPE_PAY);

        PayTask payTask = (PayTask) task;
        try {
            Double payAmount = Double.valueOf(payTask.getPay_amount());

            int payResult = pay(payTask.getUser_id(), payTask.getPay_currency(), payAmount, payTask.getOrder_id());

            switch (payResult) {
                case -1: //system error
                    sendResultToClient(s, payTask.getOrder_id(), "ERROR", "System Error");
                    dbInf.insertOrderHist(payTask, "ERROR", null);

                    dbInf.insertHistory(payTask.getBooking_number(),
                            GlobalVariables.TRANSACTION_STATUS_FAIL, payAmount,
                            new Timestamp(System.currentTimeMillis()), "", "", "PAY", payTask.getUser_id(),
                            new Timestamp(System.currentTimeMillis()), payTask.getPay_currency(), payTask.getOrder_id(), IDgenerator.getInstance().genID());

                    break;

                case 0: //user not found with pay_currency
                    sendResultToClient(s, payTask.getOrder_id(), "FAIL", "user_id:" + payTask.getUser_id() + " with pay_currency:" + payTask.getPay_currency() + " is not found");
                    dbInf.insertOrderHist(payTask, "FAIL", null);

                    dbInf.insertHistory(payTask.getBooking_number(),
                            GlobalVariables.TRANSACTION_STATUS_FAIL, payAmount,
                            new Timestamp(System.currentTimeMillis()), "", "", "PAY", payTask.getUser_id(),
                            new Timestamp(System.currentTimeMillis()), payTask.getPay_currency(), payTask.getOrder_id(), IDgenerator.getInstance().genID());
                    break;

                case 1: // pay fail: balance is not enough
                    sendResultToClient(s, payTask.getOrder_id(), "FAIL", "user_id:" + payTask.getUser_id() + " balance " + payTask.getPay_currency() + " is not enough");
                    dbInf.insertOrderHist(payTask, "FAIL", null);

                    dbInf.insertHistory(payTask.getBooking_number(),
                            GlobalVariables.TRANSACTION_STATUS_FAIL, payAmount,
                            new Timestamp(System.currentTimeMillis()), "", "", "PAY", payTask.getUser_id(),
                            new Timestamp(System.currentTimeMillis()), payTask.getPay_currency(), payTask.getOrder_id(), IDgenerator.getInstance().genID());
                    break;

                case 2: // pay success
                    sendResultToClient(s, payTask.getOrder_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "pay successfully");

                    String json = createJsonWallet(payAmount);
                    dbInf.insertOrderHist(payTask, GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), json);

                    String transID = IDgenerator.getInstance().genID();
                    dbInf.insertHistory(payTask.getBooking_number(),
                            GlobalVariables.TRANSACTION_STATUS_PENDING, payAmount,
                            new Timestamp(System.currentTimeMillis()), "", "", "PAY", payTask.getUser_id(),
                            new Timestamp(System.currentTimeMillis()), payTask.getPay_currency(), payTask.getOrder_id(), transID);

                    //push to map
                    PayTaskWrapper wrapper = new PayTaskWrapper(payTask, transID);
                    GlobalObject.getInstance().getHashOrder().put(payTask.getOrder_id(), wrapper);
                    //enqueue
                    Main.getInstance().getNotifyQueue().enqueue(payTask.getOrder_id());

                    break;

                default: //unknow error
                    sendResultToClient(s, payTask.getOrder_id(), "ERROR", "unkown");
                    dbInf.insertOrderHist(payTask, "ERROR", null);

                    dbInf.insertHistory(payTask.getBooking_number(),
                            GlobalVariables.TRANSACTION_STATUS_FAIL, payAmount,
                            new Timestamp(System.currentTimeMillis()), "", "", "PAY", payTask.getUser_id(),
                            new Timestamp(System.currentTimeMillis()), payTask.getPay_currency(), payTask.getOrder_id(), IDgenerator.getInstance().genID());
                    break;
            }
        } catch (NumberFormatException ex) {
            logger.error("[" + payTask.getOrder_id() + "] Invalid pay_amount: " + payTask.getPay_amount(), ex);
            sendResultToClient(s, payTask.getOrder_id(), "ERROR", "invalid pay_amount:" + payTask.getPay_amount());
        }

        MyLog.Infor("[" + payTask.getOrder_id() + "] Finish ProcessRequest (" + (System.currentTimeMillis() - sta) + " ms)");
    }

    private String createJsonWallet(double amount) {
        double USD = amount * SyncRateProcess.getInstance().getRate(GlobalVariables.CURRENCY_USD);
        double EUR = amount * SyncRateProcess.getInstance().getRate(GlobalVariables.CURRENCY_EUR);

        Wallet wallet = new Wallet(USD, EUR, amount);
        Gson gson = new Gson();
        String json = gson.toJson(wallet);

        return json;

    }

    private String createJsonRevenue(double amount, String currency) {

        double AVA = amount / SyncRateProcess.getInstance().getRate(currency);

        double USD = AVA * SyncRateProcess.getInstance().getRate(GlobalVariables.CURRENCY_USD);
        double EUR = AVA * SyncRateProcess.getInstance().getRate(GlobalVariables.CURRENCY_EUR);

        logger.info("input:" + amount + " --> " + currency);
        logger.info("output:" + AVA + ":" + USD + ":" + EUR);

        Coingate coingate = new Coingate(USD, EUR, AVA);
        Gson gson = new Gson();
        String json = gson.toJson(coingate);

        return json;

    }

    private boolean notify(String userID) {
        //send socket update
        int userid = Integer.valueOf(userID);
        String email = dbInf.getEmail(userid);
        UserWallet uw = dbInf.getUserWallet(userid);
        float balance = uw.getBalance();

        {
            try {
                not.sendMessageToSocket(email, userid, uw.getWallet_add(), balance);
                return true;
            } catch (URISyntaxException ex) {
                logger.error(ex.getMessage(), ex);
                return false;
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                return false;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                return false;
            }
        }
    }

    private void doDepositNotify(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        task = decodeParms(s, postLine, API_TYPE_DEPOSIT);
        Deposit dTask = (Deposit) task;

        //check wallet transaction
        NEP5Transfer nep5 = client.checkTXid(dTask.getData().getTxid(), dTask.getData().getCurrency(),
                dTask.getData().getAmount(), dTask.getData().getAddress());

        //retry
        if (nep5 == null) {
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(10000);

                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(ProcessRequest.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                logger.info("[times:" + (i + 1) + "]retry check tx: " + dTask.getData().getTxid());
                nep5 = client.checkTXid(dTask.getData().getTxid(), dTask.getData().getCurrency(),
                        dTask.getData().getAmount(), dTask.getData().getAddress());

                if (nep5 != null) {
                    break;
                }
            }

            if (nep5 == null) {
                logger.info("invalid transaction " + dTask.getData().getTxid() + " --> ignore");
                return;
            }
        }

        int checkTxExist = dbInf.checkTxExist(dTask.getData().getTxid(), dTask.getData().getAddress());

        logger.info("result of check:" + checkTxExist);
        if (checkTxExist != 0) {
            logger.info("txid already exist --> ignore:" + dTask.getData().getTxid());
            return;
        }

        String userID = dbInf.checkAddress(dTask.getData().getAddress(), dTask.getData().getCurrency());

        switch (userID) {

            case "IGNORE":
                //address not found --> ignore
                break;
            case "WARNING": //too many address
                logger.warn("too many address --> send warning");
                dbInf.insertDepositHis(dTask, "WARNING", nep5.getFrom_address());
                break;
            case "ERROR": //system error
                logger.warn("check fail --> send warning");
                dbInf.insertDepositHis(dTask, "ERROR", nep5.getFrom_address());
                break;
            default:
                String curency = dTask.getData().getCurrency();
                Double amount;
                try {
                    amount = Double.valueOf(dTask.getData().getAmount());
                    String address = dTask.getData().getAddress();
                    dbInf.updateBalance(curency, amount, address);
                    dbInf.insertDepositHis(dTask, userID, nep5.getFrom_address());
                    dbInf.insertHistory(dTask.getData().getTxid(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, amount, new Timestamp(System.currentTimeMillis()),
                            nep5.getFrom_address(), address, "DEPOSIT", userID, null, curency,
                            null, IDgenerator.getInstance().genID());

                    //notify socket
                    //notify update balance
                    boolean rs;
                    for (int i = 0; i < 3; i++) {
                        rs = notify(userID);

                        if (rs) {
                            break;
                        }
                    }

                    for (int i = 0; i < 3; i++) {
                        rs = notify(userID);

                        if (rs) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    logger.error("invalid amount:" + dTask.getData().getAmount() + " -->" + e.getMessage(), e);
                }
                break;
        }

        logger.info("finish do deposit in " + (System.currentTimeMillis() - sta) + " ms");
    }

    private void doWithdrawNotify(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        task = decodeParms(s, postLine, API_TYPE_WITHDRAW);
        WithDraw wdTask = (WithDraw) task;

        //check wallet transaction
        NEP5Transfer nep5 = client.checkTXid(wdTask.getData().getTxid(), wdTask.getData().getCurrency(),
                wdTask.getData().getAmount(), wdTask.getData().getTo_address());
        //retry
        if (nep5 == null) {
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(10000);

                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(ProcessRequest.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                logger.info("[times:" + (i + 1) + "]retry check tx: " + wdTask.getData().getTxid());
                nep5 = client.checkTXid(wdTask.getData().getTxid(), wdTask.getData().getCurrency(),
                        wdTask.getData().getAmount(), wdTask.getData().getTo_address());

                if (nep5 != null) {
                    break;
                }
            }

            if (nep5 == null) {
                logger.info("invalid transaction " + wdTask.getData().getTxid() + " --> ignore");
                return;
            }
        }

        int checkTxExist = dbInf.checkTxExist(wdTask.getData().getTxid(), null);
        if (checkTxExist != 0) {
            logger.info("txid already exist --> ignore:" + wdTask.getData().getTxid());
            return;
        }

        //check address withdraw to 
        long transactionID;
        try {
            transactionID = Long.valueOf(wdTask.getData().getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            transactionID = -2;
        }
        int checkID = dbInf.checkTransactionExist(transactionID);
        String requestsID = dbInf.getRequestID(wdTask.getData().getId());
        if (requestsID == null) {
            requestsID = "";
        }

        switch (checkID) {
            case 0: // address and currency valid

                String status = wdTask.getEvent();
                switch (status.toLowerCase()) {
                    case "completed": //withdraw success
                        dbInf.insertWithdrawHis("", wdTask.getData().getCurrency(), wdTask.getData().getAmount(), wdTask.getData().getTo_address(), "0", wdTask.getEvent(), transactionID, requestsID);
                        logger.info("Withdraw transaction " + transactionID + " --> success");
                        dbInf.updateStatusHistory(requestsID, GlobalVariables.TRANSACTION_STATUS_SUCCESS, "WITHDRAW");
                        break;
                    case "failed": //withdraw fail --> refund
                        logger.info("Withdraw transaction " + transactionID + ": fail --> refund transaction");
                        dbInf.insertWithdrawHis("", wdTask.getData().getCurrency(), wdTask.getData().getAmount(), wdTask.getData().getTo_address(), "0", wdTask.getEvent(), transactionID, requestsID);
                        //refund
                        Refund refund = dbInf.getRefund(transactionID);

                        if (refund != null) {
                            dbInf.updateBalance(refund.getUser_id(), wdTask.getData().getCurrency(), refund.getAmount());
                            logger.info("[" + refund.getUser_id() + "]" + "refund success");

                            String transID = dbInf.getHisTransID(wdTask.getData().getTxid(), "WITHDRAW");

                            //insert refund history
                            dbInf.insertHistory(wdTask.getData().getTxid(), GlobalVariables.TRANSACTION_STATUS_SUCCESS,
                                    Double.valueOf(wdTask.getData().getAmount()),
                                    new Timestamp(System.currentTimeMillis()), "", "",
                                    "REFUND", refund.getUser_id(), null, wdTask.getData().getCurrency(),
                                    transID, IDgenerator.getInstance().genID());
                        } else {
                            logger.info("[" + checkID + "]" + "fail to get info refund");
                        }
                        dbInf.updateStatusHistory(requestsID, GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");

                        break;
                    default: //dont know event
                        logger.info("Withdraw transaction " + transactionID + " --> don't know event");
                        dbInf.insertWithdrawHis("", wdTask.getData().getCurrency(), wdTask.getData().getAmount(), wdTask.getData().getTo_address(), "0", wdTask.getEvent(), transactionID, requestsID);
                        break;
                }

                break;
            case 1:
                //address not found --> ignore
                logger.info("address " + wdTask.getData().getTo_address() + " not found in system --> ignore transaction.");
                break;
            case 2: //too many address
                logger.warn("too many address --> send warning");
                dbInf.insertWithdrawHis("", wdTask.getData().getCurrency(), wdTask.getData().getAmount(), wdTask.getData().getTo_address(), "0", wdTask.getEvent(), transactionID, requestsID);
                dbInf.updateStatusHistory(requestsID, GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                break;
            case 3: //system error
                logger.warn("check fail --> send warning");
                dbInf.updateStatusHistory(requestsID, GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                break;
            default:
            //ignore
        }

        logger.info("finish do Withdraw in " + (System.currentTimeMillis() - sta) + " ms");
    }

    private void doWithdrawRequest(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        task = decodeParms(s, postLine, API_TYPE_WITHDRAW_REQUEST);
        WithdrawRequestTask wdTask = (WithdrawRequestTask) task;

        String from_add = dbInf.getAddressfromUserCurrency(wdTask.getUser_id(), wdTask.getCurrency());
        switch (from_add) {
            case ""://address not found
                sendResultToClient(s, "invalid", "address for user " + wdTask.getUser_id() + " is not found");
                dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                return;
            case "ERROR":
                sendResultToClient(s, "error", "system error");
                dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                return;
            default:
                if (from_add.equals(wdTask.getTo_address())) {
                    sendResultToClient(s, "ignore", "with draw to self address");
                    dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, "WITHDRAW");
                    return;
                }
                break;
        }

        long transaction_id = -1;

        //check limit withdraw in day
        double[] arr = dbInf.checkLimit(wdTask);
        int check = (int) arr[0];

        switch (check) {
            case 0: //check OK --> withdraw

                int payResult = pay(wdTask.getUser_id(), wdTask.getCurrency(), Double.valueOf(wdTask.getAmount()), wdTask.getUser_id());

                switch (payResult) {
                    case 0: //user not found
                        logger.info("user " + wdTask.getUser_id() + " is not found");
                        sendResultToClient(s, "invalid", "user not found");
                        dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                        break;
                    case 1: //litmited withdraw
                        String msg = GlobalVariables.MSG_NOT_ENOUGH_BALANCE;
                        sendResultToClient(s, "invalid", msg);
                        logger.info("balance user " + wdTask.getUser_id() + " is not enough:" + msg);
                        dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                        break;
                    case 2: //success
                        logger.info("[" + wdTask.getUser_id() + "]withdraw internal wallet success with " + wdTask.getAmount() + " " + wdTask.getCurrency());
                        //response to client
//                        sendError(s, "200", "transaction processing", "");
                        sendResultToClient(s, GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "ok");

                        String userID = dbInf.checkExistAddress(wdTask.getTo_address(), wdTask.getCurrency());

                        if (userID != null && !userID.equalsIgnoreCase("")) { //update to_address balance
                            logger.info("[" + wdTask.getUser_id() + "]found address " + wdTask.getTo_address() + " --> update balance");
                            dbInf.updateBalance(wdTask.getCurrency(), Double.valueOf(wdTask.getAmount()), wdTask.getTo_address());

                            //insert deposit his for to_address transaction
                            logger.info("going to insert deposit history");
                            dbInf.insertDepositHis(wdTask.getCurrency(), wdTask.getTo_address(), Double.valueOf(wdTask.getAmount()), userID, from_add);

                            //insert history deposit for to_address transaction
                            logger.info("going to insert history for deposit transaction");
                            dbInf.insertHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, Double.valueOf(wdTask.getAmount()),
                                    new Timestamp(System.currentTimeMillis()), from_add, wdTask.getTo_address(),
                                    "DEPOSIT", userID, null, wdTask.getCurrency(), null, IDgenerator.getInstance().genID());

                            //update status
                            logger.info("going to update status of transaction");
                            dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, "WITHDRAW");

//                            dbInf.updateStatusHistory(userID, userID);
                            //insert withdraw his for from_address transaction
                            dbInf.insertWithdrawHis(wdTask.getUser_id(), wdTask.getCurrency(), wdTask.getAmount(),
                                    wdTask.getTo_address(), wdTask.getMax_per_day(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, transaction_id, wdTask.getRequest_id());

                            //notify update balance
                            boolean rs;
                            for (int i = 0; i < 3; i++) {
                                rs = notify(wdTask.getUser_id());

                                if (rs) {
                                    break;
                                }
                            }

                            for (int i = 0; i < 3; i++) {
                                rs = notify(userID);

                                if (rs) {
                                    break;
                                }
                            }
                        } else if (userID != null && userID.isEmpty()) { //send request withdraw
                            logger.info("[" + wdTask.getUser_id() + "]send request withdraw to address:" + wdTask.getTo_address());
                            client.setUrl(ConfigLoader.getInstance().getWalletURL() + "withdrawals");
                            try {
                                //                            client.requestWithdraw(wdTask);
                                String rs = client.sendPost(wdTask);

                                WithdrawResp rsp = getRespFromJson(rs);
                                if (rsp != null) {
                                    if (rsp.getId() == null || rsp.getId().equals("")) {
                                        //transaction fail --> refund
                                        dbInf.updateBalance(wdTask.getUser_id(), wdTask.getCurrency(), Double.valueOf(wdTask.getAmount()));

                                        String transactionID = dbInf.getHisTransID(wdTask.getRequest_id(), "WITHDRAW");
                                        //insert history refund
                                        dbInf.insertHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, Double.valueOf(wdTask.getAmount()),
                                                new Timestamp(System.currentTimeMillis()), "", from_add,
                                                "REFUND", wdTask.getUser_id(), null, wdTask.getCurrency(), transactionID, IDgenerator.getInstance().genID());

                                        logger.info("[" + wdTask.getUser_id() + "]refund success:" + wdTask.getAmount());
                                        dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                                    } else {

                                        transaction_id = Long.valueOf(rsp.getId());

                                        dbInf.insertWithdrawHis(wdTask.getUser_id(), wdTask.getCurrency(), wdTask.getAmount(),
                                                wdTask.getTo_address(), wdTask.getMax_per_day(), GlobalVariables.TRANSACTION_STATUS_PENDING, transaction_id, wdTask.getRequest_id());
//        dbInf.insert
//                                //update status of transaction
//                                dbInf.insertHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_PENDING, Double.parseDouble(wdTask.getAmount()),
//                                        new Timestamp(System.currentTimeMillis()), "", wdTask.getTo_address(), "WITHDRAW", wdTask.getUser_id(), null, wdTask.getCurrency());

                                        //update status
                                        logger.info("going to update status of transaction");
                                        dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_PENDING, "WITHDRAW");
                                    }
                                } else {
                                    //transaction fail --> refund
                                    dbInf.updateBalance(wdTask.getUser_id(), wdTask.getCurrency(), Double.valueOf(wdTask.getAmount()));

                                    String transactionID = dbInf.getHisTransID(wdTask.getRequest_id(), "WITHDRAW");
                                    //insert history refund
                                    dbInf.insertHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, Double.valueOf(wdTask.getAmount()),
                                            new Timestamp(System.currentTimeMillis()), "", from_add,
                                            "REFUND", wdTask.getUser_id(), null, wdTask.getCurrency(), transactionID, IDgenerator.getInstance().genID());

                                    logger.info("[" + wdTask.getUser_id() + "]refund success:" + wdTask.getAmount());
                                    dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                                }
                            } catch (NumberFormatException ex) {
                                logger.error(ex.getMessage(), ex);
                                transaction_id = -2;
                                dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                            }

                            //send notify socket
                            notify(wdTask.getUser_id());
                        } else {
                            logger.error("check fail --> Please Check");
                        }
                        break;

                    default:
                        //do nothing
                        break;
                }

                break;

            case 1: //check not OK --> response user limited
                logger.info("User ID:" + wdTask.getUser_id() + " limited per day.");
                sendResultToClient(s, "limited", "limited per day");

                dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                break;
            case 2: //invalid limit input --> response error
                String msg = GlobalVariables.MSG_LIMITED_WITHDRAW.replace("#WTDRAWED#", arr[1] + "")
                        .replace("#MAXIMUM#", wdTask.getMax_per_day());
                logger.info("max per day invalid:" + wdTask.getMax_per_day() + ". msg:" + msg);
                sendResultToClient(s, "invalid", msg);
                dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                break;
            case -1: //system error
                logger.info("System ERROR:");
                sendResultToClient(s, "error", "system error");
                dbInf.updateStatusHistory(wdTask.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL, "WITHDRAW");
                break;
        }

        logger.info(
                "finish do Withdraw in " + (System.currentTimeMillis() - sta) + " ms");
    }

    private WithdrawResp getRespFromJson(String json) {
        try {
            Gson gson = new Gson();
            WithdrawResp resp = gson.fromJson(json, WithdrawResp.class);
            return resp;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

//    private boolean check
    public void process(Socket s, String ipClient) {
        long start = System.currentTimeMillis();
        long contentLength = 0;
        String ipRequest = "";
        logger.info("THREAD " + mId + ": Start ProcessRequest: '" + ipClient + "'");
        InputStream is = null;
        BufferedReader in = null;
        try {
            s.setSoTimeout(Integer.parseInt(mMain.getSOCKET_TIMEOUT()));
            s.setKeepAlive(true);

            is = s.getInputStream();
            if (is != null) {
                InputStreamReader defaultReader = new InputStreamReader(is);
                String defaultEncoding = defaultReader.getEncoding();

                in = new BufferedReader(new InputStreamReader(is, defaultEncoding));

                String sInput = in.readLine();
                MyLog.Debug("THREAD " + mId + ": METHOD INPUT '" + ipClient + "': '" + sInput + "'");

                if (!"".equalsIgnoreCase(sInput) && sInput != null) {
                    StringTokenizer st = new StringTokenizer(sInput);
                    if (st.hasMoreTokens()) {
                        String method = st.nextToken();
                        if (st.hasMoreTokens()) {
                            String uri = decodePercent(s, st.nextToken(), ipClient);
                            MyLog.Infor("THREAD " + mId + ": URI OF REQUEST '" + ipClient + "': '" + uri + "'");
                            if (st.hasMoreTokens()) {
                                Properties header = new Properties();
                                String line = in.readLine();
//                                logger.info("HEADER:" + line);

                                while (line.trim().length() > 0) {
                                    int p = line.indexOf(':');
//                                    String key = 
                                    header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
//                                    logger.info("header:" + );
                                    line = in.readLine();
                                }

                                ipRequest = header.getProperty("x-forwarded-for", "");
                                contentLength = Long.valueOf(header.getProperty("content-length", "0"));

                                if (!"".equalsIgnoreCase(ipRequest)) {
                                    ipClient = ipRequest;
                                }

                                MyLog.Debug("THREAD " + mId + ": HEADER OF REQUEST '" + ipClient + "': '" + header + "'");

                                if ("POST".equals(method.toUpperCase())) {
                                    long startContent = System.currentTimeMillis();
                                    String postLine = "";

                                    int r;
                                    for (int i = 0; i < contentLength; i++) {
                                        r = in.read();
                                        postLine += (char) r;
                                    }

                                    MyLog.Infor("THREAD " + mId + ": CONTENT OF REQUEST '" + ipClient + "': '"
                                            + postLine + "' "
                                            + "(length: " + postLine.length() + ") "
                                            + "(" + (System.currentTimeMillis() - startContent) + " ms)");

                                    switch (uri) {
                                        case "/payment/order": //create order to coingate
                                            doCreateOrder(s, postLine);
                                            break;
                                        case "/payment/order-checkout": //create order to coingate
                                            doCheckoutOrder(s, postLine);
                                            break;
                                        case "/payment/coingate/callback": //callback from coingate to update status of order
                                            doCallback(s, postLine);
                                            break;
                                        case "/payment/pay": //pay by AVA
                                            doPay(s, postLine);
                                            break;
                                        case "/payment/book-response": //service process response
                                            sendError(s, "200", "OK", "0");
                                            doBookResponse(s, postLine);
                                            break;
                                        case "/payment/wallet": //case user deposit
                                            if (postLine.contains("deposit")) {
                                                sendError(s, "200", "OK", "0");
                                                doDepositNotify(s, postLine);
                                            } else if (postLine.contains("withdraw")) {
                                                sendError(s, "200", "OK", "0");
                                                doWithdrawNotify(s, postLine);
                                            }
                                            break;
                                        case "/payment/wallet/withdraw-request": //case backend notify create withdraw request
//                                            sendError(s, "200", "OK", "0");
                                            doWithdrawClientNotify(s, postLine);
                                            break;
                                        case "/payment/wallet/withdraw": //case user withdraw
                                            doWithdrawRequest(s, postLine);
                                            break;
                                        case "/payment/paypal/create-payment":
                                            doCreatePaymentToPaypal(s, postLine);
                                            break;
                                        case "/payment/paypal/execute-payment":
                                            doExecutePaymentToPaypal(s, postLine);
                                            break;
                                        case "/payment/paypal/callback":
                                            doPaypalCallback(s, postLine, header);
                                            break;

                                        case "/payment/funding":
                                            doFunding(s, postLine, header);
                                            break;
                                        default:
                                            sendError(s, "401", "invalid", "");
                                            break;
                                    }
                                } else {
                                    if ("/loadConfig()".equalsIgnoreCase(uri)) {
                                        Main.getInstance().loadConfig();
                                    } else if (uri.startsWith("/wallet/list-transaction")) {
                                        String autho = header.getProperty("authorization");
                                        if (autho == null) {
                                            logger.info("Authorization header not found");
                                            sendError(s, "401", "invalid", "0");
                                            return;
                                        } else {
                                            autho = autho.replace("Bearer ", "");

//                                            logger.info("token:" + autho);
                                            String userID = Decrypter.getInstance().decryptToken(autho);
                                            if (userID == null) {
                                                logger.info("Authorization header invalid");
                                                sendError(s, "401", "invalid", "0");
                                                return;
                                            } else {
//                                                logger.info("User ID = " + userID);
                                                Map<String, List<String>> values = splitQuery(uri);
                                                doHistory(s, values, userID);
                                            }
                                        }

                                    } else if (uri.startsWith("/payment/user-delete")) {
                                        Map<String, List<String>> values = splitQuery(uri);
                                        sendError(s, "200", "OK", "0");
                                        doUpdateDeleted(values);
                                    } else if (uri.startsWith("/payment/details-order")) {
                                        Map<String, List<String>> values = splitQuery(uri);
                                        doGetOrderDetails(s, values);
                                    } else if (uri.startsWith("/payment/report-revenue")) {
                                        String autho = header.getProperty("authorization");
                                        if (autho == null) {
                                            logger.info("Authorization header not found");
                                            sendError(s, "401", "invalid", "0");
                                            return;
                                        } else {
//                                            autho = autho.replace("Bearer ", "");
                                            String private_key = "private_key " + ConfigLoader.getInstance().getPrivateKey();
//                                            logger.info("token:" + autho);
//                                            String userID = Decrypter.getInstance().decryptToken(autho);
                                            if (autho.equals(private_key)) {
                                                Map<String, List<String>> values = splitQuery(uri);
                                                doReportRevenue(s, values);
                                            } else {
                                                logger.info("Authorization header invalid:" + autho);
                                                sendError(s, "401", "invalid", "0");
                                                return;
                                            }
                                        }

                                    } else {
                                        sendError(s, "200", "OK", "0");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ee) {
            logger.error("THREAD " + mId + ": ERROR OF REQUEST '" + ipClient + "': " + ee.getMessage(), ee);
//            sendResultToClient(s, "", "ERROR", "invalid input", "");
        } finally {
            if (in != null) {
                try {
                    in.close();

                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ProcessRequest.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (is != null) {
                try {
                    is.close();

                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ProcessRequest.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        long processTime = System.currentTimeMillis() - start;

        if (!ipRequest.isEmpty()) {
            MyLog.Infor("THREAD " + mId + ": Finish ProcessRequest '" + ipClient + "' (" + processTime + " ms)");
        }
    }

    private Map<String, List<String>> splitQuery(String uri) throws UnsupportedEncodingException {
        if (uri.contains("?")) {
            uri = uri.split("\\?")[1];
        }
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = uri.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

//    private void insert
    public synchronized String generateToken(String orderID) {
        long longToken = Math.abs(random.nextLong());
        String r = Long.toString(longToken, 16);
        return (orderID + "-" + r);
    }

    private void sendResultToClient(Socket s, String orderID, String status, String desc, String paymentURL) {
        try {
            JSONObject resp = new JSONObject();
            resp.put("order_id", orderID);
            resp.put("status", status);
            resp.put("description", desc);
            resp.put("payment_url", paymentURL);

            logger.info("RESPONSE TO CP : " + resp.toJSONString());

            InputStream inp = new ByteArrayInputStream(resp.toJSONString().getBytes());
            HttpResponse response = new HttpResponse(HttpResponse.HTTP_OK, HttpResponse.MIME_XML, inp);
            response.addHeader("content-length", "" + inp.available());
            sendResponse(s, HttpResponse.HTTP_OK, "application/json", response.header, response.data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void sendResultToClient(Socket s, String orderID, String status, String desc) {
        try {
            JSONObject resp = new JSONObject();
            resp.put("order_id", orderID);
            resp.put("status", status);
            resp.put("description", desc);

            logger.info("RESPONSE TO CP : " + resp.toJSONString());

            InputStream inp = new ByteArrayInputStream(resp.toJSONString().getBytes());
            HttpResponse response = new HttpResponse(HttpResponse.HTTP_OK, HttpResponse.MIME_XML, inp);
            response.addHeader("content-length", "" + inp.available());
            sendResponse(s, HttpResponse.HTTP_OK, "application/json", response.header, response.data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void sendResultToClient(Socket s, String status, String desc) {
        try {
            JSONObject resp = new JSONObject();
            resp.put("status", status);
            resp.put("description", desc);

            logger.info("RESPONSE TO CP : " + resp.toJSONString());

            InputStream inp = new ByteArrayInputStream(resp.toJSONString().getBytes());
            HttpResponse response = new HttpResponse(HttpResponse.HTTP_OK, HttpResponse.MIME_XML, inp);
            response.addHeader("content-length", "" + inp.available());
            sendResponse(s, HttpResponse.HTTP_OK, "application/json", response.header, response.data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void sendResultToClient(Socket s, String rsp) {
        try {

            logger.info("Send response to client:" + rsp);
            InputStream inp = new ByteArrayInputStream(rsp.getBytes());
            HttpResponse response = new HttpResponse(HttpResponse.HTTP_OK, HttpResponse.MIME_XML, inp);
            response.addHeader("content-length", "" + inp.available());
            sendResponse(s, HttpResponse.HTTP_OK, "application/json", response.header, response.data);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Decodes the percent encoding scheme. <br/>
     * For example: "an+example%20string" -> "an example string"
     *
     * @param str <code>String</code> value will be decoded
     * @return value after decode
     * @throws InterruptedException when processing in String value, may be out
     * of index bound,...
     */
    private String decodePercent(Socket s, String str, String id) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                switch (c) {
                    case '+':
                        sb.append(' ');
                        break;

                    case '%':
                        sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
//                        i += 2;
                        break;

                    default:

                        sb.append(c);
                        break;
                }
            }
            return new String(sb.toString().getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sendError(s, HttpResponse.HTTP_BADREQUEST, "BAD REQUEST: Bad percent-encoding.", id);
            return null;
        }

    }

    private String formToJson(String formInput) {
        formInput = formInput.replace("&", ",\"");
        formInput = formInput.replace("=", "\":");
        formInput = "{\"" + formInput + "}";

        return formInput;
    }

    private OrderTask makeOrderTask(String input) {
        Gson json = new Gson();
//        json.
        OrderTask oTask = json.fromJson(input, OrderTask.class);

        return oTask;
    }

    private CallbackTask makeCallbackTask(String input) {
        input = formToJson(input);
        Gson json = new Gson();
        CallbackTask cTask = json.fromJson(input, CallbackTask.class);

        return cTask;
    }

    private PayTask makePayTask(String input) {
        Gson json = new Gson();
        PayTask pTask = json.fromJson(input, PayTask.class);

        return pTask;
    }

    private Deposit makeNotifyDepositTask(String input) {
        Gson json = new Gson();
        Deposit depositTask = json.fromJson(input, Deposit.class);

        return depositTask;
    }

    private WithDraw makeNotifyWithdrawTask(String input) {
        Gson json = new Gson();
        WithDraw withdrawTask = json.fromJson(input, WithDraw.class);

        return withdrawTask;
    }

    private WithdrawRequestTask makeWithdrawRequestTask(String input) {
        Gson json = new Gson();
        WithdrawRequestTask withdrawRequestTask = json.fromJson(input, WithdrawRequestTask.class);

        return withdrawRequestTask;
    }

    private WithdrawClientNotifyTask makeWithdrawRequestNotifyTask(String input) {
        Gson json = new Gson();
        WithdrawClientNotifyTask withdrawNotifyTask = json.fromJson(input, WithdrawClientNotifyTask.class);

        return withdrawNotifyTask;
    }

    private CheckoutTask makeCheckoutTask(String input) {
        Gson json = new Gson();
        CheckoutTask checkoutTask = json.fromJson(input, CheckoutTask.class);

        return checkoutTask;
    }

    private BookResponseTask makeBookResponseTask(String input) {
        Gson json = new Gson();
        BookResponseTask bookResponse = json.fromJson(input, BookResponseTask.class);

        return bookResponse;
    }

    private CreatePaymentTask makeCreatePaymentPaypalTask(String input) {
        Gson json = new Gson();
        CreatePaymentTask createPayment = json.fromJson(input, CreatePaymentTask.class);

        return createPayment;
    }

    private ExecutePaymentTask makeExePaymentPaypalTask(String input) {
        Gson json = new Gson();
        ExecutePaymentTask exePayment = json.fromJson(input, ExecutePaymentTask.class);

        return exePayment;
    }

    private PaypalCallbackTask makePaypalCallbackTask(String input) {
        Gson json = new Gson();
        PaypalCallbackTask callback = json.fromJson(input, PaypalCallbackTask.class);

        return callback;
    }

    private FundingTask makeFungdingTask(String input) {
        Gson json = new Gson();
        FundingTask funding = json.fromJson(input, FundingTask.class);

        return funding;
    }

    /**
     * Decodes parameters in percent-encoded URI-format ( e.g.
     * "name=Jack%20Daniels&pass=Single%20Malt" ) and adds them to given
     * Properties.
     *
     * @param parms param string will be parse
     * @param p stored properties to store value
     * @throws InterruptedException when processing in String value, may be out
     * of index bound,...
     */
    private Object decodeParms(Socket s, String body, int type) {
        long start = System.currentTimeMillis();
        try {

            if ((body != null) && (!"".equalsIgnoreCase(body))) {

                switch (type) {
                    case API_TYPE_ORDER: //create order

                        OrderTask orderTask = makeOrderTask(body);

                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + orderTask.getOrder_id() + "',"
                                + "'" + orderTask.getTitle() + "',"
                                + "'" + orderTask.getCreate_at() + "',"
                                + "'" + orderTask.getToken() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return orderTask;

                    case API_TYPE_CALLBACK: //callback from coingate
                        CallbackTask cbTask = makeCallbackTask(body);

                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + cbTask.getOrder_id() + "',"
                                + "'" + cbTask.getStatus() + "',"
                                + "'" + cbTask.getCreated_at() + "',"
                                + "'" + cbTask.getToken() + "') (" + (System.currentTimeMillis() - start) + " ms)");

                        return cbTask;

                    case API_TYPE_PAY:// pay with user internal wallet
                        PayTask pTask = makePayTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + pTask.getOrder_id() + "',"
                                + "'" + pTask.getUser_id() + "',"
                                + "'" + pTask.getPay_amount() + "',"
                                + "'" + pTask.getPay_currency() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return pTask;

                    case API_TYPE_DEPOSIT:// pay with user internal wallet
                        Deposit dTask = makeNotifyDepositTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + dTask.getType() + "',"
                                + "'" + dTask.getEvent() + "',"
                                + "'" + dTask.getData().getAddress() + "',"
                                + "'" + dTask.getData().getCurrency() + "',"
                                + "'" + dTask.getData().getAmount() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return dTask;

                    case API_TYPE_WITHDRAW:// pay with user internal wallet
                        WithDraw wTask = makeNotifyWithdrawTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + wTask.getType() + "',"
                                + "'" + wTask.getEvent() + "',"
                                + "'" + wTask.getData().getTo_address() + "',"
                                + "'" + wTask.getData().getCurrency() + "',"
                                + "'" + wTask.getData().getAmount() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return wTask;

                    case API_TYPE_WITHDRAW_REQUEST:// pay with user internal wallet
                        WithdrawRequestTask wrTask = makeWithdrawRequestTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + wrTask.getUser_id() + "',"
                                + "'" + wrTask.getCurrency() + "',"
                                + "'" + wrTask.getAmount() + "',"
                                + "'" + wrTask.getTo_address() + "',"
                                + "'" + wrTask.getMax_per_day() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return wrTask;

                    case API_TYPE_WITHDRAW_REQUEST_NOTIFY:// pay with user internal wallet
                        WithdrawClientNotifyTask wrnTask = makeWithdrawRequestNotifyTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + wrnTask.getUser_id() + "',"
                                + "'" + wrnTask.getCurrency() + "',"
                                + "'" + wrnTask.getAmount() + "',"
                                + "'" + wrnTask.getTo_address() + "',"
                                + "'" + wrnTask.getMax_per_day() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return wrnTask;

                    case API_TYPE_CHECKOUT_TASK:// pay with user internal wallet
                        CheckoutTask coTask = makeCheckoutTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + coTask.getUser_id() + "',"
                                + "'" + coTask.getOrder_id() + "',"
                                + "'" + coTask.getPrice_amount() + "',"
                                + "'" + coTask.getPrice_currency() + "',"
                                + "'" + coTask.getReceive_currency() + "',"
                                + "'" + coTask.getPay_currency() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return coTask;

                    case API_TYPE_BOOK_RESPONSE:// pay with user internal wallet
                        BookResponseTask bookRspTask = makeBookResponseTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + bookRspTask.getOrder_id() + "',"
                                + "'" + bookRspTask.getStatus() + "',"
                                + "'" + bookRspTask.getReason() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return bookRspTask;

                    case API_TYPE_CREATE_PAYMENT_PAYPAL:// pay with user internal wallet
                        CreatePaymentTask createPaymentTask = makeCreatePaymentPaypalTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + createPaymentTask.getTransactions().get(0).getAmount().getTotal() + "',"
                                + "'" + createPaymentTask.getTransactions().get(0).getAmount().getCurrency() + "',"
                                + "'" + createPaymentTask.getTransactions().get(0).getDescription() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return createPaymentTask;

                    case API_TYPE_EXECUTE_PAYMENT_PAYPAL:
                        ExecutePaymentTask exePaymentTask = makeExePaymentPaypalTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + exePaymentTask.getPayment_id() + "',"
                                + "'" + exePaymentTask.getPayer_id() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return exePaymentTask;

                    case API_TYPE_PAYPAL_CALLBACK:// pay with user internal wallet
                        PaypalCallbackTask callbackTask = makePaypalCallbackTask(body);
//                        logger.info("Receive Request: '" + type + "': ("
//                                + "'" + callbackTask.getTransactions().get(0).getAmount().getTotal() + "',"
//                                + "'" + callbackTask.getTransactions().get(0).getAmount().getCurrency() + "',"
//                                + "'" + callbackTask.getTransactions().get(0).getDescription() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return callbackTask;

                    case API_TYPE_FUNDING:// pay with user internal wallet
                        FundingTask funding = makeFungdingTask(body);
//                        logger.info("Receive Request: '" + type + "': ("
//                                + "'" + callbackTask.getTransactions().get(0).getAmount().getTotal() + "',"
//                                + "'" + callbackTask.getTransactions().get(0).getAmount().getCurrency() + "',"
//                                + "'" + callbackTask.getTransactions().get(0).getDescription() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return funding;
                    default:
                        return null;
                }
            }
        } catch (Exception ex) {
            logger.error("THREAD " + mId + ": ERROR DECODE PARAMS '" + type + "': " + ex.getMessage(), ex);
            sendResultToClient(s, "", "error", "input invalid", "");
        }
        return null;
    }

    private Object decodeParms(String body, int type) {
        long start = System.currentTimeMillis();
        try {

            if ((body != null) && (!"".equalsIgnoreCase(body))) {

                switch (type) {
                    case API_TYPE_ORDER: //create order

                        OrderTask orderTask = makeOrderTask(body);

                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + orderTask.getOrder_id() + "',"
                                + "'" + orderTask.getTitle() + "',"
                                + "'" + orderTask.getCreate_at() + "',"
                                + "'" + orderTask.getToken() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return orderTask;

                    case API_TYPE_CALLBACK: //callback from coingate
                        CallbackTask cbTask = makeCallbackTask(body);

                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + cbTask.getOrder_id() + "',"
                                + "'" + cbTask.getStatus() + "',"
                                + "'" + cbTask.getCreated_at() + "',"
                                + "'" + cbTask.getToken() + "') (" + (System.currentTimeMillis() - start) + " ms)");

                        return cbTask;

                    case API_TYPE_PAY:// pay with user internal wallet
                        PayTask pTask = makePayTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + pTask.getOrder_id() + "',"
                                + "'" + pTask.getUser_id() + "',"
                                + "'" + pTask.getPay_amount() + "',"
                                + "'" + pTask.getPay_currency() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return pTask;

                    case API_TYPE_DEPOSIT:// pay with user internal wallet
                        Deposit dTask = makeNotifyDepositTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + dTask.getType() + "',"
                                + "'" + dTask.getEvent() + "',"
                                + "'" + dTask.getData().getAddress() + "',"
                                + "'" + dTask.getData().getCurrency() + "',"
                                + "'" + dTask.getData().getAmount() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return dTask;

                    case API_TYPE_WITHDRAW:// pay with user internal wallet
                        WithDraw wTask = makeNotifyWithdrawTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + wTask.getType() + "',"
                                + "'" + wTask.getEvent() + "',"
                                + "'" + wTask.getData().getTo_address() + "',"
                                + "'" + wTask.getData().getCurrency() + "',"
                                + "'" + wTask.getData().getAmount() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return wTask;

                    case API_TYPE_WITHDRAW_REQUEST:// pay with user internal wallet
                        WithdrawRequestTask wrTask = makeWithdrawRequestTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + wrTask.getUser_id() + "',"
                                + "'" + wrTask.getCurrency() + "',"
                                + "'" + wrTask.getAmount() + "',"
                                + "'" + wrTask.getTo_address() + "',"
                                + "'" + wrTask.getMax_per_day() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return wrTask;

                    case API_TYPE_WITHDRAW_REQUEST_NOTIFY:// pay with user internal wallet
                        WithdrawClientNotifyTask wrnTask = makeWithdrawRequestNotifyTask(body);
                        logger.info("Receive Request: '" + type + "': ("
                                + "'" + wrnTask.getUser_id() + "',"
                                + "'" + wrnTask.getCurrency() + "',"
                                + "'" + wrnTask.getAmount() + "',"
                                + "'" + wrnTask.getTo_address() + "',"
                                + "'" + wrnTask.getMax_per_day() + "') (" + (System.currentTimeMillis() - start) + " ms)");
                        return wrnTask;
                    default:
                        return null;
                }
            }
        } catch (Exception ex) {
            logger.error("THREAD " + mId + ": ERROR DECODE PARAMS '" + type + "': " + ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Returns an error message as a HTTP response and throws
     * InterruptedException to stop further request processing.
     *
     * @param status status of http processing
     * @param msg http message content
     * @throws InterruptedException exception will be threw after sending
     */
    private void sendError(Socket s, String status, String msg, String id) {
        long start = System.currentTimeMillis();
        try {
            sendResponse(s, status, "text/xml", null, new ByteArrayInputStream(msg.getBytes()));
        } catch (Exception e) {
            MyLog.Error(e);
        }
        MyLog.Debug("THREAD " + mId + ": SEND ERROR TO CLIENT '" + id + "' (" + (System.currentTimeMillis() - start) + " ms)");
    }

    /**
     * Sends given response to the socket.
     *
     * @param s
     * @param status status of http processing
     * @param mime content type of response
     * @param header_sendResponse header values
     * @param data_sendResponse <code>InputStream</code> stored content of
     * response
     */
    public void sendResponse(Socket s, String status, String mime, Properties header_sendResponse, InputStream data_sendResponse) {
        try {
            if (status != null) {
                OutputStream out = s.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print("HTTP/1.1 " + status + " \r\n");
                if (mime != null) {
                    pw.print("Content-Type: " + mime + "\r\n");
                }

                if (header_sendResponse == null || header_sendResponse.getProperty("Date") == null) {
                    SimpleDateFormat utcFrmt = new SimpleDateFormat("yyyyMMddHHmmss");
                    utcFrmt.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    pw.print("Date: " + utcFrmt.format(new Date()) + "\r\n");
                }

                if (header_sendResponse != null) {
                    Enumeration e = header_sendResponse.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = header_sendResponse.getProperty(key);
                        pw.print(key + ": " + value + "\r\n");
                    }
                }

                pw.print("\r\n");
                pw.flush();

                if (data_sendResponse != null) {
                    byte[] buff_sendResponse = new byte[2048];
                    StringBuilder sbuf = new StringBuilder();
                    while (true) {
                        int read = data_sendResponse.read(buff_sendResponse, 0, 2048);
                        if (read <= 0) {
                            break;
                        }

                        int i;
                        for (i = 0; i < buff_sendResponse.length; i++) {
                            if (buff_sendResponse[i] == 0) {
                                break;
                            }
                        }
                        if (i > 0) {
                            sbuf.append(new String(buff_sendResponse, 0, i));
                        }

                        out.write(buff_sendResponse, 0, read);
                    }
                }

                out.flush();
                out.close();
                if (data_sendResponse != null) {
                    data_sendResponse.close();
                }
            }
        } catch (Exception ioe) {
            MyLog.Error("THREAD " + mId + ": Error respond to client: " + ioe.getMessage());
            MyLog.Error(ioe);
        } finally {
            try {
                s.close();

            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ProcessRequest.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void doUpdateDeleted(Map<String, List<String>> values) {
        List<String> userID = values.get("user_id");

        if (userID != null) {
            String userDeleted = userID.get(0);

            //update is_deleted_transaction
            dbInf.updateHisUserDeleted(userDeleted, GlobalVariables.SQL_UPDATE_USER_STATUS_TRANSACTION);
            logger.info("Update is_deleted table transaction_histoy");

            //update is_deleted_withdraw
            dbInf.updateHisUserDeleted(userDeleted, GlobalVariables.SQL_UPDATE_USER_STATUS_WITHDRAW);
            logger.info("Update is_deleted table withdraw_his");

            //update is_deleted_deposit
            dbInf.updateHisUserDeleted(userDeleted, GlobalVariables.SQL_UPDATE_USER_STATUS_DEPOSIT);
            logger.info("Update is_deleted table deposit_his");

            //update is_deleted_history
            dbInf.updateHisUserDeleted(userDeleted, GlobalVariables.SQL_UPDATE_USER_STATUS_HISTORY);
            logger.info("Update is_deleted table history");
        }
    }

    private void doGetOrderDetails(Socket s, Map<String, List<String>> values) {

        long start = System.currentTimeMillis();

        String listOrder = values.get("orders").get(0);
        if (listOrder == null || listOrder.trim().isEmpty()) {
            Response resp = new Response(GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "finish", null);
            Gson gson = new Gson();
            String response = gson.toJson(resp);
            sendResultToClient(s, response);
        } else {
            listOrder = listOrder.replace(" ", "");
            String[] arr = listOrder.split(",");
            List<OrderDetails> orders = dbInf.getOrderDetails(arr);

            OrderDetails[] arrOrders = new OrderDetails[orders.size()];
            arrOrders = orders.toArray(arrOrders);
            Response resp = new Response(GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "finish", arrOrders);

            Gson gson = new Gson();
            String response = gson.toJson(resp);
            sendResultToClient(s, response);
        }

        logger.info("Finish get Order detail in " + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * search history for user id
     *
     * @param s
     * @param values
     */
    private void doHistory(Socket s, Map<String, List<String>> values, String user_id) {
        List<String> lpage = values.get("page");
        String page;
        if (lpage == null) {
            page = "1";
        } else {
            page = lpage.get(0);
        }

        String per_page = values.get("per_page").get(0);
        int pageSize = 0;
        int pagePos = 0;
        try {
            pageSize = Integer.valueOf(per_page);
            pagePos = Integer.valueOf(page);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            sendResultToClient(s, "{\"status\":\"INVALID\", \"description\":\"invalid number input\"}");
            return;
        }

        List<String> lType = values.get("type");
        String type;
        if (lType == null) {
            type = "";
        } else {
            type = lType.get(0);
        }
//        String user_id = values.get("user_id").get(0);
        Date from = null;
        Date to = null;

        List<String> from_date = values.get("from_date");
        if (from_date != null) {
            if (from_date.get(0) != null) {
                try {
                    from = sdfHis.parse(from_date.get(0));
                } catch (ParseException ex) {
                    from = null;
                    logger.error(ex.getMessage(), ex);
                    sendResponse(s, HttpResponse.HTTP_BADREQUEST, "application/json", null, null);
                    return;
                }
            }
        }

        List<String> to_date = values.get("to_date");
        if (to_date != null) {
            if (to_date.get(0) != null) {
                try {
                    to = sdfHis.parse(to_date.get(0));
                } catch (ParseException ex) {
                    to = null;
                    logger.error(ex.getMessage(), ex);
                    sendResponse(s, HttpResponse.HTTP_BADREQUEST, "application/json", null, null);
                    return;
                }
            }
        }

        try {
            long period = to.getTime() - from.getTime();
            logger.debug("PERIOD ======= " + period + " and threshold =" + GlobalVariables.THRESHOLD_HIS + " minus = "
                    + (period - GlobalVariables.THRESHOLD_HIS));
            if (period > GlobalVariables.THRESHOLD_HIS) {
                logger.info("TIME TO GET HISTORY TOO LONG --> reject request");
                sendResponse(s, HttpResponse.HTTP_BADREQUEST, "application/json", null, null);
                return;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            sendResponse(s, HttpResponse.HTTP_BADREQUEST, "application/json", null, null);
            return;
        }

        switch (type.toUpperCase()) {
            case "": //get all pay , deposit and withdraw

                logger.debug("STARTING GET HISTORY FOR USER:" + user_id);
                List<History> total = dbInf.getHistory(user_id, from, to);
//                List<History> depHis = dbInf.getHisDeposit(user_id, from, to);
//                logger.info("get Deposit His success with length = " + depHis.size());
//                List<History> wdrHis = dbInf.getHisWithDraw(user_id, from, to);
//                logger.info("get WithDraw His success with length = " + wdrHis.size());
//                List<History> payHis = dbInf.getHisPay(user_id, from, to);
//                logger.info("get Pay His success with length = " + payHis.size());

//                List<History> total = new ArrayList<>();
//                total.addAll(depHis);
//                total.addAll(wdrHis);
//                total.addAll(payHis);
                logger.info("get total His success with length = " + total.size());

                //init pageable object
                Pageable pagesTotal = new Pageable(total);

                //set page size
                pagesTotal.setPageSize(pageSize);

                //set page position need to get
                pagesTotal.setPage(pagePos);

                //get page
                List<History> resultTotal = pagesTotal.getListForPage();

                String responseTotal = makeJsonHis(resultTotal, GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), pagePos, total.size(), pageSize);

                //send result to client
                sendResultToClient(s, responseTotal);

                break;
            case "DEPOSIT": //get his deposit
                try {
                    //select database to get all record
                    List<History> hisDeposit = dbInf.getHisDeposit(user_id, from, to);
                    logger.info("get Deposit His success with length = " + hisDeposit.size());
                    //init pageable object
                    Pageable pages = new Pageable(hisDeposit);

                    //set page size
                    pages.setPageSize(pageSize);

                    //set page position need to get
                    pages.setPage(pagePos);

                    //get page
                    List<History> result = pages.getListForPage();

                    String response = makeJsonHis(result, GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), pagePos, hisDeposit.size(), pageSize);

                    //send result to client
                    sendResultToClient(s, response);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    sendResultToClient(s, "{\"status\":\"ERROR\", \"description\":\"system error\"}");
                }
                break;
            case "WITHDRAW"://get his withdraw
                try {
                    //select database to get all record
                    List<History> hisWithdraw = dbInf.getHisWithDraw(user_id, from, to);
                    logger.info("get WithDraw His success with length = " + hisWithdraw.size());
                    //init pageable object
                    Pageable pages = new Pageable(hisWithdraw);

                    //set page size
                    pages.setPageSize(pageSize);

                    //set page position need to get
                    pages.setPage(pagePos);

                    //get page
                    List<History> result = pages.getListForPage();

                    String response = makeJsonHis(result, GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), pagePos, hisWithdraw.size(), pageSize);

                    //send result to client
                    sendResultToClient(s, response);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    sendResultToClient(s, "{\"status\":\"ERROR\", \"description\":\"system error\"}");
                }
                break;
            case "PAY": //get his pay
                try {
                    //select database to get all record
                    List<History> hisPay = dbInf.getHisPay(user_id, from, to);
                    logger.info("get Pay His success with length = " + hisPay.size());
                    //init pageable object
                    Pageable pages = new Pageable(hisPay);

                    //set page size
                    pages.setPageSize(pageSize);

                    //set page position need to get
                    pages.setPage(pagePos);

                    //get page
                    List<History> result = pages.getListForPage();

                    String response = makeJsonHis(result, GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), pagePos, hisPay.size(), pageSize);

                    //send result to client
                    sendResultToClient(s, response);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    sendResultToClient(s, "{\"status\":\"ERROR\", \"description\":\"system error\"}");
                }
                break;
            default: //invalid
                sendResultToClient(s, "{\"status\":\"INVALID\", \"description\":\"invalid input\"}");
                break;
        }
    }

    private void doReportRevenue(Socket s, Map<String, List<String>> values) {
        long start = System.currentTimeMillis();
        try {

//            int total = 0;
//            int sum_usd = 0;
//            int sum_eur = 0;
//            int sum_ava = 0;
            Wallet sumWallet = new Wallet(0, 0, 0);
            Coingate sumCoingate = new Coingate(0, 0, 0);

            String from = values.get("from").get(0);
            String to = values.get("to").get(0);

            Date fromDate = sdfHis.parse(from);
            Date toDate = sdfHis.parse(to);

            List<Object> list = dbInf.getRevenue(fromDate, toDate);

            list.stream().forEach((object) -> {
                if (object instanceof Wallet) {
                    Wallet wallet = (Wallet) object;
                    sumWallet.sum(wallet);

                } else if (object instanceof Coingate) {
                    Coingate coingate = (Coingate) object;
                    sumCoingate.sum(coingate);
                } else {
                    //do nothing
                }
            });

            Detail detail = new Detail(sumCoingate, sumWallet, null);
            Revenue revenue = new Revenue(list.size(), detail);
            RevenueResponse response = new RevenueResponse(GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "ok", revenue);

            Gson gson = new Gson();
            String json = gson.toJson(response);
            logger.info("Send response to client:" + json);
            sendResultToClient(s, json);

        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ProcessRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String makeJsonHis(List<History> result, String status, int pagePosition, int total, int pageSize) {
        Gson gson = new Gson();
        Meta meta = new Meta(pagePosition, total, pageSize);
        History[] his = result.toArray(new History[result.size()]);

        HistoryResponse response = new HistoryResponse(status, "finish", meta, his);
        return gson.toJson(response);
    }

    private void doWithdrawClientNotify(Socket s, String postLine) {

        logger.info("/payment/wallet/withdraw-request ...........");
        task = decodeParms(postLine, API_TYPE_WITHDRAW_REQUEST_NOTIFY);
        WithdrawClientNotifyTask notify = (WithdrawClientNotifyTask) task;

        double[] arr = dbInf.checkLimit(notify);

        int check = (int) arr[0];
        double sum = arr[1];

        switch (check) {
            case 0: //ok
                double balance = dbInf.getBalance(notify.getUser_id(), notify.getCurrency());
                try {
                    double amount = Double.valueOf(notify.getAmount());
                    if (amount > balance) {
                        logger.info("user " + notify.getUser_id() + " is not enough balance. Balance:" + balance);

                        sendResultToClient(s, "failed", GlobalVariables.MSG_NOT_ENOUGH_BALANCE);
//                        dbInf.insertHistory(notify.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL,
//                                Double.valueOf(notify.getAmount()),
//                                new Timestamp(Long.valueOf(notify.getCreated_at())),
//                                "", notify.getTo_address(), "WITHDRAW".toUpperCase(), notify.getUser_id(),
//                                new Timestamp(Long.valueOf(notify.getExpired_time())), notify.getCurrency(),
//                                null, IDgenerator.getInstance().genID());
                        return;
                    } else {
                        sendResultToClient(s, GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "ok");
                        dbInf.insertHistory(notify.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_START,
                                Double.valueOf(notify.getAmount()),
                                new Timestamp(Long.valueOf(notify.getCreated_at())),
                                "", notify.getTo_address(), "WITHDRAW".toUpperCase(), notify.getUser_id(),
                                new Timestamp(Long.valueOf(notify.getExpired_time())), notify.getCurrency(),
                                null, IDgenerator.getInstance().genID());
                        return;
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    sendResultToClient(s, "failed", "invalid input");
//                    dbInf.insertHistory(notify.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL,
//                            Double.valueOf(notify.getAmount()),
//                            new Timestamp(Long.valueOf(notify.getCreated_at())),
//                            "", notify.getTo_address(), "WITHDRAW".toUpperCase(), notify.getUser_id(),
//                            new Timestamp(Long.valueOf(notify.getExpired_time())), notify.getCurrency(),
//                            null, IDgenerator.getInstance().genID());
                    return;
                }

            case 1: //check not OK --> response user limited
                String msg = GlobalVariables.MSG_LIMITED_WITHDRAW.replace("#WTDRAWED#", sum + "")
                        .replace("#MAXIMUM#", notify.getMax_per_day());

                logger.info("User ID:" + notify.getUser_id() + " limited per day:" + msg);
                sendResultToClient(s, "limited", msg);
//                dbInf.insertHistory(notify.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL,
//                        Double.valueOf(notify.getAmount()),
//                        new Timestamp(Long.valueOf(notify.getCreated_at())),
//                        "", notify.getTo_address(), "WITHDRAW".toUpperCase(), notify.getUser_id(),
//                        new Timestamp(Long.valueOf(notify.getExpired_time())), notify.getCurrency(),
//                        null, IDgenerator.getInstance().genID());
                return;
            case 2: //invalid limit input --> response error
                logger.info("max per day invalid:" + notify.getMax_per_day());
                sendResultToClient(s, "invalid", "invalid max setting");
//                dbInf.insertHistory(notify.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL,
//                        Double.valueOf(notify.getAmount()),
//                        new Timestamp(Long.valueOf(notify.getCreated_at())),
//                        "", notify.getTo_address(), "WITHDRAW".toUpperCase(), notify.getUser_id(),
//                        new Timestamp(Long.valueOf(notify.getExpired_time())), notify.getCurrency(),
//                        null, IDgenerator.getInstance().genID());
                return;
            case -1: //system error
                logger.info("System ERROR:");
                sendResultToClient(s, "error", "system error");
//                dbInf.insertHistory(notify.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_FAIL,
//                        Double.valueOf(notify.getAmount()),
//                        new Timestamp(Long.valueOf(notify.getCreated_at())),
//                        "", notify.getTo_address(), "WITHDRAW".toUpperCase(), notify.getUser_id(),
//                        new Timestamp(Long.valueOf(notify.getExpired_time())), notify.getCurrency(),
//                        null, IDgenerator.getInstance().genID());
        }

//        logger.info("STARTING INSERT DATABASE HISTORY FOR TRANSACTION WITHDRAW REQUEST:" + notify.getRequest_id());
//        dbInf.insertHistory(notify.getRequest_id(), GlobalVariables.TRANSACTION_STATUS_START,
//                Double.valueOf(notify.getAmount()),
//                new Timestamp(Long.valueOf(notify.getCreated_at())),
//                "", notify.getTo_address(), "WITHDRAW".toUpperCase(), notify.getUser_id(),
//                new Timestamp(Long.valueOf(notify.getExpired_time())), notify.getCurrency(),
//                null, IDgenerator.getInstance().genID());
    }

    private void doBookResponse(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        task = decodeParms(s, postLine, API_TYPE_BOOK_RESPONSE);

        BookResponseTask bookRspTask = (BookResponseTask) task;
        Object object = GlobalObject.getInstance().getHashOrder().get(bookRspTask.getOrder_id());

        String status = bookRspTask.getStatus();
        if (object instanceof PayTaskWrapper) {
            PayTask pTask = ((PayTaskWrapper) object).getPaytask();
            if (pTask == null) {
                logger.info("don't know order id:" + bookRspTask.getOrder_id());
                return;
            }

            if (status.equalsIgnoreCase("BOOKING_SUCCESS")) {
                dbInf.updateStatusHistory(pTask.getBooking_number(), GlobalVariables.TRANSACTION_STATUS_SUCCESS, "PAY");

                //send notfify
                notify(pTask.getUser_id());
            } else {
                dbInf.updateStatusHistory(pTask.getBooking_number(), GlobalVariables.TRANSACTION_STATUS_FAIL, "PAY");

                //refund
                dbInf.updateBalance(pTask.getUser_id(), pTask.getPay_currency(), Double.valueOf(pTask.getPay_amount()));
                dbInf.insertHistory(pTask.getBooking_number(), GlobalVariables.TRANSACTION_STATUS_SUCCESS,
                        Double.valueOf(pTask.getPay_amount()),
                        new Timestamp(System.currentTimeMillis()), "", "",
                        "REFUND", pTask.getUser_id(), null, pTask.getPay_currency(),
                        ((PayTaskWrapper) object).getTransID(),
                        IDgenerator.getInstance().genID());
            }

            //remove object
            GlobalObject.getInstance().getHashOrder().remove(bookRspTask.getOrder_id());
        } else if (object instanceof CreatePaymentTask) {
            CreatePaymentTask paymentPaypalTask = (CreatePaymentTask) object;

            if (!status.equalsIgnoreCase("BOOKING_SUCCESS")) {
                //book fail --> refund 
                String urlRefund = paymentPaypalTask.getRefund_url();
                if (urlRefund != null && !urlRefund.equalsIgnoreCase("")) {

                    logger.info("REFUND's URL = " + urlRefund);

                    String refundResult = client.reFund(urlRefund);
                    Gson gson = new Gson();

                    RefundResponse refundRsp = gson.fromJson(refundResult, RefundResponse.class);
                    dbInf.insertOrderHist(paymentPaypalTask, "refund_" + refundRsp.getState(), null);
                } else {
                    logger.info("refund URL isn't found --> please check");
                }

                if (status.equalsIgnoreCase("EXPIRED")) {
                    String id = dbInf.getIDfromHis(bookRspTask.getOrder_id(), GlobalVariables.TRANSACTION_TYPE_PAYPAL);
                    if (id != null && !id.equalsIgnoreCase("")) {
                        dbInf.updateStatusHistory(id, GlobalVariables.TRANSACTION_STATUS_EXPIRED, GlobalVariables.TRANSACTION_TYPE_PAYPAL);
                    } else {
                        logger.info("ID is invalid :" + id);
                    }
                }
            }

            //remove object
            GlobalObject.getInstance().getHashOrder().remove(bookRspTask.getOrder_id());
        } else {
            logger.info("[" + postLine + "]no transaction waiting for process");
        }

        logger.info("Finish do booking response in " + (System.currentTimeMillis() - sta) + " ms");
    }

    private void doCreatePaymentToPaypal(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        task = decodeParms(s, postLine, API_TYPE_CREATE_PAYMENT_PAYPAL);
        CreatePaymentTask paymentPaypalTask = (CreatePaymentTask) task;

        Gson gson = new Gson();
        String request = gson.toJson(paymentPaypalTask.birhChild());
        String response = client.createPayment(request);

        if (response == null) {
            PaymentClientResponse clientResp = new PaymentClientResponse(GlobalVariables.TRANSACTION_STATUS_FAIL.toLowerCase(), "The service is currently busy. Please choose another payment method.", new Data(paymentPaypalTask.getOrder_id()));
            String respString = gson.toJson(clientResp);

            sendResultToClient(s, respString);
        }
        CreatePaymentResponse createResponse = gson.fromJson(response, CreatePaymentResponse.class);

        String id = createResponse.getId();
        String exeUrl = null;

        //get link from response
        List<Link> links = createResponse.getLinks();
        for (Link link : links) {
            if (link.getMethod().equalsIgnoreCase("post")) {
                exeUrl = link.getHref();
            }
        }

        if (exeUrl != null) {
            logger.info("ID = " + id);
            logger.info("exeUrl = " + exeUrl);
            logger.info("orderID = " + paymentPaypalTask.getOrder_id());

            PaypalPayment p = new PaypalPayment(id, paymentPaypalTask.getOrder_id(), exeUrl);
            //push into hashmap
            GlobalObject.getInstance().getHashPaypalExeLink().put(id, p);
            GlobalObject.getInstance().getHashOrder().put(paymentPaypalTask.getOrder_id(), paymentPaypalTask);

            //create and send response to client
            PaymentClientResponse clientResp = new PaymentClientResponse(GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "ok", new Data(id));
            String respString = gson.toJson(clientResp);

            sendResultToClient(s, respString);
            logger.info("send result to client: " + respString);
        } else {
            logger.warn("can't get execute payment url  --> please check");

            //create and send response to client
            PaymentClientResponse clientResp = new PaymentClientResponse("fail", "can't get execute url from paypal", new Data(id));
            String respString = gson.toJson(clientResp);
            sendResultToClient(s, respString);
            logger.info("send result to client: " + respString);
        }

        //insert into history
        dbInf.insertHistory(id, GlobalVariables.TRANSACTION_STATUS_PENDING, paymentPaypalTask.getTransactions().get(0).getAmount().getTotal(),
                new Timestamp(System.currentTimeMillis()), null, null, GlobalVariables.TRANSACTION_TYPE_PAYPAL, paymentPaypalTask.getUser_id(), null, paymentPaypalTask.getTransactions().get(0).getAmount().getCurrency(),
                paymentPaypalTask.getOrder_id(), IDgenerator.getInstance().genID());

        //insert into transaction history
        dbInf.insertOrderHist(paymentPaypalTask, createResponse.getState(), null);

        logger.info("Finish doCreatePaymentToPaypal in " + (System.currentTimeMillis() - sta) + " ms");
    }

    private void doExecutePaymentToPaypal(Socket s, String postLine) {
        long sta = System.currentTimeMillis();
        Gson gson = new Gson();
        task = decodeParms(s, postLine, API_TYPE_EXECUTE_PAYMENT_PAYPAL);
        ExecutePaymentTask exePaymentTask = (ExecutePaymentTask) task;

        //check current status
        String currentStatus = dbInf.getStatusFromHis(exePaymentTask.getPayment_id(), GlobalVariables.TRANSACTION_TYPE_PAYPAL);
        if (currentStatus.equalsIgnoreCase(GlobalVariables.TRANSACTION_STATUS_FAIL) || currentStatus.equalsIgnoreCase(GlobalVariables.TRANSACTION_STATUS_EXPIRED)) {
            //current order id is note available
            logger.info("Order id:" + exePaymentTask.getPayment_id() + " is " + currentStatus);

            PaymentClientResponse respToClient = new PaymentClientResponse(GlobalVariables.TRANSACTION_STATUS_EXPIRED.toLowerCase(), "fail", null);

            //make json response
            String rsp = gson.toJson(respToClient);

            //send result to client
            sendResultToClient(s, rsp);
            return;
        }

        String paymentId = exePaymentTask.getPayment_id();
        String payerID = exePaymentTask.getPayer_id();

        //get url execute payment menthod
        String exeUrl = GlobalObject.getInstance().getHashPaypalExeLink().get(paymentId).getExeUrl();

        ExecutePayment exe = new ExecutePayment(payerID);
        String request = gson.toJson(exe);

        ExeResponse response = client.exePayment(request, exeUrl);
        if (response == null) {
            return;
        }

        String result = response.getResponse();
        int status = response.getStatus();
        if (200 <= status && status < 300) {
            //status ok
            ExecutePaymentResponse exeResp = gson.fromJson(result, ExecutePaymentResponse.class);
            List<com.digitechlabs.paymentgw.paypal.execute.response.Link> link = exeResp.getTransactions().get(0).getRelatedResources().get(0).getSale().getLinks();

            String urlRefund = null;
            for (com.digitechlabs.paymentgw.paypal.execute.response.Link l : link) {
                logger.info(l.getMethod() + " --> " + l.getRel() + " --> " + l.getHref());
                if (l.getRel().equalsIgnoreCase("refund")) {
                    urlRefund = l.getHref();
                    logger.info("[" + exeResp.getTransactions().get(0).getRelatedResources().get(0).getSale().getId() + "] + refund URL: " + urlRefund);
                    String parentPaymentID = exeResp.getTransactions().get(0).getRelatedResources().get(0).getSale().getParentPayment();
                    String orderID = GlobalObject.getInstance().getHashPaypalExeLink().get(parentPaymentID).getOrderID();

                    //set url refund
                    Object obj = GlobalObject.getInstance().getHashOrder().get(orderID);
                    if (obj instanceof CreatePaymentTask) {
                        CreatePaymentTask payment = (CreatePaymentTask) obj;
                        payment.setRefund_url(urlRefund);
                        GlobalObject.getInstance().getHashOrder().put(orderID, payment);

                        //debug
                        logger.info("URL REFUND:" + ((CreatePaymentTask) GlobalObject.getInstance().getHashOrder().get(orderID)).getRefund_url());
                    }
                }
            }

            PaymentClientResponse respToClient = new PaymentClientResponse(GlobalVariables.TRANSACTION_STATUS_SUCCESS.toLowerCase(), "ok", null);

            //make json response
            String rsp = gson.toJson(respToClient);

            //send result to client
            sendResultToClient(s, rsp);

            //insert DB using exeResp
            String orderID = GlobalObject.getInstance().getHashPaypalExeLink().get(paymentId).getOrderID();
            Object o = GlobalObject.getInstance().getHashOrder().get(orderID);
            if (o instanceof CreatePaymentTask) {
                CreatePaymentTask cpTask = (CreatePaymentTask) o;
                dbInf.insertOrderHist(cpTask, exeResp.getState(), null);
            }
        } else if (status == 400) {
            //status fail
            ExecutePaymentResponse exeResp = gson.fromJson(result, ExecutePaymentResponse.class);
            PaymentClientResponse respToClient = new PaymentClientResponse(GlobalVariables.TRANSACTION_STATUS_FAIL.toLowerCase(), "fail", null);

            //make json response
            String rsp = gson.toJson(respToClient);

            //send result to client
            sendResultToClient(s, rsp);

            //insert DB using exeResp
            String orderID = GlobalObject.getInstance().getHashPaypalExeLink().get(paymentId).getOrderID();
            Object o = GlobalObject.getInstance().getHashOrder().get(orderID);
            if (o instanceof CreatePaymentTask) {
                CreatePaymentTask cpTask = (CreatePaymentTask) o;
                dbInf.insertOrderHist(cpTask, GlobalVariables.TRANSACTION_STATUS_CANCEL, null);
                String id = exeResp.getTransactions().get(0).getRelatedResources().get(0).getSale().getParentPayment();
                dbInf.updateStatusHistory(id, GlobalVariables.TRANSACTION_STATUS_FAIL, GlobalVariables.TRANSACTION_TYPE_PAYPAL);
            }
        } else {
            logger.info("ignore");
        }

//        CreatePaymentResponse createResponse = gson.fromJson(response, CreatePaymentResponse.class);
        logger.info("Finish doExecutePaymentToPaypal in " + (System.currentTimeMillis() - sta) + " ms");
    }

    private void doPaypalCallback(Socket s, String postLine, Properties header) {
        long sta = System.currentTimeMillis();
        sendResultToClient(s, "ok");

        Gson gson = new Gson();
        try {
            logger.info("Starting resolve call back");
            task = decodeParms(s, postLine, API_TYPE_PAYPAL_CALLBACK);

            PaypalCallbackTask paymentPaypalTask = (PaypalCallbackTask) task;
            String parentID = paymentPaypalTask.getResource().getParentPayment();
            String id = paymentPaypalTask.getResource().getParentPayment();

            logger.info("id parent= " + id);

            PaypalPayment oPP = GlobalObject.getInstance().getHashPaypalExeLink().get(parentID);
            if (oPP == null) {
                //ignore it
                logger.info("ignore paypal callback");
                return;
            }
//            else {
//                GlobalObject.getInstance().getHashPaypalExeLink().remove(parentID);
//            }

            String orderID = oPP.getOrderID();

            String PAYPAL_TRANSMISSION_SIG = header.getProperty(Constants.PAYPAL_HEADER_TRANSMISSION_SIG.toLowerCase());
            String PAYPAL_AUTH_ALGO = header.getProperty(Constants.PAYPAL_HEADER_AUTH_ALGO.toLowerCase());
            String PAYPAL_CERT_URL = header.getProperty(Constants.PAYPAL_HEADER_CERT_URL.toLowerCase());
            String webhook_trustCert = header.getProperty(Constants.PAYPAL_TRUST_CERT_URL.toLowerCase());

            String transmissionId = header.getProperty(Constants.PAYPAL_HEADER_TRANSMISSION_ID.toLowerCase());
            String transmissionTime = header.getProperty(Constants.PAYPAL_HEADER_TRANSMISSION_TIME.toLowerCase());

            //create header for verify
            HashMap<String, String> headers = new HashMap<>();
            headers.put(Constants.PAYPAL_HEADER_TRANSMISSION_SIG, PAYPAL_TRANSMISSION_SIG);
            headers.put(Constants.PAYPAL_HEADER_AUTH_ALGO, PAYPAL_AUTH_ALGO);
            headers.put(Constants.PAYPAL_HEADER_CERT_URL, PAYPAL_CERT_URL);
            headers.put(Constants.PAYPAL_TRUST_CERT_URL, webhook_trustCert);
            headers.put(Constants.PAYPAL_HEADER_TRANSMISSION_ID, transmissionId);
            headers.put(Constants.PAYPAL_HEADER_TRANSMISSION_TIME, transmissionTime);

            //create context
//            APIContext context = new APIContext(ConfigLoader.getInstance().getPaypalClientID(), ConfigLoader.getInstance().getPaypalSecret(), "sandbox");
            APIContext context = new APIContext(ConfigLoader.getInstance().getPaypalClientID(), ConfigLoader.getInstance().getPaypalSecret(), Constants.LIVE);
            context.addConfiguration(Constants.PAYPAL_WEBHOOK_ID, ConfigLoader.getInstance().getWebhookId());

            //validate header
            boolean result = Event.validateReceivedEvent(context, headers, postLine);

            logger.info("result of check:" + result);
            if (result) {
                //check status
                String event_type = paymentPaypalTask.getEventType();
                Object o = GlobalObject.getInstance().getHashOrder().get(orderID);
                CreatePaymentTask cpTask;
                if (o instanceof CreatePaymentTask) {
                    cpTask = (CreatePaymentTask) o;
                } else {
                    logger.info("???????????????????? --> hiden risk --> Please Check");
                    return;
                }

                switch (event_type) {
                    case GlobalVariables.PAYPAL_STATUS_PAYMENT_COMPLETE:
                        //check current status
                        String currentStatus = dbInf.getStatusFromHis(id, GlobalVariables.TRANSACTION_TYPE_PAYPAL);
                        if (currentStatus.equalsIgnoreCase(GlobalVariables.TRANSACTION_STATUS_FAIL) || currentStatus.equalsIgnoreCase(GlobalVariables.TRANSACTION_STATUS_EXPIRED)) {
                            //current order id is note available
                            logger.info("Order id:" + orderID + " is " + currentStatus);
                            //get refund url: 
                            List<com.digitechlabs.paymentgw.paypal.callback.Link> links = paymentPaypalTask.getResource().getLinks();
                            String refundUrl = null;
                            for (com.digitechlabs.paymentgw.paypal.callback.Link link : links) {
                                if (link.getRel().equalsIgnoreCase("refund")) {
                                    refundUrl = link.getHref();
                                }
                            }

                            if (refundUrl != null) {
                                String refundResult = client.reFund(refundUrl);

                                RefundResponse refundRsp = gson.fromJson(refundResult, RefundResponse.class);
                                dbInf.insertOrderHist(cpTask, "refund_" + refundRsp.getState(), null);
                            }

                        } else {
                            //comple --> notify booking paysuccess
                            Main.getInstance().getNotifyQueue().enqueue(orderID);
                            GlobalObject.getInstance().getHashPaypalExeLink().remove(parentID);
                            logger.info("[" + orderID + "] Payment Success");
                            if (o instanceof CreatePaymentTask) {
                                String json = createJsonRevenue(cpTask.getTransactions().get(0).getAmount().getTotal(), cpTask.getTransactions().get(0).getAmount().getCurrency());
                                dbInf.insertOrderHist(cpTask, event_type, json);
                            }

                            dbInf.updateStatusHistory(id, GlobalVariables.TRANSACTION_STATUS_SUCCESS, GlobalVariables.TRANSACTION_TYPE_PAYPAL);
                        }
                        break;
                    case GlobalVariables.PAYPAL_STATUS_PAYMENT_PENDING:
                        //do pending
                        logger.info("[" + orderID + "]TRANSACTION PENDING .... ");
                        dbInf.insertOrderHist(cpTask, event_type, null);
                        break;

                    case GlobalVariables.PAYPAL_STATUS_REFUND_COMPLETE:
                        //refund success
//                        dbInf.updateStatusHistory(id, GlobalVariables.TRANSACTION_STATUS_, GlobalVariables.TRANSACTION_TYPE_PAYPAL);
                        break;

                    default:
                        // notify payment fail
                        GlobalObject.getInstance().getHashPaypalExeLink().remove(parentID);
                        NotifyBookFail notify = new NotifyBookFail(orderID);
                        String resp = gson.toJson(notify);
                        client.notifyBookFail(resp);

                        dbInf.insertOrderHist(cpTask, event_type, null);
                        dbInf.updateStatusHistory(id, GlobalVariables.TRANSACTION_STATUS_FAIL, GlobalVariables.TRANSACTION_TYPE_PAYPAL);

                        break;
                }
            } else {
                logger.warn("call back problem -> please check");
            }
        } catch (PayPalRESTException | InvalidKeyException | NoSuchAlgorithmException | SignatureException ex) {
            logger.error(ex.getMessage(), ex);
        }

        logger.info("finish do callback from paypal in " + (System.currentTimeMillis() - sta) + " ms");
    }

    private void doFunding(Socket s, String postLine, Properties header) {

        long star = System.currentTimeMillis();

        //check token
        String autho = header.getProperty("authorization");
        if (autho == null) {
            logger.info("Authorization header not found");
            sendError(s, "401", "invalid", "0");
            return;
        } else {
            String private_key = "private_key " + ConfigLoader.getInstance().getPrivateKey();

            if (!autho.equals(private_key)) {
                logger.info("Authorization header invalid:" + autho);
                sendError(s, "401", "invalid", "0");
                return;
            }
        }

        Gson gson = new Gson();

        logger.info("Starting resolve funding");
        task = decodeParms(s, postLine, API_TYPE_FUNDING);
        String transactionid = IDgenerator.getInstance().genID();
        //resolve task
        if (task instanceof FundingTask) {
            try {
                FundingTask fundingTask = (FundingTask) task;

                String id = fundingTask.getId();
                String userID = fundingTask.getUser_id();
                String fund = fundingTask.getFund().trim();
                String currency = fundingTask.getCurrency().toUpperCase();
                String transaction_type = fundingTask.getTransaction_type().toUpperCase();
                String reason = fundingTask.getReason();

                if (userID.equals("") || fund.equals("") || currency.equals("") || transaction_type.equals("")) {
                    logger.info("input is invalid --> please check");

                    com.digitechlabs.paymentgw.funding.Response resp = new com.digitechlabs.paymentgw.funding.Response("fail", "invalid input, check your body request");
                    sendResultToClient(s, gson.toJson(resp));

                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(fund);

                    if (Math.abs(amount) > ConfigLoader.getInstance().getMaxFunding()) {
                        //over max funding require.
                        logger.info("funding amout over maximum --> expect < " + ConfigLoader.getInstance().getMaxFunding() + " actual:" + amount);
                        com.digitechlabs.paymentgw.funding.Response resp = new com.digitechlabs.paymentgw.funding.Response("fail", "funding amount is too much");
                        sendResultToClient(s, gson.toJson(resp));

                        //insert db:
                        dbInf.insertHistory(id, STATUS_FAILED, -1, new Timestamp(System.currentTimeMillis()),
                                "", "", transaction_type, userID, null, currency, reason, transactionid);

                        return;
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    com.digitechlabs.paymentgw.funding.Response resp = new com.digitechlabs.paymentgw.funding.Response("fail", "invalid format of fund");
                    sendResultToClient(s, gson.toJson(resp));

                    //insert db:
                    dbInf.insertHistory(id, STATUS_FAILED, -1, new Timestamp(System.currentTimeMillis()),
                            "", "", transaction_type, userID, null, currency, reason, transactionid);
                    //end job
                    return;
                }

                int result = dbInf.updateBalance(userID, currency, amount);
                switch (result) {
                    case 0: //success
                        com.digitechlabs.paymentgw.funding.Response resp = new com.digitechlabs.paymentgw.funding.Response("true", "success");
                        sendResultToClient(s, gson.toJson(resp));

                        //insert db:
                        dbInf.insertHistory(id, STATUS_SUCCESS, amount, new Timestamp(System.currentTimeMillis()),
                                "", "", transaction_type, userID, null, currency, reason, transactionid);
                        break;
                    case 1: //system error
                        com.digitechlabs.paymentgw.funding.Response respError = new com.digitechlabs.paymentgw.funding.Response("fail", "system error");
                        sendResultToClient(s, gson.toJson(respError));

                        //insert db
                        dbInf.insertHistory(id, STATUS_FAILED, amount, new Timestamp(System.currentTimeMillis()),
                                "", "", transaction_type, userID, null, currency, reason, transactionid);
                        break;
                    case -1: //user not found
                        com.digitechlabs.paymentgw.funding.Response respUserNotFound = new com.digitechlabs.paymentgw.funding.Response("fail", "user not found");
                        sendResultToClient(s, gson.toJson(respUserNotFound));

                        //insert db
                        dbInf.insertHistory(id, STATUS_FAILED, amount, new Timestamp(System.currentTimeMillis()),
                                "", "", transaction_type, userID, null, currency, reason, transactionid);

                        break;
                    default://do nothing
                        break;
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);

                logger.info("input is invalid --> please check");

                com.digitechlabs.paymentgw.funding.Response resp = new com.digitechlabs.paymentgw.funding.Response("fail", "invalid input, check your body request");
                sendResultToClient(s, gson.toJson(resp));

                return;
            }
        } else {
            logger.error("isn't fungding task --> Please check");
        }

        logger.info("finish funding task in " + (System.currentTimeMillis() - star) + " ms");
    }

//    public HistoryWallet createHisWalletObject(String id, String status, double amount,
//            Timestamp created_at, String from_address, String to_address,
//            String transaction_type, String user_id, Timestamp expired_time, String currency, String order_id, String transaction_id) {
//
//        HistoryWallet hw = new HistoryWallet();
//
//        hw.setId(id);
//        hw.setStatus(status);
//        hw.setAmount(amount);
//        hw.setCreated_at(created_at);
//        hw.setFrom_address(from_address);
//        hw.setTo_address(to_address);
//        hw.setTransaction_type(transaction_type);
//        hw.setUser_id(user_id);
//        hw.setExpired_time(expired_time);
//        hw.setCurrency(currency);
//        hw.setOrder_id(order_id);
//        hw.setTransaction_id(transaction_id);
//
//        return hw;
//    }
}

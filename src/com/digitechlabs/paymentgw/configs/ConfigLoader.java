package com.digitechlabs.paymentgw.configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import utils.Config;
import utils.ProcessThreadMX;

public class ConfigLoader extends ProcessThreadMX {

    private static ConfigLoader instance;
    long count = 0;

    private String mainConfigFile = "payment.conf";
    private final String rapidQueueFile = "rabbit_queue.conf";
    private final String walletFile = "wallet_client.conf";
    private final String paypalFile = "paypal_client.conf";
    private final String notifyBookFailFile = "notify_book_fail.conf";
    private String urlOrder = "https://api-sandbox.coingate.com/v2/orders";
    private String serviceUrl = "http://trav-service-providers:8000/services/hotels-pro-booking/";
    private String serviceKey = "DHVHX8XGM6A595PRELJahSwDdWHDWeWq9SGQZagprcXdhtkSKG8k34N92s8C3QAq";
    private String callbackUrl = "http://api-dev.travala.com/payment/callback";
    private String appToken = "Token PPJpSoWyma12J8WZY11wzeypWewuse4tbXZbes1L";
    private String appContentType = "application/x-www-form-urlencoded";
    private String privateKey = "X8XGM6AWHDWeN92sELJahSwDdWHDWe";

    private String queueName = "queue-booking-room";
    private String routingKey = "make_room_booking";
    private String brokerURI = "amqp://travala:travala%40123@localhost:5672/trav_vhost";

    private String walletURL;
    private String walletToken;
    private String[] acceptConfirming;

    private String paypalUrlGetToken;
    private String paypalUrlCreatePayment;
    private String paypalSecret;
    private String paypalClientID;
    private String allowPaymentMethod;
    private String webhookId;

    private String notifyBookFailUrl;
    private String notifyBookFailHeader;
    private String notifyBookFailPrivatekey;

    public static synchronized ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader("config");
        }
        return instance;
    }

    public ConfigLoader(String threadName) {
        super(threadName);
        loadConfig();
    }

    private void loadConfig() {
        try {
            //load config File
            File mainFile = new File(Config.getConfigDir() + File.separator + mainConfigFile);
            File rabbitFile = new File(Config.getConfigDir() + File.separator + rapidQueueFile);
            File wallet = new File(Config.getConfigDir() + File.separator + this.walletFile);
            File paypal = new File(Config.getConfigDir() + File.separator + this.paypalFile);
            File bookfail = new File(Config.getConfigDir() + File.separator + this.notifyBookFailFile);

            Properties prop = new Properties();
            prop.load(new FileInputStream(mainFile));
            prop.load(new FileInputStream(rabbitFile));
            prop.load(new FileInputStream(wallet));
            prop.load(new FileInputStream(paypal));
            prop.load(new FileInputStream(bookfail));
            this.urlOrder = prop.getProperty("coingate.url.order").trim();
            this.serviceUrl = prop.getProperty("servicegw.url").trim();
            this.serviceKey = prop.getProperty("servicegw.key").trim();
            this.callbackUrl = prop.getProperty("callback.url").trim();
            this.appToken = prop.getProperty("coingate.app.token");
            this.appContentType = prop.getProperty("coingate.app.contenttype");
            this.privateKey = prop.getProperty("private.key");

            this.queueName = prop.getProperty("queue.name").trim();
            this.routingKey = prop.getProperty("routing.key").trim();
            this.brokerURI = prop.getProperty("broker.uri").trim();

            this.walletURL = prop.getProperty("wallet.url");
            this.walletToken = prop.getProperty("wallet.token");

            String temp = prop.getProperty("accept.confirming");

            while (temp.contains(" ")) {
                temp = temp.replace(" ", "");
            }
            acceptConfirming = temp.split(",");

            this.paypalUrlGetToken = prop.getProperty("paypal.url.gettoken").trim();
            this.paypalUrlCreatePayment = prop.getProperty("paypal.url.createpayment").trim();
            this.paypalSecret = prop.getProperty("paypal.secret").trim();
            this.paypalClientID = prop.getProperty("paypal.clientid").trim();
            this.allowPaymentMethod = prop.getProperty("allowed.payment.method");
            this.webhookId = prop.getProperty("webhook.id");

            this.notifyBookFailUrl = prop.getProperty("notify.book.fail.url");
            this.notifyBookFailHeader = prop.getProperty("notify.book.fail.header");
            this.notifyBookFailPrivatekey = prop.getProperty("notify.book.fail.privatekey");

        } catch (IOException ex) {
            logger.error("ERR: " + ex.getMessage() + " --> please try config again", ex);
        } catch (Exception e) {
            logger.error("ERR: " + e.getMessage() + " --> please try config again", e);
        }
    }

    static InetAddress ip() throws SocketException {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        NetworkInterface ni;
        while (nis.hasMoreElements()) {
            ni = nis.nextElement();
            if (!ni.isLoopback()/*not loopback*/ && ni.isUp()/*it works now*/) {
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    //filter for ipv4/ipv6
                    if (ia.getAddress().getAddress().length == 4) {
                        //4 for ipv4, 16 for ipv6
                        return ia.getAddress();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void process() {

        if (count % 30 == 0) {
            reload();
        }

        try {
            Thread.sleep(1000);

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (count > 99999999) {
            count = 0;
        }
    }

    private boolean reload() {
        boolean isReloadOK = false;
        loadConfig();

        logger.info("Reload Config .. OK");
        return isReloadOK;
    }

    public String getConfigFile() {
        return mainConfigFile;
    }

    public void setConfigFile(String configFile) {
        this.mainConfigFile = configFile;
    }

    public String getUrlOrder() {
        return urlOrder;
    }

    public void setUrlOrder(String urlOrder) {
        this.urlOrder = urlOrder;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getAppToken() {
        return appToken;
    }

    public String getAppContentType() {
        return appContentType;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getBrokerURI() {
        return brokerURI;
    }

    public void setBrokerURI(String brokerURI) {
        this.brokerURI = brokerURI;
    }

    public String getWalletURL() {
        return walletURL;
    }

    public void setWalletURL(String walletURL) {
        this.walletURL = walletURL;
    }

    public String getWalletToken() {
        return walletToken;
    }

    public void setWalletToken(String walletToken) {
        this.walletToken = walletToken;
    }

    public String[] getAcceptConfirming() {
        return acceptConfirming;
    }

    public String getPaypalUrlGetToken() {
        return paypalUrlGetToken;
    }

    public void setPaypalUrlGetToken(String paypalUrlGetToken) {
        this.paypalUrlGetToken = paypalUrlGetToken;
    }

    public String getPaypalUrlCreatePayment() {
        return paypalUrlCreatePayment;
    }

    public void setPaypalUrlCreatePayment(String paypalUrlCreatePayment) {
        this.paypalUrlCreatePayment = paypalUrlCreatePayment;
    }

    public String getPaypalSecret() {
        return paypalSecret;
    }

    public void setPaypalSecret(String paypalSecret) {
        this.paypalSecret = paypalSecret;
    }

    public String getPaypalClientID() {
        return paypalClientID;
    }

    public void setPaypalClientID(String paypalClientID) {
        this.paypalClientID = paypalClientID;
    }

    public String getAllowPaymentMethod() {
        return allowPaymentMethod;
    }

    public void setAllowPaymentMethod(String allowPaymentMethod) {
        this.allowPaymentMethod = allowPaymentMethod;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public String getNotifyBookFailUrl() {
        return notifyBookFailUrl;
    }

    public String getNotifyBookFailHeader() {
        return notifyBookFailHeader;
    }

    public String getNotifyBookFailPrivatekey() {
        return notifyBookFailPrivatekey;
    }

}

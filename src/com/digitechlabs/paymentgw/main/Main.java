package com.digitechlabs.paymentgw.main;

import com.digitechlabs.paymentgw.soap.HttpListener;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import com.digitechlabs.paymentgw.configs.MyLog;
import com.digitechlabs.paymentgw.currency.SyncRateProcess;
import com.digitechlabs.paymentgw.history.UpdateStatusProcess;
import com.digitechlabs.paymentgw.paypal.token.GetTokenProcess;
import com.digitechlabs.paymentgw.rabbitqueue.RabbitMQHisWalletSender;
import com.digitechlabs.paymentgw.rabbitqueue.RabbitQueueProcess;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.BlockQueue;
import utils.Config;

public class Main {

    private String CONF_FILE = "httplistener.conf";
    private String LOG_CFG_FILE = "config/log4j.properties";

    private String PORT_WSDL;
    private String NUMB_THREAD;
    private String SOCKET_TIMEOUT;

    private String listIpClient;
    private String allowIpCall;

    private static Main mMe = null;
    private HttpListener h;

    private final BlockQueue notifyQueue = new BlockQueue();
    private RabbitQueueProcess processor;

    /**
     * @param args the command line arguments$
     */
    public static void main(String args[]) {

        Main r = Main.getInstance();
        r.start();

    }

    public static synchronized Main getInstance() {
        if (mMe == null) {
            mMe = new Main();
            mMe.loadConfig();
        }
        return mMe;
    }

    public void start() {
        //init sync rate process
        GetTokenProcess.getInstance().start();
        SyncRateProcess.getInstance().start();

        h = new HttpListener(Integer.parseInt(PORT_WSDL));
        h.start();

        processor = new RabbitQueueProcess("RabbitQueueProcess");
        processor.start();

        UpdateStatusProcess.getInstance().start();
        RabbitMQHisWalletSender.getInstance().start();
    }

    public void stop() {
        UpdateStatusProcess.getInstance().stop();
        h.stop();

        processor.stop();
        SyncRateProcess.getInstance().stop();
        GetTokenProcess.getInstance().stop();
        RabbitMQHisWalletSender.getInstance().stop();
    }

    public void loadConfig() {
//        Config.config("etc", "log");
        String os = System.getProperty("os.name");
        os = os.toLowerCase(Locale.getDefault());
        if (os.contains("windows")) {
            Config.config("etc", "log");
//            Config.config("etc", "log");
        } else {
//            Config.config("../etc", "../log");
            Config.config("etc", "log");
//            Config.config("../etc", "../log");
        }

        LOG_CFG_FILE = Config.getConfigDir() + File.separator + "log4j.conf";
        CONF_FILE = Config.getConfigDir() + File.separator + "httplistener.conf";

        PropertyConfigurator.configure(LOG_CFG_FILE); // log4j
        FileInputStream gencfgFile = null;
        try {
            Properties prop = new Properties();
            gencfgFile = new FileInputStream(CONF_FILE);
            prop.load(gencfgFile);

            //PORT
            PORT_WSDL = prop.getProperty("http.port").trim();
            MyLog.Debug("http.port: " + PORT_WSDL);

            //NUMBER OF THREAD
            NUMB_THREAD = prop.getProperty("thread.number").trim();
            MyLog.Debug("thread.number: " + NUMB_THREAD);

            //SOCKET TIMEOUT
            SOCKET_TIMEOUT = prop.getProperty("socket.timeout").trim();
            MyLog.Debug("socket.timeout: " + SOCKET_TIMEOUT);

        } catch (IOException e) {
            MyLog.Error("ERROR LOAD CONFIG FILE: " + e.getMessage());
            MyLog.Error(e);
        } finally {
            if (gencfgFile != null) {
                try {
                    gencfgFile.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        MyLog.Debug("Done.");
    }

    public String getCONF_FILE() {
        return CONF_FILE;
    }

    public String getLOG_CFG_FILE() {
        return LOG_CFG_FILE;
    }

    public String getPORT_WSDL() {
        return PORT_WSDL;
    }

    public String getNUMB_THREAD() {
        return NUMB_THREAD;
    }

    public String getSOCKET_TIMEOUT() {
        return SOCKET_TIMEOUT;
    }

    public String getListIpClient() {
        return listIpClient;
    }

    public String getAllowIpCall() {
        return allowIpCall;
    }

    public BlockQueue getNotifyQueue() {
        return notifyQueue;
    }

}

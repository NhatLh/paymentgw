package com.digitechlabs.paymentgw.dbpooling;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class ConnectionInfo {

    protected Map<String, String> keyValueTable;
    private Logger logger = Logger.getLogger(ConnectionInfo.class);

    public ConnectionInfo() {
        this.keyValueTable = new HashMap();
    }

    public String getValue(String key) {
        key = key.toLowerCase();
        if (this.keyValueTable.containsKey(key)) {
            return (String) this.keyValueTable.get(key);
        }
        return "";
    }

    public void setValue(String key, String value) {
        key = key.toLowerCase();
        if (!this.keyValueTable.containsKey(key)) {
            this.keyValueTable.put(key, value);
        }
    }

    public String getID() {
        return getValue("id");
    }

    public String getDriver() {
        return getValue("driver");
    }

    public String getUrl() throws Exception {
        return getValue("url");
    }

    public String getUsername() throws Exception {
        return getValue("username");
    }

    public String getPassword() throws Exception {
        return getValue("password");
    }

    public String getMinPoolSize() {
        return getValue("MINPOLLSIZE");
    }

    public String getMAXPOLLSIZE() {
        return getValue("MAXPOLLSIZE");
    }

    public String getCHECK_TIMEOUT() {
        return getValue("CHECK_TIMEOUT");
    }

    public String getIDLE_PERIOD() {
        return getValue("IDLE_PERIOD");
    }

    public String getMAX_IDLE_TIME() {
        return getValue("MAX_IDLE_TIME");
    }

    public ComboPooledDataSource createPoolConnection() {
        try {
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass(getDriver());
            cpds.setJdbcUrl(getUrl());
            cpds.setUser(getUsername());
            cpds.setPassword(getPassword());
            cpds.setMinPoolSize(Integer.parseInt(getMinPoolSize()));
            cpds.setMaxPoolSize(Integer.parseInt(getMAXPOLLSIZE()));
            cpds.setCheckoutTimeout(Integer.parseInt(getCHECK_TIMEOUT()));
            cpds.setIdleConnectionTestPeriod(Integer.parseInt(getIDLE_PERIOD()));
            cpds.setMaxIdleTime(Integer.parseInt(getMAX_IDLE_TIME()));
            return cpds;
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);

        }
        return null;
    }
}

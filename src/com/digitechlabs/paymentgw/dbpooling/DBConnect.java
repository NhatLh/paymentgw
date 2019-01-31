package com.digitechlabs.paymentgw.dbpooling;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import config.Group;
import config.Groups;
import config.PairPropertyValue;
import configurator.Configurator;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import utils.Config;

public class DBConnect {

    private static Logger logger = Logger.getLogger(DBConnect.class);
    private static Map<String, ComboPooledDataSource> connectionMap = new HashMap();

    public static synchronized Connection getConnection(String id) {
        Connection conn = null;
        if (connectionMap.isEmpty()) {
            Configurator config = new Configurator();
//      config.setConfigureFile("etc/db.conf");
//      config.setConfigureFile("../etc/db.conf");
            config.setConfigureFile(Config.getConfigDir() + File.separator + "db.conf");

            if (config.load()) {
                Groups grps = config.getGroups();
                try {
                    List<Group> listUserGrp = grps.getGroup("connection");

                    for (Group grp : listUserGrp) {
                        ConnectionInfo info = new ConnectionInfo();
                        for (PairPropertyValue pair : grp.getAllPropertyValue()) {
                            info.setValue(pair.property, pair.value);
                        }
                        connectionMap.put(info.getID(), info.createPoolConnection());
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        try {
            if (connectionMap.containsKey(id)) {
                conn = ((ComboPooledDataSource) connectionMap.get(id)).getConnection();
            }
        } catch (SQLException ex) {
            logger.error("Can't connect DB, with ID " + id + " ==>" + ex.getMessage(), ex);
        }

        return conn;
    }
}

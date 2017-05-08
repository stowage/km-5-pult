package org.ucs.wrapper;

import java.util.HashMap;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 19.11.2006
 * Time: 19:55:02
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionManager {

    static HashMap connections = new HashMap();

    public static synchronized ConnectionModule getConnection(String deviceKey) {
        if(connections.containsKey(deviceKey)) {
            return (ConnectionModule)connections.get(deviceKey);
        }

        try {
            Properties prop = Settings.getProperties(deviceKey);
            ConnectionModule conn = (ConnectionModule)(((Class)Class.forName(prop.getProperty("DRIVER"))).newInstance());
            try {
                conn.connect(prop);
            } catch (Exception ex1) {
                ex1.printStackTrace();
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Невозможно разорвать соединение");
                }
            }
            connections.put(deviceKey, conn);
            return conn;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException("Невозможно установить соединение");
    }

    public static synchronized ConnectionModule getConnection(String deviceKey, Properties prop) {
        if(connections.containsKey(deviceKey)) {
            return (ConnectionModule)connections.get(deviceKey);
        }

        try {
            ConnectionModule conn = (ConnectionModule)(((Class)Class.forName(prop.getProperty("DRIVER"))).newInstance());
            try {
                conn.connect(prop);
            } catch (Exception ex1) {
                ex1.printStackTrace();
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Невозможно разорвать соединение");
                }
            }
            connections.put(deviceKey, conn);
            return conn;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException("Невозможно установить соединение");
    }


    public static synchronized void disconnect(String deviceKey) {
        if(connections.containsKey(deviceKey)) {
            ConnectionModule conn = (ConnectionModule)connections.get(deviceKey);
            try {
               conn.disconnect();
            } catch (Exception ex) {}
            connections.remove(deviceKey);
        }
    }

}

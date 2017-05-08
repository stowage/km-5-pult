package org.ucs.wrapper;

import java.util.*;

public class Settings {

	public static Properties getProperties(String baseName) {
        Properties prop = new Properties();
        try {

            ResourceBundle bundle = ResourceBundle.getBundle(baseName);
            Enumeration en = bundle.getKeys();
            while(en.hasMoreElements()) {
                String key = (String)en.nextElement();
                prop.setProperty(key, bundle.getString(key));
            }
        } catch(Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e.toString());
        }
        return prop;
    }

}
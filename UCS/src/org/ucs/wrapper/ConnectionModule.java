package org.ucs.wrapper;

import java.util.Properties;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 17.06.2005
 * Time: 21:18:16
 * To change this template use File | Settings | File Templates.
 */
public interface ConnectionModule {
    
    public void connect(Properties prop) throws Exception;
    public void disconnect();
    public void kill();

    public boolean isConnected(Properties prop);

    public Properties getProperties();

    public void setProperties(Properties prop);

    public SessionModule getSession() throws Exception;

    public ArrayList getSources();

    public int getMode();

}

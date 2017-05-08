package org.ucs.wrapper;

import org.ucs.wrapper.km5.CommandModuleImpl;

import java.util.HashMap;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 20.11.2006
 * Time: 14:53:54
 * To change this template use File | Settings | File Templates.
 */
public class TransactionManager {
    static HashMap transactions = new HashMap();

    public static synchronized CommandModule getCommander(String deviceKey) {
        if(transactions.containsKey(deviceKey)) {
            return (CommandModule)transactions.get(deviceKey);
        }

        try {
            Properties prop = Settings.getProperties(deviceKey);
            ConnectionModule conn = ConnectionManager.getConnection(deviceKey, prop);
            Object trans = (((Class)Class.forName(prop.getProperty("TRANSDRIVER"))).newInstance());
            CommandModuleImpl cmdi = new CommandModuleImpl(conn);
            Transaction normalT = new Transaction(conn, Integer.parseInt(prop.getProperty("TRY")) , Integer.parseInt(prop.getProperty("TRY_DELAY")), true);
            normalT.setTransactionReanimator((TransactionReanimator)trans);
            normalT.setTransactionValidater((TransactionValidater)trans);
            CommandModule cmd = CommandTransaction.DTI(cmdi, normalT);
            transactions.put(deviceKey, cmd);
            return cmd;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException("Невозможно нладить управление");
    }

}

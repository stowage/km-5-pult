package org.ucs.wrapper;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 14.11.2005
 * Time: 23:43:10
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {

    public int numberOfTries;
    public long delay;
    public boolean isSynchronized;
    ConnectionModule conn;

    public Transaction(ConnectionModule conn, int numberOfTries, long delay, boolean isSynchronized) {
        this.numberOfTries = numberOfTries;
        this.delay = delay;
        this.isSynchronized = isSynchronized;
        this.conn = conn;
    }

    public TransactionValidater validater;
    public TransactionReanimator reanimator;

    public void setTransactionValidater(TransactionValidater validater) {
        this.validater = validater;
    }

    public void setTransactionReanimator(TransactionReanimator reanimator) {
        this.reanimator = reanimator;
    }

 }

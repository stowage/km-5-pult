package org.ucs.wrapper;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 19.06.2005
 * Time: 16:31:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class DataContainer implements java.io.Serializable {

      public String netAddr;
      public int command;
      public boolean checkSum;
      public int csValue1;
      public int csValue2;
      public int err;

}

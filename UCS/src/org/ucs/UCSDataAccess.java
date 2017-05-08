package org.ucs;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import org.ucs.wrapper.km5.container.*;
import java.sql.*;
import java.io.*;

public class UCSDataAccess implements DataAccess {
  protected FileWriter out;
  protected File fname;
  protected String deviceId;
  protected int dbType;

  public UCSDataAccess() {

  }

  public void open(String filename) throws IOException {
    fname = new File(filename);
    out = new FileWriter(fname, false);
  }

  public void checkBound(String deviceId, int dbType, ReadNumberLineDBContainer container) throws SQLException {
    this.deviceId = deviceId;
    this.dbType = dbType;
  }

  public void insertRecord(String deviceId, int dbType, java.util.Date date, float Q, float M1, float M2, float t1, float t2, float P1, float P2, float Tp) throws SQLException,IOException {
    out.write(deviceId+"\t"+dbType+"\t"+StringUtils.formatDate(date,"yyyy-MM-dd HH:mm:ss")+"\t"+Q+"\t"+M1+"\t"+M2+"\t"+t1+"\t"+t2+"\t"+P1+"\t"+P2+"\t"+Tp+"|");
    out.flush();
  }

  public void insertRecord(String deviceId, java.util.Date date, int errCode) throws SQLException,IOException {
    out.write(deviceId+"\t"+4+"\t"+StringUtils.formatDate(date,"yyyy-MM-dd HH:mm:ss")+"\t"+errCode+"|");
    out.flush();
  }

  public void close() throws SQLException,IOException {
    out.flush();
    out.close();
    out = null;
  }

  public void publishData(String deviceId, int dbType, java.util.Date start, java.util.Date end, DataPublisher dpub) throws SQLException,IOException {
  }

}
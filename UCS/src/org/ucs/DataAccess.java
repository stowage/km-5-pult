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

public interface DataAccess {
   public void open(String name) throws SQLException, IOException;
   public void close() throws SQLException, IOException;
   public void checkBound(String deviceId, int dbType, ReadNumberLineDBContainer container) throws SQLException;
   public void insertRecord(String deviceId, int dbType, java.util.Date date, float Q, float M1, float M2, float t1, float t2, float P1, float P2, float Tp) throws SQLException,IOException;
   public void insertRecord(String deviceId, java.util.Date date,int errCode) throws SQLException,IOException;
   public void publishData(String deviceId, int dbType, java.util.Date start, java.util.Date end, DataPublisher dpub) throws SQLException,IOException;

}
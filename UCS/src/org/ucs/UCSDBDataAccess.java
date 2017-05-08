package org.ucs;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.sql.*;
import java.io.*;

public class UCSDBDataAccess extends UCSDataAccess {
  Connection conn;
  public UCSDBDataAccess(Connection conn) {
    this.conn = conn;
  }

  public void close() throws SQLException,IOException {
    super.close();
    CallableStatement stm = null;
    try {
      stm = conn.prepareCall(super.dbType!=4?"{call insertData(?)}":"{call insertErrData(?)}");
      stm.setString(1, fname.getAbsolutePath());
      stm.execute();
      stm.close();
    } finally {
      if(stm!=null)
        stm.close();
    }
  }

}
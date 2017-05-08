package org.ucs;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.io.*;

public class LogController implements StatusController {
  FileWriter log;
  public LogController() {

  }
  public void open(String logFileName) throws IOException {
    log = new FileWriter(logFileName, false);
  }

  public void close() throws IOException {
    log.flush();
    log.close();
    log = null;
  }

  public void setBounds(int min, int max) {
  }
  public void adjustProgress(int cur) {
  }

  public void write(String str) throws IOException {
    log.write("["+StringUtils.formatDate(new java.util.Date(),"MM-dd-yy HH:mm:ss")+"] "+str);
    log.flush();
  }
  public void writeln(String str) throws IOException {
    log.write("["+StringUtils.formatDate(new java.util.Date(),"MM-dd-yy HH:mm:ss")+"] "+str+"\r\n");
    log.flush();
  }

}
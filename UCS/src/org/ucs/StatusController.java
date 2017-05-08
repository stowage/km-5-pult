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

public interface StatusController {
  public void setBounds(int min, int max);
  public void adjustProgress(int cur);

  public void open(String fileName) throws IOException;
  public void close() throws IOException;
  public void write(String str) throws IOException;
  public void writeln(String str) throws IOException;
}
package org.ucs;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface DataPublisher {
  public void publish(int dbType, java.util.Date date,float Q, float M1, float M2, float t1, float t2, float Tp);
  public void publish(java.util.Date date,int errCode);
}
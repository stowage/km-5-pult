package org.ucs;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.*;

public class FormatUtils {
  public static String getDBName(int dbType) {
    String strDBType = "неизвестный";
    switch (dbType) {
      case 0:
        strDBType = "почасовая";
        break;
      case 1:
        strDBType = "посуточная";
        break;
      case 2:
        strDBType = "помесячная";
        break;
      case 3:
        strDBType = "погодовая";
        break;
      case 4:
        strDBType = "ошибок";
        break;
    }
    return strDBType;
  }

  public static java.util.Date toJDate(int day, int month, int year, int hour, int minute, int second) {
    Calendar cld = Calendar.getInstance();
    if(hour>23) {
      cld.set(2000+year, month-1, day);
      cld.add(Calendar.DATE, 1);
      cld.set(Calendar.HOUR_OF_DAY,0);
      cld.set(Calendar.MINUTE,minute);
      cld.set(Calendar.SECOND,second);
    } else {
      cld.set(2000+year, month-1, day, hour, minute, second);
    }
    return cld.getTime();
  }
}
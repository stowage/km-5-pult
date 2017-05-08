package org.ucs;

import org.ucs.wrapper.*;
import org.ucs.wrapper.km5.container.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UCSDataLoader implements DataLoader {
  CommandModule cmd;
  String deviceId;

  public UCSDataLoader(CommandModule cmd, String deviceId) {
    this.cmd = cmd;
    this.deviceId = deviceId;
  }
  public void load(DataAccess data, StatusController control) throws Exception {

    synchronized(cmd) {
      try { control.open(deviceId+".log");
      for(int dbType=0;dbType<=4;dbType++){
        String dbName = FormatUtils.getDBName(dbType);
        control.writeln("Получение информации о "+dbName+" БД...");
        ReadTitleDBContainer dbTitle = new ReadTitleDBContainer();
        cmd.cmdReadTitleDB(dbTitle, deviceId, dbType);
        control.writeln("ГОТОВО");

        if ( (dbTitle.specialFlags & 64) == 64) {
          ReadNumberLineDBContainer nline = new ReadNumberLineDBContainer();
          cmd.cmdReadNumberLineDB(nline, deviceId, dbType);
          data.checkBound(deviceId, dbType, nline);

          if(nline.numberEarlyLine==nline.numberLastLine && nline.numberLastLine==-1) {
            control.writeln("Данные уже загружены.");
          } else {

            if((dbTitle.specialFlags & 128)==128) {
                control.writeln("Все строки заполнены данными.");
            } else {
                control.writeln("Не все строки заполнены данными.");
            }

            control.writeln("Допустимое число строк в БД:"+dbTitle.numberLineDB);
            control.writeln("№ последней записи:"+dbTitle.numberLastLine);

            control.writeln("№ первой записи:"+nline.numberEarlyLine);
            control.writeln("Дата/Время первой записи:"+nline.numbMonthEarly+"/"+nline.monthEarly+"/"+nline.yearEarly+" "+nline.hoursEarly+":"+nline.minutesEarly+":"+nline.secondsEarly);

            control.writeln("№ последней записи:"+nline.numberLastLine);
            control.writeln("Дата/Время последней записи:"+nline.numbMonthLast+"/"+nline.monthLast+"/"+nline.yearLast+" "+nline.hoursLast+":"+nline.minutesLast+":"+nline.secondsLast);

            control.writeln("Загрузка "+dbName+" БД...");
            control.setBounds(nline.numberEarlyLine,nline.numberLastLine);

            ReadLineDBContainer rld = new ReadLineDBContainer();
            ReadLineNumberDBErrContainer errl = new ReadLineNumberDBErrContainer();

            try {
            data.open("report"+dbType+"-"+StringUtils.formatDate(new java.util.Date(),"MMddyy-HHmmss")+".txt");
            for (int i = nline.numberEarlyLine; i <= nline.numberLastLine; i++) {

              if (dbType != 4) {
                cmd.cmdReadLineDB(rld, deviceId, dbType, i);

                data.insertRecord(deviceId,
                                  dbType,
                                  FormatUtils.toJDate(rld.day,
                    rld.month,
                    rld.year,
                    rld.hours,
                    rld.minutes,
                    rld.seconds),
                                  rld.Q, rld.M1, rld.M2, rld.t1, rld.t2, rld.P1, rld.P2, rld.Tp
                                  );
                rld.day = 0;
                rld.month = 0;
                rld.year = 0;
                rld.minutes = 0;
                rld.seconds = 0;
                rld.hours = 0;
                rld.Q = 0;
                rld.M1 = 0;
                rld.M2 = 0;
                rld.t1 = 0;
                rld.t2 = 0;
                rld.P1 = 0;
                rld.P2 = 0;

                rld.Tp = 0;
              } else {
                cmd.cmdReadLineNumberDBErr(errl, deviceId, i);

                data.insertRecord(deviceId,
                                  FormatUtils.toJDate(errl.day,
                    errl.month,
                    errl.year,
                    errl.hours,
                    errl.minutes,
                    errl.seconds),
                                  errl.codeEventOrErr
                                  );
                errl.day = 0;
                errl.month = 0;
                errl.year = 0;
                errl.minutes = 0;
                errl.seconds = 0;
                errl.hours = 0;
                errl.codeEventOrErr = 0;

              }

              control.adjustProgress(i);
            }
            } finally { data.close(); }
            control.writeln("ГОТОВО");
          }
        } else {
          control.writeln("В БД нет записей.");
        }
      }
    } finally { control.close(); }
    }

  }

}
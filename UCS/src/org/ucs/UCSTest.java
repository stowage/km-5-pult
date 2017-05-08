package org.ucs;

import org.ucs.wrapper.km5.DirectConnector;
import org.ucs.wrapper.CommandModule;
import org.ucs.wrapper.km5.CommandModuleImpl;
import org.ucs.wrapper.*;
import org.ucs.wrapper.km5.container.*;
import java.sql.*;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 10.11.2005
 * Time: 9:21:33
 * To change this template use File | Settings | File Templates.
 */
public class UCSTest {

    public static void main(String arg[]) throws Exception {

        final ConnectionModule conn = new DirectConnector();
        Properties prop = new Properties();
        prop.setProperty("SERIAL_PORT","COM4");
        prop.setProperty("DRIVER_VERSION", "KM5 1.0");
        prop.setProperty("TIMEOUT","5000");
        prop.setProperty("BAUD_RATE","9600");
        prop.setProperty("DATABITS","8");
        prop.setProperty("STOPBITS","2");
        prop.setProperty("PARITY","None");

        conn.connect(prop);

        CommandModuleImpl cmdi = new CommandModuleImpl(conn);
        CommandModule cmd =
                CommandTransaction.DTI(cmdi, new Transaction(conn, 10,10, true));

        //conn.disconnect();
        {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    conn.disconnect();
                }
            }));
            try {
                //conn.connect(prop);
                if(conn.isConnected(prop)) {
                    //Check monitor
                    Class.forName("com.inet.tds.TdsDriver").newInstance();
                    //Thread.sleep(100);
                    Connection cnt = DriverManager.getConnection("jdbc:inetdae7:10.77.106.162:1444?database=ucs&user=sa&password=libor");

                    //CallableStatement stm = cnt.prepareCall("{call truncateTables()}");
                    //stm.execute();

                    String deviceID = "00000001";
                    /*UCSDBDataAccess da = new UCSDBDataAccess(cnt);
                    LogController log = new LogController();
                    UCSDataLoader dl = new UCSDataLoader(cmd,deviceID);

                    dl.load(da, log);*/

          //DataReport dr = new DataReport(cnt, cmd);
          //          dr.generateReport(deviceID,2,new java.sql.Date(105,1,10),new java.sql.Date(106,3,30));

                    /*System.out.println("Номер прибора:"+deviceID);

                    ReadTitleDBContainer dbTitle = new ReadTitleDBContainer();
                    cmd.cmdReadTitleDB(dbTitle, deviceID, dbType);

                    String strDBType = "неизвестный";
                    switch(dbType) {
                        case 0: strDBType="почасовая"; break;
                        case 1: strDBType="посуточная"; break;
                        case 2: strDBType="помесячная"; break;
                        case 3: strDBType="погодовая"; break;
                        case 4: strDBType="ошибок"; break;
                    }

                    ReadVersionContainer version = new ReadVersionContainer();
                    cmd.cmdVersion(version,deviceID);

                    ReadImmidiateValue1Container imm = new ReadImmidiateValue1Container();
                    cmd.cmdImmidiateValue1(imm, deviceID, 0);

                    System.out.println("Тип прибора:"+((int)imm.currentType+1)+"\tv"+version.version);

                    System.out.println("Тип БД:"+strDBType);

                    if((dbTitle.specialFlags & 64)==64) {
                        System.out.println("В БД сделана по меньшей мере одна запись.");
                    } else {
                        System.out.println("В БД нет записей.");
                    }
                    if((dbTitle.specialFlags & 128)==128) {
                        System.out.println("Все строки заполнены данными.");
                    } else {
                        System.out.println("Не все строки заполнены данными.");
                    }

                    System.out.println("Максимальное число строк в БД:"+dbTitle.numberLineDB);
                    System.out.println("№ последней записи:"+dbTitle.numberLastLine);

                    ReadNumberLineDBContainer nline = new ReadNumberLineDBContainer();
                    cmd.cmdReadNumberLineDB(nline, deviceID, dbType);

                    System.out.println("№ первой записи:"+nline.numberEarlyLine);
                    System.out.println("Дата/Время первой записи:"+nline.numbMonthEarly+"/"+nline.monthEarly+"/"+nline.yearEarly+" "+nline.hoursEarly+":"+nline.minutesEarly+":"+nline.secondsEarly);

                    System.out.println("№ последней записи:"+nline.numberLastLine);
                    System.out.println("Дата/Время последней записи:"+nline.numbMonthLast+"/"+nline.monthLast+"/"+nline.yearLast+" "+nline.hoursLast+":"+nline.minutesLast+":"+nline.secondsLast);

                    double sumQ = 0D;
                    double sumM1 = 0D;
                    double sumM2 = 0D;
                    double sumTp = 0D;
                    for(int i=nline.numberEarlyLine;i<=nline.numberLastLine; i++) {

                        if(dbType!=4) {
                            ReadLineDBContainer rld = new ReadLineDBContainer();
                            cmd.cmdReadLineDB(rld,deviceID,dbType,i);

                            sumQ+=rld.Q;
                            sumM1+=rld.M1;
                            sumM2+=rld.M2;
                            sumTp+=rld.Tp;

                            System.out.println(rld.day+"/"+rld.month+"/"+rld.year+" "+rld.hours+":"+rld.minutes+":"+rld.seconds+"\t"+rld.Q+"\t"+rld.M1+"\t"+rld.M2+"\t"+(rld.M2-rld.M1)+"\t"+rld.t1+"\t"+rld.t2+"\t"+(rld.t2-rld.t1)+"\t"+rld.Tp);
                        } else {
                            ReadLineNumberDBErrContainer errl = new ReadLineNumberDBErrContainer();
                            cmd.cmdReadLineNumberDBErr(errl, deviceID, i);

                            System.out.println(errl.day+"/"+errl.month+"/"+errl.year+" "+errl.hours+":"+errl.minutes+":"+errl.seconds+"\t"+errl.codeEventOrErr);
                        }
                    }*/
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            } finally {
                conn.disconnect();
            }
        }
        Thread.currentThread().join(10000);

    }
}

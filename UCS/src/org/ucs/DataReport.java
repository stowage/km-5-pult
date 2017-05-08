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
import org.ucs.wrapper.*;
import org.ucs.wrapper.km5.container.*;

public class DataReport {
  Connection conn;
  CommandModule cmd;

  static final String[] status = new String[128];
  static {
    status[51] = "t4 > максимума t4max";
    status[52] = "t4 < минимума t4min";
    status[53] = "Резерв";
    status[54] = "Gv4 > максимума Gv4max";
    status[55] = "Gv3 > максимума Gv3max";
    status[56] = "Режим \"Основной (Зима)\"";
    status[57] = "Режим \"Лето 1\"";
    status[58] = "Режим \"Лето 2\"";
    status[59] = "Режим \"Лето 3\"";
    status[60] = "Режим \"Нет потока\"";
    status[61] = "Режим \"НЕШТАТНЫЙ\"";
    status[62] = "Резерв";
    status[63] = "Резерв";
    status[64] = "U канала G ППС > допустимого максимума";
    status[65] = "I катушки ППС > допустимого максимума";
    status[66] = "I катушки ППС < допустимого минимума";
    status[67] = "U канала G КМ-5 > допустимого максимума";
    status[68] = "I катушки КМ-5 > допустимого максимума";
    status[69] = "I катушки КМ-5 < допустимого минимума";
    status[70] = "Gv2 > 1.04*Gv2 (только для КМ-5-5)";
    status[71] = "tхп* > максимума tхпmax";
    status[72] = "Резерв";
    status[73] = "Tхп* < минимума tхпmin";
    status[74] = "t2п* > максимума t2пmax";
    status[75] = "Резерв";
    status[76] = "t2п* < минимума t2пmin";
    status[77] = "ta > максимума    tamax (по умолчанию tamax = + 60°С)";
    status[78] = "ta < минимума tamin (по умолчанию tamin = - 60°С)";
    status[79] = "tхк* > максимума tхкmax";
    status[80] = "Резерв";
    status[81] = "tхк* < минимума tхкmin";
    status[82] = "Gv2 > максимума Gv2max";
    status[83] = "Резерв";
    status[84] = "Gv2 < минимума Gv2min";
    status[85] = "Gv1 > максимума Gv1max";
    status[86] = "Резерв";
    status[87] = "Gv1 < минимума Gv1min";
    status[88] = "t2к* > максимума t2кmax";
    status[89] = "Резерв";
    status[90] = "t2к* < минимума t2кmin";
    status[91] = "t1 к* > максимума t1кmax";
    status[92] = "Резерв";
    status[93] = "t1 к* < минимума t1кmin";
    status[94] = "t1 - t2 > максимума dtmax";
    status[95] = "Резерв";
    status[96] = "t1 - t2 < минимума dtmin";
    status[97] = "Давление Px < Pxmin";
    status[98] = "Был RESET или WATCHDOG";
    status[99] =
        "Было выполнено изменение даты и/или времени в RTC теплосчетчика";
    status[100] = "Давление Px > Pxmax";
    status[101] = "Давление P2 < P2min";
    status[102] = "Обнуление интеграторов за час";
    status[103] = "Количество ошибок за сутки > максимума***";
    status[104] = "Давление P2 > P2max";
    status[105] = "Давление P1 < P1min";
    status[106] = "Тепловая мощность W < 0";
    status[107] = "Резерв";
    status[108] = "Давление P1 > P1max";
    status[114] = "Ошибка обмена с ППС";
    status[115] = "Обрыв в цепи датчика Px";
    status[116] = "Обрыв в цепи датчика P2 ППС";
    status[117] = "Обрыв в цепи датчика P2 КМ-5";
    status[118] = "Обрыв в цепи датчика P1";
    status[119] = "Неисправность в цепи термопреобразователей ППС";
    status[120] = "Неисправность в цепи термопреобразователей КМ-5";
    status[121] = "Останов счета";
    status[122] = "Сбой питания";
    status[123] = "Резерв";
    status[124] = "Ошибка чтения из RTC";
    status[125] = "Ошибка записи в RTC";
    status[126] = "Ошибка чтения из EEPROM";
    status[127] = "Ошибка записи в EEPROM";

  }

  public static final String getErr(int code) {
    return code > 0 && code < status.length && status[code] != null ?
        status[code] : "неизвестная ошибка";
  }

  public DataReport(Connection conn, CommandModule cmd) {
    this.conn = conn;
    this.cmd = cmd;
  }

  public void generateReport(File file, String deviceId, int dbType,
                             java.util.Date start_date, java.util.Date end_date) throws
      Exception {
    FileWriter out = new FileWriter(file, false);

    ReadVersionContainer version = new ReadVersionContainer();
    ReadImmidiateValue1Container imm = new ReadImmidiateValue1Container();
    synchronized (cmd) {
      cmd.cmdVersion(version, deviceId);
      cmd.cmdImmidiateValue1(imm, deviceId, 0);
    }

    out.write("<html><body>\n");
    out.write("<h3>Отчет за период с" +
              StringUtils.formatDate(start_date, "dd.MM.yyyy") + " по " +
              StringUtils.formatDate(end_date, "dd.MM.yyyy") + " (БД:" +
              FormatUtils.getDBName(dbType) + ")</h3>");
    out.write("<table width=100% border=0>\n");
    out.write("<tr>\n");
    out.write("<td align=left>Номер: " + deviceId +
              "</td>\n<td align=right>Тип прибора: КМ-5-" +
              ( (int) imm.currentType + 1) + " v" + version.version + "</td>\n");
    out.write("</tr>\n");
    out.write("</table>\n");
    out.write("<br>\n");

    CallableStatement stm = conn.prepareCall("{call generateReport(?,?,?,?)}");
    stm.setString(1, deviceId);
    stm.setInt(2, dbType);
    stm.setDate(3, new java.sql.Date(start_date.getTime()));
    stm.setDate(4, new java.sql.Date(end_date.getTime()));
    stm.execute();

    if (dbType != 4) {
      out.write("<table width=100% border=1>\n");
      out.write("<tr>\n");
      out.write("<td align=center>Дата</td>");
      out.write("<td align=center>Q</td>");
      out.write("<td align=center>M1</td>");
      out.write("<td align=center>M2</td>");
      out.write("<td align=center>M1-M2</td>");
      out.write("<td align=center>M2-M1</td>");
      out.write("<td align=center>T1</td>");
      out.write("<td align=center>T2</td>");
      out.write("<td align=center>T1-T2</td>");
      out.write("<td align=center>P1</td>");
      out.write("<td align=center>P2</td>");
      out.write("<td align=center>Tp</td>");
      out.write("<td align=center>Ошибки</td>");
      out.write("</tr>\n");

      ResultSet rs = stm.getResultSet();
      while (rs.next()) {
        out.write("<tr>\n");
        out.write("<td align=center>" +
                  (rs.getDate(1) != null ?
                   StringUtils.
                   formatDate(new java.util.
                              Date(rs.getDate(1).getTime() +
                                   rs.getTime(1).getTime()),
                              "dd.MM.yyyy HH:mm:ss") : "----") + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(2), 3) + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(3), 2) + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(4), 2) + "</td>");
        out.write("<td align=center>" +
                  (rs.getFloat(5) < 0 ? "-----" :
                   StringUtils.formatFloat(rs.getFloat(5), 2)) + "</td>");
        out.write("<td align=center>" +
                  (rs.getFloat(6) < 0 ? "-----" :
                   StringUtils.formatFloat(rs.getFloat(6), 2)) + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(7), 2) + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(8), 2) + "</td>");
        out.write("<td align=center>" +
                  (rs.getFloat(9) < 0 ? "-----" :
                   StringUtils.formatFloat(rs.getFloat(9), 2)) + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(10), 2) + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(11), 2) + "</td>");
        out.write("<td align=center>" +
                  StringUtils.formatFloat(rs.getFloat(12), 2) + "</td>");
        out.write("<td align=center>" +
                  (rs.getString(13)!=null?rs.getString(13):"") +
                 (rs.getString(14)!=null?rs.getString(14):"") +
                 (rs.getString(15)!=null?rs.getString(15):"") +
                 (rs.getString(13)!=null?rs.getString(16):"") +"</td>");
        out.write("</tr>\n");
      }
      rs.close();

      if (stm.getMoreResults()) {
        rs = stm.getResultSet();
        while (rs.next()) {
          out.write("<tr border=0>\n");
          out.write("<td align=center><b>Итого:</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(1), 3) + "</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(2), 2) + "</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(3), 2) + "</b></td>");
          out.write("<td align=center><b>" +
                    (rs.getFloat(4) < 0 ? "-----" :
                     StringUtils.formatFloat(rs.getFloat(4), 2)) + "</b></td>");
          out.write("<td align=center><b>" +
                    (rs.getFloat(5) < 0 ? "-----" :
                     StringUtils.formatFloat(rs.getFloat(5), 2)) + "</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(6), 2) + "</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(7), 2) + "</b></td>");
          out.write("<td align=center><b>" +
                    (rs.getFloat(8) < 0 ? "-----" :
                     StringUtils.formatFloat(rs.getFloat(8), 2)) + "</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(9), 2) + "</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(10), 2) + "</b></td>");
          out.write("<td align=center><b>" +
                    StringUtils.formatFloat(rs.getFloat(11), 2) + "</b></td>");
          out.write("</tr>\n");
        }
      }

      out.write("</table>\n<br>");
      out.write("<table border=1>");
      out.write("<tr>");
      out.write("<td align=center>Дата/Время</td>");
      out.write("<td align=center>Q</td>");
      out.write("<td align=center>M1</td>");
      out.write("<td align=center>M2</td>");
      out.write("<td align=center>Tp</td>");
      out.write("</tr>");
      if (stm.getMoreResults()) {
        rs = stm.getResultSet();
        while (rs.next()) {
          out.write("<tr border=0>\n");
          out.write("<td align=center>" +
                    (rs.getDate(1) != null ?
                     StringUtils.
                     formatDate(new java.
                                util.Date(rs.getDate(1).getTime() +
                                          rs.getTime(1).getTime()),
                                "dd.MM.yyyy HH:mm:ss") : "----") + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(2), 3) + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(3), 2) + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(4), 2) + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(5), 2) + "</td>");
          out.write("</tr>\n");
        }
      }

      if (stm.getMoreResults()) {
        rs = stm.getResultSet();
        while (rs.next()) {
          out.write("<tr border=0>\n");
          out.write("<td align=center>" +
                    (rs.getDate(1) != null ?
                     StringUtils.
                     formatDate(new java.
                                util.Date(rs.getDate(1).getTime() +
                                          rs.getTime(1).getTime()),
                                "dd.MM.yyyy HH:mm:ss") : "----") + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(2), 3) + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(3), 2) + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(4), 2) + "</td>");
          out.write("<td align=center>" +
                    StringUtils.formatFloat(rs.getFloat(5), 2) + "</td>");
          out.write("</tr>\n");
        }
      }
      out.write("</table>\n");
    } else {
      out.write("<table width=100% border=1>\n");
      out.write("<tr>\n");
      out.write("<td align=center>Код</td>");
      out.write("<td align=center>Дата/Время</td>");
      out.write("<td align=center>Описание</td>");
      out.write("</tr>\n");

      ResultSet rs = stm.getResultSet();
      while (rs.next()) {
        out.write("<tr>\n");
        out.write("<td align=center>" +rs.getInt(2)+ "</td>");
        out.write("<td align=center>" +
                  (rs.getDate(1) != null ?
                   StringUtils.
                   formatDate(new java.util.
                              Date(rs.getDate(1).getTime() +
                                   rs.getTime(1).getTime()),
                              "dd.MM.yyyy HH:mm:ss") : "----") + "</td>");
        out.write("<td align=center>" +DataReport.getErr(rs.getInt(2))+ "</td>");
        out.write("</tr>\n");
      }
      rs.close();

    }
    stm.close();
    out.write("</body></html>\n");
    out.close();
  }
}

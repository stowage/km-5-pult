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
    status[51] = "t4 > ��������� t4max";
    status[52] = "t4 < �������� t4min";
    status[53] = "������";
    status[54] = "Gv4 > ��������� Gv4max";
    status[55] = "Gv3 > ��������� Gv3max";
    status[56] = "����� \"�������� (����)\"";
    status[57] = "����� \"���� 1\"";
    status[58] = "����� \"���� 2\"";
    status[59] = "����� \"���� 3\"";
    status[60] = "����� \"��� ������\"";
    status[61] = "����� \"���������\"";
    status[62] = "������";
    status[63] = "������";
    status[64] = "U ������ G ��� > ����������� ���������";
    status[65] = "I ������� ��� > ����������� ���������";
    status[66] = "I ������� ��� < ����������� ��������";
    status[67] = "U ������ G ��-5 > ����������� ���������";
    status[68] = "I ������� ��-5 > ����������� ���������";
    status[69] = "I ������� ��-5 < ����������� ��������";
    status[70] = "Gv2 > 1.04*Gv2 (������ ��� ��-5-5)";
    status[71] = "t��* > ��������� t��max";
    status[72] = "������";
    status[73] = "T��* < �������� t��min";
    status[74] = "t2�* > ��������� t2�max";
    status[75] = "������";
    status[76] = "t2�* < �������� t2�min";
    status[77] = "ta > ���������    tamax (�� ��������� tamax = + 60��)";
    status[78] = "ta < �������� tamin (�� ��������� tamin = - 60��)";
    status[79] = "t��* > ��������� t��max";
    status[80] = "������";
    status[81] = "t��* < �������� t��min";
    status[82] = "Gv2 > ��������� Gv2max";
    status[83] = "������";
    status[84] = "Gv2 < �������� Gv2min";
    status[85] = "Gv1 > ��������� Gv1max";
    status[86] = "������";
    status[87] = "Gv1 < �������� Gv1min";
    status[88] = "t2�* > ��������� t2�max";
    status[89] = "������";
    status[90] = "t2�* < �������� t2�min";
    status[91] = "t1 �* > ��������� t1�max";
    status[92] = "������";
    status[93] = "t1 �* < �������� t1�min";
    status[94] = "t1 - t2 > ��������� dtmax";
    status[95] = "������";
    status[96] = "t1 - t2 < �������� dtmin";
    status[97] = "�������� Px < Pxmin";
    status[98] = "��� RESET ��� WATCHDOG";
    status[99] =
        "���� ��������� ��������� ���� �/��� ������� � RTC �������������";
    status[100] = "�������� Px > Pxmax";
    status[101] = "�������� P2 < P2min";
    status[102] = "��������� ������������ �� ���";
    status[103] = "���������� ������ �� ����� > ���������***";
    status[104] = "�������� P2 > P2max";
    status[105] = "�������� P1 < P1min";
    status[106] = "�������� �������� W < 0";
    status[107] = "������";
    status[108] = "�������� P1 > P1max";
    status[114] = "������ ������ � ���";
    status[115] = "����� � ���� ������� Px";
    status[116] = "����� � ���� ������� P2 ���";
    status[117] = "����� � ���� ������� P2 ��-5";
    status[118] = "����� � ���� ������� P1";
    status[119] = "������������� � ���� ��������������������� ���";
    status[120] = "������������� � ���� ��������������������� ��-5";
    status[121] = "������� �����";
    status[122] = "���� �������";
    status[123] = "������";
    status[124] = "������ ������ �� RTC";
    status[125] = "������ ������ � RTC";
    status[126] = "������ ������ �� EEPROM";
    status[127] = "������ ������ � EEPROM";

  }

  public static final String getErr(int code) {
    return code > 0 && code < status.length && status[code] != null ?
        status[code] : "����������� ������";
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
    out.write("<h3>����� �� ������ �" +
              StringUtils.formatDate(start_date, "dd.MM.yyyy") + " �� " +
              StringUtils.formatDate(end_date, "dd.MM.yyyy") + " (��:" +
              FormatUtils.getDBName(dbType) + ")</h3>");
    out.write("<table width=100% border=0>\n");
    out.write("<tr>\n");
    out.write("<td align=left>�����: " + deviceId +
              "</td>\n<td align=right>��� �������: ��-5-" +
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
      out.write("<td align=center>����</td>");
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
      out.write("<td align=center>������</td>");
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
          out.write("<td align=center><b>�����:</b></td>");
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
      out.write("<td align=center>����/�����</td>");
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
      out.write("<td align=center>���</td>");
      out.write("<td align=center>����/�����</td>");
      out.write("<td align=center>��������</td>");
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

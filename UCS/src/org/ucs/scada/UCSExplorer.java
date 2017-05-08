package org.ucs.scada;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import org.ucs.wrapper.CommandModule;
import org.ucs.wrapper.km5.CommandModuleImpl;
import org.ucs.wrapper.*;
import org.ucs.wrapper.km5.container.*;
import org.ucs.wrapper.km5.DirectConnector;
import java.util.Properties;
import org.ucs.*;
import java.io.*;
import java.sql.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UCSExplorer extends JFrame implements StatusController {
  public static String deviceId="";
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JTextField jtDeviceID = new JTextField();
  JLabel jLabel1 = new JLabel();
  JButton jbConnect = new JButton();
  JPanel jPanel2 = new JPanel();
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  TitledBorder titledBorder2;
  Pult pult = new Pult();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  Border border3;
  TitledBorder titledBorder3;
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel4 = new JPanel();
  JPanel jPanel5 = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JProgressBar jbProgress = new JProgressBar();
  JButton jbRead = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel2 = new JLabel();
  JComboBox jsDBType = new JComboBox();
  JLabel jLabel3 = new JLabel();
  org.freixas.jcalendar.JCalendarCombo jtStartDate = new org.freixas.jcalendar.JCalendarCombo();
  JLabel jLabel4 = new JLabel();
  org.freixas.jcalendar.JCalendarCombo jtEndDate = new org.freixas.jcalendar.JCalendarCombo();
  JButton jbReport = new JButton();
  JButton jbDisconnect = new JButton();
  JPanel jPanel6 = new JPanel();
  JLabel jlStatus = new JLabel();
  FlowLayout flowLayout3 = new FlowLayout();
  Border border4;

  public static CommandModule cmd;
  public static ConnectionModule conn;

  static {
    try {
      Class.forName("com.inet.tds.TdsDriver").newInstance();
    } catch (Exception ex) {}
  }

  public UCSExplorer() {
    try {
      jbInit();
      pult.setVisible(false);
      this.setSize(600, 360);
      this.setResizable(false);
      this.setTitle("UCS Explorer");
      jsDBType.addItem("���������");
      jsDBType.addItem("����������");
      jsDBType.addItem("����������");
      jsDBType.addItem("���������");
      jsDBType.addItem("������");

      jsDBType.setSelectedIndex(0);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
    UCSExplorer explorer = new UCSExplorer();
    explorer.setVisible(true);

  }
  private void jbInit() throws Exception {
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158));
    titledBorder1 = new TitledBorder(border1,"�����");
    border2 = BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158));
    titledBorder2 = new TitledBorder(border2,"������");
    border3 = BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158));
    titledBorder3 = new TitledBorder(border3,"������");
    border4 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(109, 109, 110),new Color(156, 156, 158)),BorderFactory.createEmptyBorder(2,2,2,2));
    this.getContentPane().setLayout(borderLayout1);
    jPanel1.setLayout(flowLayout1);
    jtDeviceID.setDebugGraphicsOptions(0);
    jtDeviceID.setPreferredSize(new Dimension(60, 21));
    jtDeviceID.setText("");
    flowLayout1.setAlignment(FlowLayout.LEFT);
    jLabel1.setText("����� �������:");
    jbConnect.setText("���������");
    jbConnect.addActionListener(new UCSExplorer_jbConnect_actionAdapter(this));
    jPanel2.setLayout(borderLayout4);
    jPanel2.setBorder(null);
    jPanel3.setBorder(titledBorder3);
    jPanel3.setDebugGraphicsOptions(0);
    jPanel3.setLayout(borderLayout2);
    jPanel4.setLayout(flowLayout2);
    jbRead.setEnabled(false);
    jbRead.setText("������� ������");
    jbRead.addActionListener(new UCSExplorer_jbRead_actionAdapter(this));
    jPanel5.setLayout(gridBagLayout1);
    jsDBType.setEnabled(true);
    jLabel2.setText("���� ������:");
    jsDBType.setPreferredSize(new Dimension(96, 21));
    jLabel3.setText("� ����:");
    jLabel4.setText("�� ����:");
    jtStartDate.setMinimumSize(new Dimension(26, 21));
    jbProgress.setPreferredSize(new Dimension(120, 16));
    jbProgress.setString("0%");
    jbReport.setEnabled(false);
    jbReport.setText("������������ �����");
    jbReport.addActionListener(new UCSExplorer_jbReport_actionAdapter(this));
    jbDisconnect.setEnabled(false);
    jbDisconnect.setToolTipText("");
    jbDisconnect.setText("�����������");
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.addWindowListener(new UCSExplorer_this_windowAdapter(this));
    jPanel6.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(5);
    jlStatus.setFont(new java.awt.Font("Dialog", 0, 12));
    jlStatus.setMaximumSize(new Dimension(300, 16));
    jlStatus.setMinimumSize(new Dimension(129, 16));
    jlStatus.setPreferredSize(new Dimension(300, 16));
    jlStatus.setToolTipText("");
    jlStatus.setText("...");
    jPanel6.setBorder(border4);
    this.getContentPane().add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel1, null);
    jPanel1.add(jtDeviceID, null);
    jPanel1.add(jbConnect, null);
    jPanel1.add(jbDisconnect, null);
    this.getContentPane().add(jPanel2,  BorderLayout.CENTER);
    jPanel2.add(pult,  BorderLayout.WEST);
    jPanel2.add(jPanel3, BorderLayout.CENTER);
    jPanel3.add(jPanel4, BorderLayout.NORTH);
    jPanel4.add(jbProgress, null);
    jPanel4.add(jbRead, null);
    jPanel3.add(jPanel5, BorderLayout.CENTER);
    jPanel5.add(jLabel2,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel5.add(jsDBType,       new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel5.add(jLabel3,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel5.add(jtStartDate,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel5.add(jLabel4,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel5.add(jtEndDate,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel5.add(jbReport,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(13, 0, 0, 0), 0, 0));
    this.getContentPane().add(jPanel6,  BorderLayout.SOUTH);
    jPanel6.add(jlStatus, null);
  }

  FileWriter log;

  public void open(String logFileName) throws IOException {
    log = new FileWriter(logFileName, false);
  }

  public void close() throws IOException {
    log.flush();
    log.close();
    log = null;
  }

  public void setBounds(int min, int max) {
    jbProgress.setMinimum(min);
    jbProgress.setMaximum(max);
  }
  public void adjustProgress(int cur) {
    jbProgress.setValue(cur);
  }

  public void write(String str) throws IOException {
    log.write("["+StringUtils.formatDate(new java.util.Date(),"MM-dd-yy HH:mm:ss")+"] "+str);
    log.flush();
    jlStatus.setText(str);
  }
  public void writeln(String str) throws IOException {
    log.write("["+StringUtils.formatDate(new java.util.Date(),"MM-dd-yy HH:mm:ss")+"] "+str+"\r\n");
    log.flush();
    jlStatus.setText(str);
  }

  void jbConnect_actionPerformed(ActionEvent e) {

    conn = new DirectConnector();
    Properties prop = new Properties();
    prop.setProperty("SERIAL_PORT","COM1");
    prop.setProperty("DRIVER_VERSION", "KM5 1.0");
    prop.setProperty("TIMEOUT","5000");
    prop.setProperty("BAUD_RATE","9600");
    prop.setProperty("DATABITS","8");
    prop.setProperty("STOPBITS","2");
    prop.setProperty("PARITY","None");

    try {
      conn.connect(prop);

      CommandModuleImpl cmdi = new CommandModuleImpl(conn);
      Object trans = (((Class)Class.forName("org.ucs.wrapper.km5.TransactionChecker")).newInstance());
      Transaction normalT = new Transaction(conn, 10 , 10, true);
      //normalT.setTransactionReanimator((TransactionReanimator)trans);
      normalT.setTransactionValidater((TransactionValidater)trans);

      cmd =
            CommandTransaction.DTI(cmdi, normalT);
      String zeros = "";
      if(jtDeviceID.getText().length()<8) {

        for(int i=0;i<8-jtDeviceID.getText().length();i++) {
          zeros+="0";
        }

      }
      deviceId = zeros+jtDeviceID.getText();


      pult.startThread();
      jlStatus.setText("���������� �����������...");
      jbConnect.setEnabled(false);
      jtDeviceID.setEnabled(false);
      jbDisconnect.setEnabled(true);
      jbRead.setEnabled(true);
      jbReport.setEnabled(true);
      pult.setVisible(true);
    } catch (Exception ex) {
      jlStatus.setText(ex.toString());
      if(conn!=null) {
        conn.disconnect();
      }

    }
  }

  void this_windowClosing(WindowEvent e) {

  }

  void this_windowClosed(WindowEvent e) {
    if(conn!=null) {
      conn.disconnect();
    }
    System.exit(0);
  }

  void jbRead_actionPerformed(ActionEvent e) {
    UCSExplorer.this.pult.setVisible(false);
    UCSExplorer.this.jbReport.setEnabled(false);
    UCSExplorer.this.jbRead.setEnabled(false);
    new Thread(new Runnable() {
      public void run() {
        Connection cnt= null;
        try {


          cnt = DriverManager.getConnection(
              "jdbc:inetdae7:notebook:1444?database=ucs&user=sa&password=libor");

          CallableStatement stm = cnt.prepareCall("{call truncateTables()}");
          stm.execute();
          stm.close();

          UCSDBDataAccess da = new UCSDBDataAccess(cnt);
          UCSDataLoader dl = new UCSDataLoader(cmd,
                                               UCSExplorer.deviceId);

          dl.load(da, UCSExplorer.this);

        } catch (Exception ex) {
          UCSExplorer.this.jlStatus.setText(ex.toString());
        } finally {
          UCSExplorer.this.pult.setVisible(true);
          UCSExplorer.this.jbReport.setEnabled(true);
          UCSExplorer.this.jbRead.setEnabled(true);
          if(cnt!=null) {
            try {
              cnt.close();
            } catch (Exception e) {}
          }
        }
      }
    }).start();

  }

  void jbReport_actionPerformed(ActionEvent e) {
    File f=new File("xreport"+jsDBType.getSelectedIndex()+"-"+StringUtils.formatDate(new java.util.Date(),"MMddyy-HHmmss")+".xls");
    Connection cnt= null;
    try {

      cnt = DriverManager.getConnection(
          "jdbc:inetdae7:notebook:1444?database=ucs&user=sa&password=libor");

      DataReport dr = new DataReport(cnt, cmd);
      dr.generateReport(f, deviceId,jsDBType.getSelectedIndex(),this.jtStartDate.getDate(),this.jtEndDate.getDate());

      Runtime.getRuntime().exec("\"C:\\Program Files\\Microsoft Office\\Office\\EXCEL.EXE\" "+f.getAbsolutePath());
    } catch (Exception ex) {
      UCSExplorer.this.jlStatus.setText(ex.toString());
    } finally {
      if(cnt!=null) {
        try {
          cnt.close();
        } catch (Exception ex2) {}
      }
    }

  }

}

class UCSExplorer_jbConnect_actionAdapter implements java.awt.event.ActionListener {
  UCSExplorer adaptee;

  UCSExplorer_jbConnect_actionAdapter(UCSExplorer adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jbConnect_actionPerformed(e);
  }
}

class UCSExplorer_this_windowAdapter extends java.awt.event.WindowAdapter {
  UCSExplorer adaptee;

  UCSExplorer_this_windowAdapter(UCSExplorer adaptee) {
    this.adaptee = adaptee;
  }
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
  public void windowClosed(WindowEvent e) {
    adaptee.this_windowClosed(e);
  }
}

class UCSExplorer_jbRead_actionAdapter implements java.awt.event.ActionListener {
  UCSExplorer adaptee;

  UCSExplorer_jbRead_actionAdapter(UCSExplorer adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jbRead_actionPerformed(e);
  }
}

class
        UCSExplorer_jbReport_actionAdapter implements java.awt.event.ActionListener {
  UCSExplorer adaptee;

  UCSExplorer_jbReport_actionAdapter(UCSExplorer adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jbReport_actionPerformed(e);
  }
}
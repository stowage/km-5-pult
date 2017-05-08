package org.ucs.scada;

import org.ucs.wrapper.*;
import org.ucs.wrapper.km5.DirectConnector;
import org.ucs.wrapper.km5.CommandModuleImpl;
import org.ucs.wrapper.km5.container.ReadMonitorContainer;
import org.ucs.wrapper.km5.container.ReadCheckContainer;
import org.ucs.wrapper.km5.container.ReadVersionContainer;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.Properties;
import java.lang.reflect.Method;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Pult
    extends JPanel {
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JTextField jtMonitor = new JTextField();
  JButton jbCancel = new JButton();
  JButton jbSystem = new JButton();
  JButton jbEnter = new JButton();
  JButton jbLeft = new JButton();
  JButton jbDown = new JButton();
  JButton jbRight = new JButton();
  TitledBorder titledBorder1;
  Thread t1;
  ReadMonitorContainer monitor = new ReadMonitorContainer();
  Border border1;
  TitledBorder titledBorder2;
  ImageIcon image1;

  public void startThread() {
    t1 = new Thread(new Runnable() {
      public void run() {
        String prevDisp = "";
        int curPos = -1;

        CommandModule cmd = UCSExplorer.cmd;

        while (true) {
          long tm1 = System.currentTimeMillis();
          synchronized (cmd) {
            try {
              cmd.cmdReadMonitor(monitor, UCSExplorer.deviceId);

              if (!prevDisp.equals(monitor.display) || curPos != monitor.curPos) {
                jtMonitor.setText(monitor.display);
                if (monitor.curPos >= 0 && monitor.curPos < 16) {
                  jtMonitor.select(monitor.curPos, monitor.curPos + 1);
                  jtMonitor.grabFocus();
                }
              }

              curPos = monitor.curPos;
              prevDisp = monitor.display;

            }
            catch (Exception ex) {
              ex.printStackTrace(System.out);
            }
          }
          long dt = System.currentTimeMillis() - tm1;
          try {
            Thread.sleep(dt>=900?100:1000 - dt);
          }
          catch (Exception ex) {break;}

        }

      }
    });

    //t1.setDaemon(true);
    t1.start();

  }

  public Pult() {
    try {
      jbInit();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.
        white, new Color(156, 156, 158)), "Пульт");
    border1 = BorderFactory.createEtchedBorder(Color.white,
                                               new Color(156, 156, 158));
    titledBorder2 = new TitledBorder(border1, "Позиционирование");
    jtMonitor.setBackground(new Color(173, 194, 255));
    jtMonitor.setFont(new java.awt.Font("SansSerif", 1, 16));
    jtMonitor.setForeground(new Color(0, 0, 130));
    jtMonitor.setDoubleBuffered(true);
    jtMonitor.setPreferredSize(new Dimension(232, 40));
    jtMonitor.setRequestFocusEnabled(true);
    jtMonitor.setToolTipText("");
    jtMonitor.setDisabledTextColor(new Color(0, 0, 130));
    jtMonitor.setEditable(false);
    jtMonitor.setSelectedTextColor(new Color(100, 130, 100));
    jtMonitor.setSelectionColor(new Color(0, 0, 130));
    jtMonitor.setText("ВЫКЛЮЧЕН");
    jtMonitor.setColumns(24);
    jtMonitor.setHorizontalAlignment(SwingConstants.CENTER);
    jtMonitor.addMouseListener(new Pult_jtMonitor_mouseAdapter(this));
    this.setLayout(gridBagLayout1);
    jbCancel.setMaximumSize(new Dimension(70, 24));
    jbCancel.setMinimumSize(new Dimension(70, 24));
    jbCancel.setPreferredSize(new Dimension(78, 24));
    jbCancel.setMargin(new Insets(2, 8, 2, 8));
    jbCancel.setText("Отмена");
    jbCancel.addActionListener(new Pult_jbCancel_actionAdapter(this));
    jbSystem.setMaximumSize(new Dimension(70, 24));
    jbSystem.setMinimumSize(new Dimension(70, 24));
    jbSystem.setOpaque(true);
    jbSystem.setPreferredSize(new Dimension(78, 24));
    jbSystem.setHorizontalAlignment(SwingConstants.CENTER);
    jbSystem.setHorizontalTextPosition(SwingConstants.TRAILING);
    jbSystem.setMargin(new Insets(2, 2, 2, 2));
    jbSystem.setText("Система");
    jbSystem.addActionListener(new Pult_jbSystem_actionAdapter(this));
    jbEnter.setMaximumSize(new Dimension(70, 24));
    jbEnter.setMinimumSize(new Dimension(70, 24));
    jbEnter.setPreferredSize(new Dimension(78, 24));
    jbEnter.setMargin(new Insets(2, 8, 2, 8));
    jbEnter.setText("Ввод");
    jbEnter.addActionListener(new Pult_jbEnter_actionAdapter(this));
    jbLeft.setMaximumSize(new Dimension(70, 70));
    jbLeft.setMinimumSize(new Dimension(70, 70));
    jbLeft.setPreferredSize(new Dimension(78, 84));
    jbLeft.setMargin(new Insets(2, 4, 2, 4));
    jbLeft.setText("Влево");
    jbLeft.addActionListener(new Pult_jbLeft_actionAdapter(this));
    jbDown.setMaximumSize(new Dimension(70, 70));
    jbDown.setMinimumSize(new Dimension(70, 70));
    jbDown.setPreferredSize(new Dimension(78, 84));
    jbDown.setSelected(false);
    jbDown.setText("Вниз");
    jbDown.addActionListener(new Pult_jbDown_actionAdapter(this));
    jbRight.setMaximumSize(new Dimension(70, 70));
    jbRight.setMinimumSize(new Dimension(70, 70));
    jbRight.setPreferredSize(new Dimension(78, 84));
    jbRight.setMargin(new Insets(2, 8, 2, 8));
    jbRight.setText("Вправо");
    jbRight.addActionListener(new Pult_jbRight_actionAdapter(this));
    this.setBorder(titledBorder1);
    this.add(jtMonitor, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), -103, 0));
    this.add(jbCancel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.NONE,
                                              new Insets(0, 0, 0, 0), 0, 0));
    this.add(jbEnter, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
                                             , GridBagConstraints.WEST,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 0, 0, 0), 0, 0));
    this.add(jbLeft, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.NONE,
                                            new Insets(0, 0, 22, 0), 0, -26));
    this.add(jbDown, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.EAST,
                                            GridBagConstraints.NONE,
                                            new Insets(0, 0, 22, 0), 0, -26));
    this.add(jbRight, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
                                             , GridBagConstraints.WEST,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 0, 20, 0), 0, -27));
    this.add(jbSystem, new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0
                                              , GridBagConstraints.EAST,
                                              GridBagConstraints.NONE,
                                              new Insets(0, 0, 0, 0), 0, 0));
  }

  void pressKey(int code) throws Exception {
    CommandModule cmd = UCSExplorer.cmd;

    synchronized (cmd) {
      cmd.cmdSendKey(monitor, UCSExplorer.deviceId, (byte) code);
      if (!monitor.checkSum)
        System.out.println("INVALID_CHECKSUM");
    }

    Thread.sleep(100);

    synchronized (cmd) {

      cmd.cmdReadMonitor(monitor, UCSExplorer.deviceId);
      jtMonitor.setText(monitor.display);
      if (monitor.curPos >= 0 && monitor.curPos < 16) {
        jtMonitor.select(monitor.curPos, monitor.curPos + 1);
        jtMonitor.grabFocus();
      }
      else {
        //jtMonitor.grabFocus();
      }
    }

  }

  void jbEnter_actionPerformed(ActionEvent e) {
    try {
      pressKey(0x0C);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }

  void jbLeft_actionPerformed(ActionEvent e) {
    try {
      pressKey(0x01);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }

  void jbDown_actionPerformed(ActionEvent e) {
    try {
      pressKey(0x02);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }

  void jbRight_actionPerformed(ActionEvent e) {
    try {
      pressKey(0x04);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }

  void jbSystem_actionPerformed(ActionEvent e) {
    try {
      pressKey(0x0A);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }

  void jbCancel_actionPerformed(ActionEvent e) {
    try {
      pressKey(0x09);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }

  void jtMonitor_mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) {

    }

  }

}

class Pult_jbEnter_actionAdapter
    implements java.awt.event.ActionListener {
  Pult adaptee;

  Pult_jbEnter_actionAdapter(Pult adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jbEnter_actionPerformed(e);
  }
}

class Pult_jbLeft_actionAdapter
    implements java.awt.event.ActionListener {
  Pult adaptee;

  Pult_jbLeft_actionAdapter(Pult adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jbLeft_actionPerformed(e);
  }
}

class Pult_jbDown_actionAdapter
    implements java.awt.event.ActionListener {
  Pult adaptee;

  Pult_jbDown_actionAdapter(Pult adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jbDown_actionPerformed(e);
  }
}

class Pult_jbRight_actionAdapter
    implements java.awt.event.ActionListener {
  Pult adaptee;

  Pult_jbRight_actionAdapter(Pult adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jbRight_actionPerformed(e);
  }
}

class Pult_jbSystem_actionAdapter
    implements java.awt.event.ActionListener {
  Pult adaptee;

  Pult_jbSystem_actionAdapter(Pult adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jbSystem_actionPerformed(e);
  }
}

class Pult_jbCancel_actionAdapter
    implements java.awt.event.ActionListener {
  Pult adaptee;

  Pult_jbCancel_actionAdapter(Pult adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jbCancel_actionPerformed(e);
  }
}

class Pult_jtMonitor_mouseAdapter
    extends java.awt.event.MouseAdapter {
  Pult adaptee;

  Pult_jtMonitor_mouseAdapter(Pult adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.jtMonitor_mouseClicked(e);
  }
}
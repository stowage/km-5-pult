package org.ucs.wrapper.km5;

import org.ucs.wrapper.ConnectionModule;
import org.ucs.wrapper.SessionModule;
import org.ucs.wrapper.Extractor;
import org.ucs.wrapper.IOHelper;

import javax.comm.*;
import java.util.Properties;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: landuser
 * Date: 19.06.2005
 * Time: 23:11:06
 * To change this template use File | Settings | File Templates.
 */
public class ModemConnector /*implements ConnectionModule, SerialPortEventListener */{
/*
    private CommPortIdentifier portId;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;

    public void connect(Properties prop) throws Exception {

        String port = prop.getProperty("SERIAL_PORT");
        String driverVersion = prop.getProperty("DRIVER_VERSION");
        int connectionTimeout = Extractor.getInt(prop.getProperty("TIMEOUT"));
        int baudRate =   Extractor.getInt(prop.getProperty("BAUD_RATE"));
        int dataBits =   Extractor.getInt(prop.getProperty("DATABITS"));
        String stopBits = prop.getProperty("STOPBITS");
        String parityCheck = prop.getProperty("PARITY");

        if(!detectPort(port))
            throw new RuntimeException("Invalid port exception!");

        if(isConnected(prop))
            throw new RuntimeException("Already connected!");
        else
            disconnect();

        try {
            serialPort = (SerialPort)
                    portId.open(driverVersion + "(" + port + ")", connectionTimeout);
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
            throw new RuntimeException("Unable to open serial port!");
        };

        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
            throw new RuntimeException("Unable to resolve data stream!");
        }

        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);
            serialPort.notifyOnCarrierDetect(true);
            serialPort.notifyOnCTS(true);
            serialPort.notifyOnDSR(true);
            serialPort.notifyOnFramingError(true);
            serialPort.notifyOnOutputEmpty(true);
            serialPort.notifyOnOverrunError(true);
            serialPort.notifyOnParityError(true);
            serialPort.notifyOnRingIndicator(true);

        } catch (TooManyListenersException e) {
            e.printStackTrace();
            disconnect();
            throw new RuntimeException("Unable to setup runtime notifications!");
        }

        int baud_rate = serialPort.getBaudRate();
        if (baudRate>0) {
            baud_rate = baudRate;
        }

        int data_bits = serialPort.getDataBits();
        switch(dataBits) {
            case 5: data_bits = SerialPort.DATABITS_5; break;
            case 6: data_bits = SerialPort.DATABITS_6; break;
            case 7: data_bits = SerialPort.DATABITS_7; break;
            case 8: data_bits = SerialPort.DATABITS_8; break;
            default:
                disconnect();
                throw new RuntimeException("Ivalid databits parameter!");
        }

        int stop_bits = serialPort.getStopBits();
        if(stopBits.equals("1"))
            stop_bits = SerialPort.STOPBITS_1;
        else if(stopBits.equals("1.5"))
            stop_bits = SerialPort.STOPBITS_1_5;
        else if(stopBits.equals("2"))
            stop_bits = SerialPort.STOPBITS_2;
        else {
            disconnect();
            throw new RuntimeException("Invalid stopbits parameter!");
        }

        int parity = serialPort.getParity();
        if(parityCheck.equals("None"))
            parity = SerialPort.PARITY_NONE;
        else if(parityCheck.equals("Even"))
            parity = SerialPort.PARITY_EVEN;
        else if(parityCheck.equals("Odd"))
            parity = SerialPort.PARITY_ODD;
        else if(parityCheck.equals("Mark"))
            parity = SerialPort.PARITY_MARK;
        else if(parityCheck.equals("Space"))
            parity = SerialPort.PARITY_SPACE;
        else {
            disconnect();
            throw new RuntimeException("Invalid parity parameter!");
        }

        try {
            serialPort.setSerialPortParams(baud_rate,
                    data_bits,
                    stop_bits,
                    parity);
        }
        catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
            disconnect();
            throw new RuntimeException("Unable to initialize serial parameters!");
        }

        ////
       try {
          System.out.println("Try to connect to modem");
          Thread.sleep(1000);
          ByteArrayOutputStream bs = new ByteArrayOutputStream();
          String dialUpLine = "ATZ\r\n\n";
          for(int i=0;i<dialUpLine.length();i++) {
            bs.write(dialUpLine.charAt(i));
          }
          //TODO : IOHelper
          IOHelper.writeBytes(bs.toByteArray());
          Thread.sleep(1000);
          bs.reset();
          dialUpLine = "ATDP 3021979\r\n\n";
          for(int i=0;i<dialUpLine.length();i++) {
            bs.write(dialUpLine.charAt(i));
          }
          //TODO
          IOHelper.writeBytes(outputStream,bs.toByteArray());

        } catch (Exception ex) {
          ex.printStackTrace(System.out);
        }
    }

    private boolean detectPort(String port) {
        boolean wasFound = false;

        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getName().equals(port) && portId.getPortType()==CommPortIdentifier.PORT_SERIAL) {
                wasFound = true;
                break;
            }
        }

        return wasFound;
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:   try {

//VVV
                  BufferedReader is = new BufferedReader (new InputStreamReader(inputStream));
                  String inputLine;

                  byte[] ComByte = {10, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 1};
                  System.out.println(ComByte + "-Array");//[B@125d06e


                  boolean cmp = false;
                  while (((inputStream.available())  != 0)&(!cmp)) {
                    inputLine = is.readLine();
                    System.out.println(inputLine);
                    String stCommand = "CONNECT";
                    //int cmp = inputLine.compareTo(stCommand);

                    int i = 0;
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    while ((i<(inputLine.length()))&(!cmp)){
                      cmp = inputLine.regionMatches(true, i, stCommand, 0, 7);
                      i++;
                    }
                    if (cmp) {
                      System.out.println("Ok, We is dealing  ");
                      Thread.sleep(5000);




          Thread.sleep(1000);
                    //TODO
                    IOHelper.writeBytes(outputStream,ComByte);
                      System.out.println("command sended");
                      //StringBuffer sb = new StringBuffer();
                     Thread.sleep(1000);





                    }
                      else System.out.println("It isn't no carrier");


                  }
Thread.sleep(1000);
                  System.out.println("Мы снаружи");
                  while (inputStream.available() > 0) {
                        System.out.println("Мы внутри");
                    byte[] b = new byte[16];
                         int len = inputStream.read(b);
                         System.out.println(len);

                      for(int g=0; g<len; g++) {
                      System.out.println(b[g]);
                  }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

                break;
        }
    }


    public void disconnect() {
        if(serialPort==null)
            return;

        try {
            serialPort.removeEventListener();
            serialPort.close();
            serialPort = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }

    public void kill() {

    }

    public SessionModule getSession() throws Exception {
        return null;
    }

    public boolean isConnected(Properties prop){
        if(portId==null)
            return false;
        String port = prop.getProperty("SERIAL_PORT");
        String driverVersion = prop.getProperty("DRIVER_VERSION");
        return portId.isCurrentlyOwned() && portId.getCurrentOwner()!=null && portId.getCurrentOwner().equals(driverVersion+"("+port+")");

    }

    public void run() {

    }*/
}

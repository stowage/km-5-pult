package org.ucs.wrapper.km5;

import org.ucs.wrapper.*;

import javax.comm.*;
import java.util.Properties;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.ArrayList;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: landuser
 * Date: 19.06.2005
 * Time: 23:11:06
 * To change this template use File | Settings | File Templates.
 */
public class DirectConnector implements ConnectionModule, SerialPortEventListener{

    private CommPortIdentifier portId;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
    SessionModule session;
    Properties prop;
    byte[] buffer = new byte[256];
    byte[] dummyBuffer = new byte[256];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    volatile boolean bufferEmpty = true;
    volatile boolean wasCTS = true;

    public DirectConnector() {
    }

    public int getMode() {
        return IOHelperInterface.MODE_FORWARD;
    }

    public void connect(Properties prop) throws Exception {
        this.prop = prop;

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
            //return;
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

        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();

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

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.disableReceiveFraming();
            serialPort.disableReceiveThreshold();
            serialPort.disableReceiveTimeout();



        }
        catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
            disconnect();
            throw new RuntimeException("Unable to initialize serial parameters!");
        }

    }

    private boolean detectPort(String port) {
        boolean wasFound = false;

        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println(portId.getName());
            if (portId.getName().equals(port) && portId.getPortType()==CommPortIdentifier.PORT_SERIAL) {

                wasFound = true;
                break;
            }
        }

        return wasFound;
    }

    public ArrayList getSources() {
        ArrayList al = new ArrayList();
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();

            if (portId.getPortType()==CommPortIdentifier.PORT_SERIAL) {
                al.add(portId.getName());
                break;
            }
        }
        return al;
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
                break;
            case SerialPortEvent.CTS:
            case SerialPortEvent.DATA_AVAILABLE:
                try {

                    while(inputStream.available()>0) {

                        int len = inputStream.read(buffer);
                        baos.write(buffer, 0, len);

                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.out);
                }

                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                bufferEmpty = true;
                break;

        }
    }


    public void disconnect() {
        if(portId==null)
            return;

        if(serialPort==null)
            return;

        try {

            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                }
                catch (IOException e) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                }
                catch (IOException e) {}
            }

            serialPort.close();
            serialPort = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }

    public void kill() {
        System.exit(0);
    }

    public Properties getProperties() {
        return prop;
    }

    public void setProperties(Properties prop) {
        this.prop = prop;
    }

    public SessionModule getSession() throws Exception {
        if(serialPort==null)
            throw new Exception("Not connected");


        return session==null?session = new SessionModule() {

            private int timeout;
            private int responseSize;

            public void setConstraint(int timeout, int responseSize) throws Exception{
                this.timeout = timeout;
                this.responseSize = responseSize;
            }

            public InputStream getInput() throws IOException {
                if(DirectConnector.this.inputStream==null)
                    throw new IOException("Input stream is null");


                while(/*!bufferEmpty && */!serialPort.isCTS()) {
                    try {
                        Thread.sleep(5);
                    } catch (Exception ex) {

                    }
                }


                long t1 = System.currentTimeMillis();
                while(baos.size()<responseSize) {
                    long dt = System.currentTimeMillis() - t1;
                    try {
                        Thread.sleep(5);
                    } catch (Exception ex) {}
                    if((baos.size()==0 && dt>timeout) || dt>2*timeout) break;
                }

                //System.out.println("///////////New Command DONE");
                if(baos.size()==0) {
                    IOHelper.dump(baos.toByteArray(), "Not completed");
                    throw new IOException("Buffer not completed:"+baos.size());
                }

                return new ByteArrayInputStream(DirectConnector.this.baos.toByteArray());
            }

            public OutputStream getOutput() throws IOException {
                if(DirectConnector.this.outputStream==null)
                    throw new IOException("Output stream is null");

                serialPort.setRTS(true);
                serialPort.setDTR(true);

                while(inputStream.available()>0) {
                    inputStream.read(dummyBuffer);
                }


                while(/*!bufferEmpty && */!serialPort.isCTS()) {
                    try {
                        Thread.sleep(5);
                    } catch (Exception ex) {

                    }
                }


                bufferEmpty = false;
                baos.reset();
                wasCTS = false;
                //System.out.println("///////////New Command");

                return DirectConnector.this.outputStream;
            }

        }:session;
    }

    public boolean isConnected(Properties prop){
        if(portId==null)
            return false;
        String port = prop.getProperty("SERIAL_PORT");
        String driverVersion = prop.getProperty("DRIVER_VERSION");
        return (portId.isCurrentlyOwned() && portId.getCurrentOwner()!=null && portId.getCurrentOwner().equals(driverVersion+"("+port+")"));

    }

}

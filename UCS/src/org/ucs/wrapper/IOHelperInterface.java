package org.ucs.wrapper;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 19.06.2005
 * Time: 17:15:39
 * To change this template use File | Settings | File Templates.
 */
public interface IOHelperInterface  {

    public static final int MODE_FORWARD = 0;
    public static final int MODE_BACKWARD = 1;

    public static final long DEFAULT_COMMAND_TIMEOUT = 5000;

       
    public void setResponseTimeout(long timeout);
    public long getResponseTimeout();

    //Writing data to internal buffer
    void writeBCDAddr(String value) throws IOException;
    void writeUnsignedByte(byte value) throws IOException;
    void writeByte(byte value) throws IOException;
    void writeBCDByte(String v) throws IOException;
    void writeBytes(byte[] value) throws IOException;
    void writeFloat(float value) throws IOException;
    void writeBCDDate(long value) throws IOException;
    void writeBCDTime(long value) throws IOException;
    void writeShort(int value) throws IOException;
    void writeInt(int value) throws IOException;
    void writeChecksum() throws IOException;

    String readBCDAddr() throws IOException;
    int readUnsignedByte() throws IOException;
    byte readByte() throws IOException;
    float readFloat() throws IOException;
    long readBCDDate() throws IOException;
    long readBCDTime() throws IOException;
    short readShort() throws IOException;
    String readBCDByte() throws IOException;
    int readInt() throws IOException;
    String readASCII(int numOfBytes)throws IOException;
    String readDisplay(int numOfBytes)throws IOException;
    boolean readChecksum() throws IOException;

    void writeToOut(OutputStream out, int size, int mode) throws IOException;
    void readIn(InputStream in, int size, int mode) throws IOException;


    void clearOut() throws IOException;
    void clearIn() throws IOException;
}

package org.ucs.wrapper;

import java.io.*;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 19.06.2005
 * Time: 17:42:15
 * To change this template use File | Settings | File Templates.
 */
public class IOHelper implements IOHelperInterface {

    protected static String display = "0123456789 ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕÖ×ØÙÚÛÜİŞßDF!%()*+,-./:;<=>?NS<GIJLRUVWYZQàáâãäå¸æçèéêëìíîïğñòóôõö÷øùúûüışÿbdfghijklmnqrstuvwz";

    protected byte[] buffer = null;
    protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protected ByteArrayInputStream bais = null;

    public IOHelper() {
    }

    private long timeout = DEFAULT_COMMAND_TIMEOUT;

    public void setResponseTimeout(long timeout) {
        this.timeout = timeout;
    }
    public long getResponseTimeout() {
        return timeout;
    }

    public void writeBCDByte(String v) throws IOException {
        baos.write(Byte.parseByte(v, 16));
    }

    public void writeBCDAddr(String value) throws IOException {
        writeBCDByte(value.substring(6, 8));
        writeBCDByte(value.substring(4, 6));
        writeBCDByte(value.substring(2, 4));
        writeBCDByte(value.substring(0, 2));

    }

    public void writeUnsignedByte(byte value) throws IOException {
        baos.write(((int)value) & 0xFF);
    }

    public void writeByte(byte value) throws IOException {
        baos.write(value);
    }
    public void writeBytes(byte[] value) throws IOException {
        baos.write(value);
    }

    public void writeFloat(float value) throws IOException {
        writeInt(Float.floatToIntBits(value));
    }

    public void writeBCDDate(long value) throws IOException {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(value);
        writeBCDByte(Integer.toString(cl.get(Calendar.DAY_OF_MONTH)));
        writeBCDByte(Integer.toString(cl.get(Calendar.MONTH)));
        writeBCDByte(Integer.toString(cl.get(Calendar.YEAR)));
    }

    public void writeBCDTime(long value) throws IOException {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(value);
        writeBCDByte(Integer.toString(cl.get(Calendar.HOUR_OF_DAY)));
        writeBCDByte(Integer.toString(cl.get(Calendar.MINUTE)));
        writeBCDByte(Integer.toString(cl.get(Calendar.SECOND)));
    }

    public void writeInt(int value)throws IOException{
        baos.write((value >>> 0) & 0xFF);
        baos.write((value >>> 8) & 0xFF);
        baos.write((value >>> 16) & 0xFF);
        baos.write((value >>> 24) & 0xFF);

    }

    public void writeShort(int value)throws IOException{
        baos.write((value >>> 0) & 0xFF);
        baos.write((value >>> 8) & 0xFF);

    }

    public void writeChecksum() {
        byte[] b = baos.toByteArray();
        int cs1 = 0;
        int cs2 = 0;
        for(int i=0;i<b.length;i++) {
            cs1 = cs1 ^ b[i];
            cs2 += b[i] & 0xFF;
        }
        baos.write(cs1);
        baos.write(cs2 % 256);

    }

    public String readBCDByte() throws IOException {
        int ch = bais.read();
        String bcd_byte = Integer.toHexString(ch);
        if(bcd_byte.length() == 1)
            bcd_byte = "0" + bcd_byte;
        return bcd_byte;
    }


    public String readBCDAddr() throws IOException {
        String ch4 = readBCDByte();
        String ch3 = readBCDByte();
        String ch2 = readBCDByte();
        String ch1 = readBCDByte();
        return ch1 + ch2 + ch3 + ch4;
    }

    public int readUnsignedByte() throws IOException {
        int ch = (int)(bais.read()) & 0xFF;
        return ch;
    }

    public byte readByte() throws IOException{
        int ch = bais.read();
        return (byte) (ch);
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public long readBCDDate() throws IOException {
        int day_of_month = Integer.parseInt(readBCDByte());
        int month = Integer.parseInt(readBCDByte());
        int year = Integer.parseInt(readBCDByte());
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.DAY_OF_MONTH, day_of_month);
        cl.set(Calendar.MONTH, month);
        cl.set(Calendar.YEAR, year);
        return cl.getTimeInMillis();
    }

    public long readBCDTime() throws IOException {
        int hour = Integer.parseInt(readBCDByte());
        int minute = Integer.parseInt(readBCDByte());
        int seconds = Integer.parseInt(readBCDByte());
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.HOUR_OF_DAY, hour);
        cl.set(Calendar.MINUTE, minute);
        cl.set(Calendar.SECOND, seconds);
        return cl.getTimeInMillis();

    }

    public short readShort() throws IOException {
        int ch2 = bais.read();
        int ch1 = bais.read();
        return (short)((ch1 << 8) + (ch2 << 0));

    }

    public int readInt() throws IOException {
        int ch4 = bais.read();
        int ch3 = bais.read();
        int ch2 = bais.read();
        int ch1 = bais.read();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

    }

    public String readDisplay(int numOfBytes) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < numOfBytes; i++) {
            int pos = (int) (bais.read() & 0xFF);
            if(pos<display.length()) {
                char ch = display.charAt(pos);
                sb.append( (char) ch);
            } else
                sb.append(' ');
        }
        return sb.toString();

    }

    public String readASCII(int numOfBytes) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < numOfBytes; i++) {
            int ch = bais.read();
            sb.append( (char) ch);
        }
        return sb.toString();
    }

    public boolean readChecksum() throws IOException{
        int cs1 = 0;
        int cs2 = 0;
        boolean flag = true;
        for(int i = 0;i<buffer.length-2;i++) {
            if(flag) {
                cs1 = buffer[i];
                flag = false;
            } else
                cs1 = cs1 ^ buffer[i];

            cs2 += buffer[i];
        }

        //System.out.println("Buffer length:"+buffer.length);
        System.out.println("Checksum:"+(buffer[buffer.length-2]==cs1 && (buffer[buffer.length-1] & 0xFF)==(cs2 & 0xFF)%256));
        return (buffer[buffer.length-1]!=0 || buffer[buffer.length-2]!=0) && buffer[buffer.length-2]==cs1 && (buffer[buffer.length-1] & 0xFF)==(cs2 & 0xFF)%256;

    }

    public void writeToOut(OutputStream out,int size, int mode) throws IOException {
        switch(mode) {
            case IOHelperInterface.MODE_BACKWARD:
                byte[] b = baos.toByteArray();
                byte[] rev_b = new byte[b.length];
                for(int i=1;i<=b.length;i++) {
                    if(i-1>size)
                        throw new IOException("Buffer out of range.");
                    rev_b[i-1] = b[b.length-i];
                }
                dump(b, "Command");
                out.write(rev_b);

                //out.flush();
                break;
            case IOHelperInterface.MODE_FORWARD:
            default:
                byte[] bb = baos.toByteArray();
                dump(bb, "Command");
                out.write(baos.toByteArray());
                break;
        }
    }

    public void readIn(InputStream in, int size, int mode) throws IOException {

        buffer = new byte[size];

        int t_size = 0;
        while(in.available()>0) {
            if(t_size>=buffer.length) {
                break;
                //throw new IOException("Buffer out of range: "+t_size);
            }

            t_size+= in.read(buffer, t_size, buffer.length);
        }

        if(t_size<size)
            new IOException("Empty buffer");

        if(mode == IOHelperInterface.MODE_BACKWARD) {
            byte[] rev_b = new byte[size];
            for(int i=1;i<=size;i++) {
                rev_b[i-1] = buffer[buffer.length - i];
            }
            buffer = rev_b;
        }

        byte[] bb = buffer;
        dump(bb, "Response");

        bais = new ByteArrayInputStream(buffer);
    }

    public void clearOut() throws IOException {
        baos.reset();
    }

    public void clearIn() throws IOException {
        bais.reset();
    }

    public static void dump(byte[] bb, String what) {

        System.out.println("-----> "+what);
        String charCode = "";
        for(int i=0;i<bb.length;i++) {
            String hexCode = Integer.toHexString((int)bb[i] & 0xFF).toUpperCase();
            System.out.print((hexCode.length()==1?"0"+hexCode:hexCode)+" ");
            int dCode = (int)bb[i] & 0xFF;

            charCode += dCode<display.length()?display.charAt(dCode):'.';
            if((i+1)%8==0) {
                System.out.println(charCode);
                charCode="";
            }
        }
        System.out.println("-----> //"+what);

    }

}

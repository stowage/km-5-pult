package org.ucs.wrapper.km5.container;

import org.ucs.wrapper.DataContainer;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 15.07.2005
 * Time: 20:43:56
 * To change this template use File | Settings | File Templates.
 */
public class ReadLineNumberDBErrContainer extends DataContainer {

    public int codeEventOrErr;

    public byte eeh;
    public int day;
    public int month;
    public int year;
    public int type;
    public int hours;
    public int minutes;
    public int seconds;  //9 ����

    public byte buffer;

}

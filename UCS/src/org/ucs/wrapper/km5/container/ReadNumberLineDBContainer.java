package org.ucs.wrapper.km5.container;

import org.ucs.wrapper.DataContainer;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 14.07.2005
 * Time: 6:28:26
 * To change this template use File | Settings | File Templates.
 */
public class ReadNumberLineDBContainer extends DataContainer {
    public byte specialIndication;
    public int numberLine;

    public byte eehEarly;
    public int numbMonthEarly;
    public int monthEarly;
    public int yearEarly;
    public int typeEarly;
    public int hoursEarly;
    public int minutesEarly;
    public int secondsEarly;

    public int numberEarlyLine;

    public byte eehLast;
    public int numbMonthLast;
    public int monthLast;
    public int yearLast;
    public int typeLast;
    public int hoursLast;
    public int minutesLast;
    public int secondsLast;

    public int numberLastLine;
    public int numberLineDB;
}

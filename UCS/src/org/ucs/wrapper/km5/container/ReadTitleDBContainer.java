package org.ucs.wrapper.km5.container;

import org.ucs.wrapper.DataContainer;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 14.07.2005
 * Time: 6:08:52
 * To change this template use File | Settings | File Templates.
 */
public class ReadTitleDBContainer extends DataContainer {
    public int addrStartDB;
    public int numbEEPROM;
    public byte specialFlags;
    public int numberLineDB;
    public int numberLastLine;
    public byte[] zero;
}

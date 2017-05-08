package org.ucs.wrapper.km5.container;

import org.ucs.wrapper.DataContainer;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 25.06.2005
 * Time: 20:26:40
 * To change this template use File | Settings | File Templates.
 */
public class ReadMonitorContainer extends DataContainer {
    public String display;
    public int isCancel;
    public int curPos;
    public byte buffer;
}

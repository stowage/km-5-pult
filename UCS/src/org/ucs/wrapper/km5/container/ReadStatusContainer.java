package org.ucs.wrapper.km5.container;

import org.ucs.wrapper.DataContainer;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 30.06.2005
 * Time: 21:57:23
 * To change this template use File | Settings | File Templates.
 */
public class ReadStatusContainer extends DataContainer {
    public int regimeGvs;
    public byte byteEmptyPipe;
    public byte byteOfHardErr;
    public byte regimeGvsMode;
    public byte byteSettingUp;
    public byte byteErrDevice;
    public byte byteFlagOfData;
    public byte byteDeviceProperties;
    public byte buffer;
}

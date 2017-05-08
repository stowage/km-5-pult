package org.ucs.wrapper.km5.container;

import org.ucs.wrapper.DataContainer;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 09.07.2005
 * Time: 15:57:12
 * To change this template use File | Settings | File Templates.
 */
public class ReadImmidiateValue2Container extends DataContainer {
    public byte zero;
    public float g1;
    public float g2;
    public float g3;
    public float t1;
    public float t2;
    public float tx;
    public float ta;
    public float p1;
    public float p2;
    public float p3;
    public float w;
    public float t2pps;
    public float txpps;
    public float tInDevice;
    public float w2;
    public float tGvs;
    public byte insideCounter;
}

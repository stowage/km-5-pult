package org.ucs.wrapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bliss
 * Date: 17.06.2005
 * Time: 21:26:00
 * To change this template use File | Settings | File Templates.
 */
public interface SessionModule {

    public InputStream getInput() throws IOException;
    public OutputStream getOutput() throws IOException;
    public void setConstraint(int timeout, int responseSize) throws Exception;
    
}

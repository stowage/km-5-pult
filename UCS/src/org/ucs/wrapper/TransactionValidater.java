package org.ucs.wrapper;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 15.11.2005
 * Time: 0:15:43
 * To change this template use File | Settings | File Templates.
 */
public interface TransactionValidater {
    public boolean validate(Object object,Method method, Object[] args);
}

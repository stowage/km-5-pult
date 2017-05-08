package org.ucs.wrapper;


import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 12.11.2005
 * Time: 2:16:19
 * To change this template use File | Settings | File Templates.
 */
public class CommandTransaction  {

   public static CommandModule DTI(CommandModule target, Transaction transaction) throws Exception {
        InvocationHandler handler = new DTI(target, transaction);
        Class cls = target.getClass();
        return (CommandModule)Proxy.newProxyInstance(cls.getClassLoader(),new Class[] { CommandModule.class },handler);
    }


}

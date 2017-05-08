package org.ucs.wrapper.km5;

import org.ucs.wrapper.TransactionValidater;
import org.ucs.wrapper.TransactionReanimator;
import org.ucs.wrapper.ConnectionModule;
import org.ucs.wrapper.DataContainer;

import java.lang.reflect.Method;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 20.11.2006
 * Time: 15:03:15
 * To change this template use File | Settings | File Templates.
 */
public class TransactionChecker implements TransactionValidater, TransactionReanimator {

    public boolean reanimate(ConnectionModule conn) {
        return true;
    }

    public boolean validate(Object object,Method method, Object[] args) {
        if(args!=null && args.length>0) {
            DataContainer dc = (DataContainer)args[0];
            //System.out.println("DCCommandTransValidate:"+dc.command);
            switch(dc.command) {
                case 0xEF:
                case 0xF0:
                   return true;
                case 0xFB:
                case 0xFC:
                case 0xFD:
                case 0xFE:
                case 0xFF:
                case 0xF1:
                   return false;
            }

            if(dc.command == 5) {
                return true;
            } else {
              //System.out.println("TransChecksum:"+dc.checkSum);

                return dc.checkSum;
            }
        }
        return false;
    }

}

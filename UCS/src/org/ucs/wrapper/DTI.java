package org.ucs.wrapper;

/*
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;

import com.caucho.hessian.server.HessianServlet;
import com.caucho.hessian.server.HessianSkeleton;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;*/
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 12.11.2005
 * Time: 2:34:44
 * To change this template use File | Settings | File Templates.
 */
public class DTI /*extends HessianServlet*/ implements InvocationHandler {

    Object target;
    Transaction transaction;

    public DTI(Object object, Transaction transaction) {
        this.target = object;
        this.transaction = transaction;
    }

    public Object invoke(Object object,Method method, Object[] args) throws Exception {
        Object returnValue = null;
        for(int i=0;i<transaction.numberOfTries;i++) {
            try {
                if(transaction.isSynchronized)
                    synchronized(transaction.conn.getSession()) {
                        returnValue = method.invoke(target,args);
                    }
                else
                    returnValue = method.invoke(target,args);

            } catch (Exception e) {
                e.printStackTrace(System.out);

                if(transaction.delay>0) {
                    try {
                        Thread.sleep(transaction.delay);
                    } catch (Exception ex) {}
                }

                continue;
            }
            if(transaction.validater!=null) {
                    if(!transaction.validater.validate(object, method, args))
                        continue;
            }
            //System.out.println("Exited");
            return returnValue;
        }

        throw new RuntimeException("Unable to continue transaction");
    }

    /*private HessianSkeleton skeleton = new HessianSkeleton(this);

    public void _service(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

       if (!req.getMethod().equals("POST")) {
                res.setStatus(500, "Data Provider Requires POST");
                PrintWriter out = res.getWriter();

                res.setContentType("text/html");
                out.println("<h1>Data Provider Requires POST</h1>");

                return;
       }

        InputStream is = request.getInputStream();
        OutputStream os = response.getOutputStream();

        HessianInput in = new HessianInput(is);
        HessianOutput out = new HessianOutput(os);

        try {
            skeleton.invoke(in, out);

        } catch (Throwable e) {
            throw new ServletException(e);
        }
    }
*/
}
